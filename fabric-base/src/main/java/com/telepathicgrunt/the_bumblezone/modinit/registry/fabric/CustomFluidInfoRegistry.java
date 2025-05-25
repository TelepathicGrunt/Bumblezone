package com.telepathicgrunt.the_bumblezone.modinit.registry.fabric;

import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfo;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfoRegistry;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidProperties;
import com.telepathicgrunt.the_bumblezone.fluids.fabric.BaseFluidInfo;
import com.telepathicgrunt.the_bumblezone.modinit.registry.BasicRegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;

import java.util.Collection;

public class CustomFluidInfoRegistry implements FluidInfoRegistry {

    private final RegistryEntries<FluidInfo> entries = new RegistryEntries<>();
    private final String modid;

    public CustomFluidInfoRegistry(String modid) {
        this.modid = modid;
    }

    @Override
    public RegistryEntry<FluidInfo> register(FluidProperties.Builder properties) {
        BaseFluidInfo info = new BaseFluidInfo(properties.build(modid));
        return this.entries.add(new BasicRegistryEntry<>(info.properties().id(), info));
    }

    @Override
    public Collection<RegistryEntry<FluidInfo>> getEntries() {
        return entries.getEntries();
    }

    @Override
    public void init() {

    }
}
