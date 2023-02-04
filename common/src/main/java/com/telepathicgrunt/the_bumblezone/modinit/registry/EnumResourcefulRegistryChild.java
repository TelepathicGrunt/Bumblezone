package com.telepathicgrunt.the_bumblezone.modinit.registry;

import java.util.EnumMap;
import java.util.function.Supplier;

public class EnumResourcefulRegistryChild<E extends Enum<E>, T> extends ResourcefulRegistryChild<T> {

    private final EnumMap<E, RegistryEntries<T>> entries;

    public EnumResourcefulRegistryChild(Class<E> enumClass, ResourcefulRegistry<T> parent) {
        super(parent);
        entries = new EnumMap<>(enumClass);
    }

    public <I extends T> RegistryEntry<I> register(E enumValue, String id, Supplier<I> supplier) {
        return entries.computeIfAbsent(enumValue, a -> new RegistryEntries<>())
            .add(super.register(id, supplier));
    }

    public RegistryEntries<T> getEntries(E enumValue) {
        return entries.get(enumValue);
    }

}