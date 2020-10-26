package com.telepathicgrunt.the_bumblezone.surfacebuilders;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.generation.BzBiomeProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.BiomeMaker;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.event.RegistryEvent;

public class BzSurfaceBuilders {
    public static final SurfaceBuilder<SurfaceBuilderConfig> HONEY_SURFACE_BUILDER = new HoneySurfaceBuilder(SurfaceBuilderConfig.CODEC);

    public static void registerSurfaceBuilders(final RegistryEvent.Register<SurfaceBuilder<?>> event) {
        event.getRegistry().register(HONEY_SURFACE_BUILDER.setRegistryName(new ResourceLocation(Bumblezone.MODID, "honey_surface_builder")));
    }
}
