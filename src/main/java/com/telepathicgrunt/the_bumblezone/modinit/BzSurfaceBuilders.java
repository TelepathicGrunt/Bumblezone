package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.world.surfacebuilders.HoneySurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BzSurfaceBuilders {
    public static final DeferredRegister<SurfaceBuilder<?>> SURFACE_BUILDERS = DeferredRegister.create(ForgeRegistries.SURFACE_BUILDERS, Bumblezone.MODID);
    public static final RegistryObject<SurfaceBuilder<SurfaceBuilderConfig>> HONEY_SURFACE_BUILDER = SURFACE_BUILDERS.register("honey_surface_builder", () -> new HoneySurfaceBuilder(SurfaceBuilderConfig.CODEC));
}
