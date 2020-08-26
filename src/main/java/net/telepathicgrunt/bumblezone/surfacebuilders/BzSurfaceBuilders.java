package net.telepathicgrunt.bumblezone.surfacebuilders;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import net.telepathicgrunt.bumblezone.Bumblezone;

public class BzSurfaceBuilders {
    public static final SurfaceBuilder<TernarySurfaceConfig> HONEY_SURFACE_BUILDER = new HoneySurfaceBuilder(TernarySurfaceConfig.CODEC);

    public static void registerSurfaceBuilders() {
        Registry.register(Registry.SURFACE_BUILDER, new ResourceLocation(Bumblezone.MODID, "honey_surface_builder"), HONEY_SURFACE_BUILDER);
    }
}
