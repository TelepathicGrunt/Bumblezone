package com.telepathicgrunt.the_bumblezone.modinit.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public class BasicRegistryEntry<T> implements RegistryEntry<T> {

    private final ResourceLocation id;
    private final T value;

    public BasicRegistryEntry(ResourceLocation id, T value) {
        this.id = Objects.requireNonNull(id);
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }
}
