package com.telepathicgrunt.the_bumblezone.modinit.registry.fabric;

import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfo;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfoRegistry;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidProperties;
import com.telepathicgrunt.the_bumblezone.fluids.fabric.FabricFluidInfo;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;

public class FabricFluidInfoRegistry implements FluidInfoRegistry {

    private final RegistryEntries<FluidInfo> entries = new RegistryEntries<>();
    private final String modid;

    public FabricFluidInfoRegistry(String modid) {
        this.modid = modid;
    }

    @Override
    public RegistryEntry<FluidInfo> register(FluidProperties.Builder properties) {
        FabricFluidInfo info = new FabricFluidInfo(properties.build(modid));
        return entries.add(new RegistryEntry<>() {
            @Override
            public FluidInfo get() {
                return info;
            }

            @Override
            public ResourceLocation getId() {
                return info.properties().id();
            }
        });
    }

    @Override
    public Collection<RegistryEntry<FluidInfo>> getEntries() {
        return entries.getEntries();
    }

    @Override
    public void init() {

    }
}
