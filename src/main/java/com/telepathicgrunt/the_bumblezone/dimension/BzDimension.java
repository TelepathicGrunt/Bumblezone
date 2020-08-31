package com.telepathicgrunt.the_bumblezone.dimension;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.generation.BzBiomeProvider;
import com.telepathicgrunt.the_bumblezone.generation.BzChunkGenerator;
import com.telepathicgrunt.the_bumblezone.surfacebuilders.BzSurfaceBuilders;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class BzDimension {
    public static final RegistryKey<World> BZ_WORLD_KEY = RegistryKey.of(Registry.DIMENSION, Bumblezone.MOD_DIMENSION_ID);

    public static void setupDimension(){
        BzChunkGenerator.registerChunkgenerator();
        BzBiomeProvider.registerBiomeProvider();

        BzSurfaceBuilders.registerSurfaceBuilders();
    }
}
