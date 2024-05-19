package com.telepathicgrunt.the_bumblezone.modinit.registry.fabric;

import com.telepathicgrunt.the_bumblezone.fluids.base.FluidDataRegistry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.CustomRegistryLookup;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Supplier;

public class ResourcefulRegistriesImpl {
    public static <T> ResourcefulRegistry<T> create(Registry<T> registry, String id) {
        return new CustomResourcefulRegistry<>(registry, id);
    }

    public static <T, R extends T, K extends Registry<T>> Pair<Supplier<CustomRegistryLookup<T, T>>, ResourcefulRegistry<T>> createCustomRegistryInternal(String modId, ResourceKey<K> key, boolean save, boolean sync, boolean allowModification) {
        FabricRegistryBuilder<T, MappedRegistry<T>> registry = FabricRegistryBuilder.createSimple(null, key.location());
        if (sync) registry.attribute(RegistryAttribute.SYNCED);
        if (allowModification) registry.attribute(RegistryAttribute.MODDED);
        MappedRegistry<T> builtRegistry = registry.buildAndRegister();
        CustomRegistry<T> customRegistry = new CustomRegistry<>(builtRegistry);
        return Pair.of(() -> customRegistry, new CustomResourcefulRegistry<>(builtRegistry, modId));
    }

    public static FluidDataRegistry createFluidRegistry(String id) {
        return new CustomFluidDataRegistry(id);
    }
}
