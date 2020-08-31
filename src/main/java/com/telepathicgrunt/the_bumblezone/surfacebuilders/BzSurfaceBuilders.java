package com.telepathicgrunt.the_bumblezone.surfacebuilders;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class BzSurfaceBuilders {
    public static final SurfaceBuilder<SurfaceBuilderConfig> HONEY_SURFACE_BUILDER = new HoneySurfaceBuilder(SurfaceBuilderConfig.CODEC);

    public static void registerSurfaceBuilders() {
        Registry.register(Registry.SURFACE_BUILDER, new ResourceLocation(Bumblezone.MODID, "honey_surface_builder"), HONEY_SURFACE_BUILDER);
    }
}
