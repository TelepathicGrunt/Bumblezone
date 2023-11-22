package com.telepathicgrunt.the_bumblezone.modinit.registry.neoforge;

import com.telepathicgrunt.the_bumblezone.modinit.registry.CustomRegistryLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

public class NeoForgeCustomRegistry<T, K extends T> implements CustomRegistryLookup<T, K> {

    private final Registry<T> registry;

    public NeoForgeCustomRegistry(Registry<T> registry) {
        this.registry = registry;
    }

    @Override
    public boolean containsKey(ResourceLocation id) {
        return registry.containsKey(id);
    }

    @Override
    public @Nullable T get(ResourceLocation id) {
        return registry.get(id);
    }

    @Override
    public Collection<T> getValues() {
        return registry.stream().collect(Collectors.toList());
    }

    @Override
    public Collection<ResourceLocation> getKeys() {
        return registry.keySet();
    }

    @Override
    public @Nullable ResourceLocation getKey(Object value) {
        return registry.getKey((T) value);
    }

    @Override
    public boolean containsValue(Object value) {
        return registry.containsValue((T) value);
    }

    @NotNull
    @Override
    public Iterator iterator() {
        return registry.iterator();
    }
}
