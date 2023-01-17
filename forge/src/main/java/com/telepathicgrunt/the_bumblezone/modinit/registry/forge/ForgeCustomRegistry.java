package com.telepathicgrunt.the_bumblezone.modinit.registry.forge;

import com.telepathicgrunt.the_bumblezone.modinit.registry.CustomRegistryLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Supplier;

public class ForgeCustomRegistry<T> implements CustomRegistryLookup<T> {

    private final Supplier<IForgeRegistry<T>> registry;

    public ForgeCustomRegistry(Supplier<IForgeRegistry<T>> registry) {
        this.registry = registry;
    }

    @Override
    public boolean containsKey(ResourceLocation id) {
        return registry.get().containsKey(id);
    }

    @Override
    public boolean containsValue(T value) {
        return registry.get().containsValue(value);
    }

    @Override
    public @Nullable T get(ResourceLocation id) {
        return registry.get().getValue(id);
    }

    @Override
    public @Nullable ResourceLocation getKey(T value) {
        return registry.get().getKey(value);
    }

    @Override
    public Collection<T> getValues() {
        return registry.get().getValues();
    }

    @Override
    public Collection<ResourceLocation> getKeys() {
        return registry.get().getKeys();
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return registry.get().iterator();
    }
}
