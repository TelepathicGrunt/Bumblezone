package com.telepathicgrunt.the_bumblezone.modinit.registry.fabric;

import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidDataRegistry;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidProperties;
import com.telepathicgrunt.the_bumblezone.fluids.fabric.BaseFluidData;
import com.telepathicgrunt.the_bumblezone.modinit.registry.BasicRegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;

import java.util.Collection;

public class CustomFluidDataRegistry implements FluidDataRegistry {

    private final RegistryEntries<FluidData> entries = new RegistryEntries<>();
    private final String modid;

    public CustomFluidDataRegistry(String modid) {
        this.modid = modid;
    }

    @Override
    public RegistryEntry<FluidData> register(FluidProperties.Builder properties) {
        BaseFluidData info = new BaseFluidData(properties.build(modid));
        return this.entries.add(new BasicRegistryEntry<>(info.properties().id(), info));
    }

    @Override
    public Collection<RegistryEntry<FluidData>> getEntries() {
        return entries.getEntries();
    }

    @Override
    public void init() {

    }
}
