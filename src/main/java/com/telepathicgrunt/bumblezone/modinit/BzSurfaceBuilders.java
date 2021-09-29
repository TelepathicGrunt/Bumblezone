package com.telepathicgrunt.bumblezone.modinit;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.world.surfacebuilders.HoneySurfaceBuilder;
import com.telepathicgrunt.bumblezone.world.surfacebuilders.PollenSurfaceBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

public class BzSurfaceBuilders {
    public static final SurfaceBuilder<TernarySurfaceConfig> HONEY_SURFACE_BUILDER = new HoneySurfaceBuilder(TernarySurfaceConfig.CODEC);
    public static final SurfaceBuilder<TernarySurfaceConfig> POLLEN_SURFACE_BUILDER = new PollenSurfaceBuilder(TernarySurfaceConfig.CODEC);

    public static void registerSurfaceBuilders() {
        Registry.register(Registry.SURFACE_BUILDER, new Identifier(Bumblezone.MODID, "honey_surface_builder"), HONEY_SURFACE_BUILDER);
        Registry.register(Registry.SURFACE_BUILDER, new Identifier(Bumblezone.MODID, "pollen_surface_builder"), POLLEN_SURFACE_BUILDER);
    }
}
