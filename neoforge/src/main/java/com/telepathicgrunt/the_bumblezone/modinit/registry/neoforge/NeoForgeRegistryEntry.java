package com.telepathicgrunt.the_bumblezone.modinit.registry.neoforge;

import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

public class NeoForgeRegistryEntry<R, T extends R> implements RegistryEntry<T> {

    private final DeferredHolder<R, T> object;

    public NeoForgeRegistryEntry(DeferredHolder<R, T> object) {
        this.object = object;
    }

    @Override
    public T get() {
        return object.get();
    }

    @Override
    public ResourceLocation getId() {
        return object.getId();
    }
}