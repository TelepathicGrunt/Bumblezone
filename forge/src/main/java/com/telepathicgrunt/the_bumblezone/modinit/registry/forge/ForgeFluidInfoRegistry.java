package com.telepathicgrunt.the_bumblezone.modinit.registry.forge;

import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfo;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfoRegistry;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidProperties;
import com.telepathicgrunt.the_bumblezone.fluids.forge.BzFluidType;
import com.telepathicgrunt.the_bumblezone.fluids.forge.ForgeFluidInfo;
import com.telepathicgrunt.the_bumblezone.modinit.registry.BasicRegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;

public class ForgeFluidInfoRegistry implements FluidInfoRegistry {

    private final String modid;
    private final DeferredRegister<FluidType> registry;
    private final RegistryEntries<FluidInfo> entries = new RegistryEntries<>();

    public ForgeFluidInfoRegistry(String modid) {
        this.modid = modid;
        this.registry = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, modid);
    }

    @Override
    public RegistryEntry<FluidInfo> register(FluidProperties.Builder properties) {
        FluidProperties props = properties.build(this.modid);
        RegistryObject<BzFluidType> type = registry.register(props.id().getPath(), () -> BzFluidType.of(props));
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
