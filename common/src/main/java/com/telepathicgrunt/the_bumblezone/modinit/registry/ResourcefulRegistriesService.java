package com.telepathicgrunt.the_bumblezone.modinit.registry;

import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfoRegistry;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Supplier;

public interface ResourcefulRegistriesService {
    ResourcefulRegistriesService INSTANCE = GeneralUtils.loadService(ResourcefulRegistriesService.class);

    <T> ResourcefulRegistry<T> create(ResourcefulRegistry<T> parent);

    <T> ResourcefulRegistry<T> create(Registry<T> registry, String id);

    <T, K extends Registry<T>> Pair<Supplier<CustomRegistryLookup<T>>, ResourcefulRegistry<T>> createCustomRegistryInternal(String modId, Class<T> type, ResourceKey<K> key, boolean save, boolean sync, boolean allowModification);

    FluidInfoRegistry createFluidRegistry(String id);
}
