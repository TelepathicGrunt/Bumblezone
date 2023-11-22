package com.telepathicgrunt.the_bumblezone.modinit.registry.fabric;

import com.telepathicgrunt.the_bumblezone.mixin.fabric.block.PoiTypesAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.registry.BasicRegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;

import java.util.Collection;
import java.util.function.Supplier;

public class CustomResourcefulRegistry<T> implements ResourcefulRegistry<T> {

    private final RegistryEntries<T> entries = new RegistryEntries<>();
    private final Registry<T> registry;
    private final String id;

    public CustomResourcefulRegistry(Registry<T> registry, String id) {
        this.registry = registry;
        this.id = id;
    }

    @Override
    public <I extends T> RegistryEntry<I> register(String id, Supplier<I> supplier) {
        I value = Registry.register(registry, new ResourceLocation(this.id, id), supplier.get());
        if (value instanceof PoiType poiType) {
            PoiTypesAccessor.callRegisterBlockStates(
                (Holder<PoiType>) registry.getHolderOrThrow(registry.getResourceKey(value).orElseThrow()),
                poiType.matchingStates()
            );
        }
        return entries.add(new BasicRegistryEntry<>(new ResourceLocation(this.id, id), value));
    }

    @Override
    public Collection<RegistryEntry<T>> getEntries() {
        return this.entries.getEntries();
    }

    @Override
    public void init() {
        // NO-OP
    }
}
