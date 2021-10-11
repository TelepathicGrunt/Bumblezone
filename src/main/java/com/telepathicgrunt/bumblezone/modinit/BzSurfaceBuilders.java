package com.telepathicgrunt.bumblezone.modinit;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.world.surfacebuilders.HoneySurfaceBuilder;
import com.telepathicgrunt.bumblezone.world.surfacebuilders.PollenSurfaceBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;

public class BzSurfaceBuilders {
    public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> HONEY_SURFACE_BUILDER = new HoneySurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC);
    public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> POLLEN_SURFACE_BUILDER = new PollenSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC);

    public static void registerSurfaceBuilders() {
        Registry.register(Registry.SURFACE_BUILDER, new ResourceLocation(Bumblezone.MODID, "honey_surface_builder"), HONEY_SURFACE_BUILDER);
        Registry.register(Registry.SURFACE_BUILDER, new ResourceLocation(Bumblezone.MODID, "pollen_surface_builder"), POLLEN_SURFACE_BUILDER);
    }
}
