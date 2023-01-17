package com.telepathicgrunt.the_bumblezone.modinit.registry;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

public interface CustomRegistryLookup<T> extends Iterable<T> {

    boolean containsKey(ResourceLocation id);

    boolean containsValue(T value);

    @Nullable
    T get(ResourceLocation id);

    @Nullable
    ResourceLocation getKey(T value);

    Collection<T> getValues();

    Collection<ResourceLocation> getKeys();

}
