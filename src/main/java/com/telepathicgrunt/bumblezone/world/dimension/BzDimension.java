package com.telepathicgrunt.bumblezone.world.dimension;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.modinit.BzSurfaceBuilders;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class BzDimension {
    public static final ResourceKey<Level> BZ_WORLD_KEY = ResourceKey.create(Registry.DIMENSION_REGISTRY, Bumblezone.MOD_DIMENSION_ID);

    public static void setupDimension() {
        BzChunkGenerator.registerChunkgenerator();
        BzBiomeProvider.registerBiomeProvider();

        BzSurfaceBuilders.registerSurfaceBuilders();
    }
}
