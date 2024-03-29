package com.telepathicgrunt.the_bumblezone.modinit.registry.neoforge;

import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfo;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfoRegistry;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidProperties;
import com.telepathicgrunt.the_bumblezone.fluids.neoforge.BzFluidType;
import com.telepathicgrunt.the_bumblezone.fluids.neoforge.ForgeFluidInfo;
import com.telepathicgrunt.the_bumblezone.modinit.registry.BasicRegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Collection;

public class NeoForgeFluidInfoRegistry implements FluidInfoRegistry {

    private final String modid;
    private final DeferredRegister<FluidType> registry;
    private final RegistryEntries<FluidInfo> entries = new RegistryEntries<>();

    public NeoForgeFluidInfoRegistry(String modid) {
        this.modid = modid;
        this.registry = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, modid);
    }

    @Override
    public RegistryEntry<FluidInfo> register(FluidProperties.Builder properties) {
        FluidProperties props = properties.build(this.modid);
        DeferredHolder<FluidType, BzFluidType> type = registry.register(props.id().getPath(), () -> BzFluidType.of(props));
        ForgeFluidInfo info = new ForgeFluidInfo(type, props);
        return this.entries.add(new BasicRegistryEntry<>(props.id(), info));
    }

    @Override
    public Collection<RegistryEntry<FluidInfo>> getEntries() {
        return this.entries.getEntries();
    }

    @Override
    public void init() {
        this.registry.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
