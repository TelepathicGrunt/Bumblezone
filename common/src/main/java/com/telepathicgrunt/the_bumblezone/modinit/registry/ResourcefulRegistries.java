package com.telepathicgrunt.the_bumblezone.modinit.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.apache.commons.lang3.NotImplementedException;

public class ResourcefulRegistries {

    public static <T> ResourcefulRegistry<T> create(ResourcefulRegistry<T> parent) {
        return new ResourcefulRegistryChild<>(parent);
    }


    public static <T> ResourcefulRegistry<T> create(Registry<T> registry, String id) {
        return create(registry.key(), id);
    }

    @ExpectPlatform
    public static <T, K extends Registry<T>> ResourcefulRegistry<T> create(ResourceKey<K> registry, String id) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static <T, K extends Registry<T>> RegistryEntry<Registry<T>> createCustomRegistry(ResourceKey<K> key) {
        throw new NotImplementedException();
    }
}
