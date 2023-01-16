package com.telepathicgrunt.the_bumblezone.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.BaseMapCodec;

import java.util.Map;
import java.util.Objects;

public record LenientUnboundedMapCodec<K, V>(Codec<K> keyCodec, Codec<V> elementCodec) implements BaseMapCodec<K, V>, Codec<Map<K, V>> {

    @Override
    public <T> DataResult<Map<K, V>> decode(DynamicOps<T> ops, MapLike<T> input) {
        final ImmutableMap.Builder<K, V> read = ImmutableMap.builder();
        final ImmutableList.Builder<Pair<T, T>> failed = ImmutableList.builder();

        final DataResult<Unit> result = input.entries().reduce(
                DataResult.success(Unit.INSTANCE, Lifecycle.stable()),
                (r, pair) -> {
                    final DataResult<K> k = keyCodec().parse(ops, pair.getFirst());
                    final DataResult<V> v = elementCodec().parse(ops, pair.getSecond());

                    final DataResult<Pair<K, V>> entry = k.apply2stable(Pair::of, v);
                    final Either<Pair<K, V>, DataResult.PartialResult<Pair<K, V>>> either = entry.get();
                    either.ifLeft(p -> read.put(p.getFirst(), p.getSecond()));
                    either.ifRight(e -> failed.add(pair)); // We use an either here as it doesn't create optional and just consumes on the ifs
                    return r.apply2stable((u, p) -> u, entry);
                },
                (r1, r2) -> r1.apply2stable((u1, u2) -> u1, r2)
        );

        final Map<K, V> elements = read.build();
        final T errors = ops.createMap(failed.build().stream());

        return result.map(unit -> elements)
                .setPartial(elements)
                .mapError(e -> e + " missed input: " + errors);
    }

    @Override
    public <T> DataResult<Pair<Map<K, V>, T>> decode(final DynamicOps<T> ops, final T input) {
        return ops.getMap(input).setLifecycle(Lifecycle.stable()).flatMap(map -> decode(ops, map)).map(r -> Pair.of(r, input));
    }

    @Override
    public <T> DataResult<T> encode(final Map<K, V> input, final DynamicOps<T> ops, final T prefix) {
        return encode(input, ops, ops.mapBuilder()).build(prefix);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LenientUnboundedMapCodec<?, ?> that = (LenientUnboundedMapCodec<?, ?>) o;
        return Objects.equals(keyCodec, that.keyCodec) && Objects.equals(elementCodec, that.elementCodec);
    }

    @Override
    public String toString() {
        return "LenientUnboundedMapCodec[" + keyCodec + " -> " + elementCodec + ']';
    }
}