package com.telepathicgrunt.the_bumblezone.modinit.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface CustomRegistry<T> extends ResourcefulRegistry<T> {

    static <T, K extends Registry<T>> CustomRegistry<T> of(String modId, Class<T> type, ResourceKey<K> key, boolean save, boolean sync, boolean allowModification) {
        Pair<Supplier<CustomRegistryLookup<T>>, ResourcefulRegistry<T>> pair = ResourcefulRegistries.createCustomRegistryInternal(modId, type, key, save, sync, allowModification);
        return new CustomRegistry<>() {
            @Override
            public CustomRegistryLookup<T> lookup() {
                return pair.getLeft().get();
            }

            @Override
            public ResourcefulRegistry<T> registry() {
                return pair.getRight();
            }
        };
    }

    CustomRegistryLookup<T> lookup();

    ResourcefulRegistry<T> registry();

    @Override
    default <I extends T> RegistryEntry<I> register(String id, Supplier<I> supplier) {
        return registry().register(id, supplier);
    }

    @Override
    default Collection<RegistryEntry<T>> getEntries() {
        return registry().getEntries();
    }

    @Override
    default Stream<RegistryEntry<T>> stream() {
        return registry().stream();
    }

    @Override
    default void init() {
        registry().init();
    }
}
