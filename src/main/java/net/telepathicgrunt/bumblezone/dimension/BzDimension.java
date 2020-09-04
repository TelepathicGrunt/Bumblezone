package net.telepathicgrunt.bumblezone.dimension;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.generation.BzBiomeProvider;
import net.telepathicgrunt.bumblezone.generation.BzChunkGenerator;
import net.telepathicgrunt.bumblezone.surfacebuilders.BzSurfaceBuilders;

public class BzDimension {
    public static final RegistryKey<World> BZ_WORLD_KEY = RegistryKey.of(Registry.DIMENSION, Bumblezone.MOD_DIMENSION_ID);

    public static void setupDimension(){
        BzChunkGenerator.registerChunkgenerator();
        BzBiomeProvider.registerBiomeProvider();

        BzSurfaceBuilders.registerSurfaceBuilders();
    }
}
