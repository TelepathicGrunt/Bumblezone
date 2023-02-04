package com.telepathicgrunt.the_bumblezone.modinit.registry;

import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfoRegistry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Supplier;

public class ResourcefulRegistries {

    public static <T> ResourcefulRegistry<T> create(ResourcefulRegistry<T> parent) {
        return new ResourcefulRegistryChild<>(parent);
    }


    @ExpectPlatform
    public static <T> ResourcefulRegistry<T> create(Registry<T> registry, String id) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static <T, K extends Registry<T>> Pair<Supplier<CustomRegistryLookup<T>>, ResourcefulRegistry<T>> createCustomRegistryInternal(String modId, Class<T> type, ResourceKey<K> key, boolean save, boolean sync, boolean allowModification) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static FluidInfoRegistry createFluidRegistry(String id) {
        throw new NotImplementedException();
    }
}
