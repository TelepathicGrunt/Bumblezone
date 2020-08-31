package com.telepathicgrunt.bumblezone.dimension;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.generation.BzBiomeProvider;
import com.telepathicgrunt.bumblezone.generation.BzChunkGenerator;
import com.telepathicgrunt.bumblezone.surfacebuilders.BzSurfaceBuilders;

public class BzDimension {
    public static final RegistryKey<World> BZ_WORLD_KEY = RegistryKey.of(Registry.DIMENSION, Bumblezone.MOD_DIMENSION_ID);

    public static void setupDimension(){
        BzChunkGenerator.registerChunkgenerator();
        BzBiomeProvider.registerBiomeProvider();

        BzSurfaceBuilders.registerSurfaceBuilders();
    }
}
