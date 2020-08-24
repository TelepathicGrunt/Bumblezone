package net.telepathicgrunt.bumblezone.dimension;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.features.BzConfiguredFeatures;
import net.telepathicgrunt.bumblezone.features.BzFeatures;
import net.telepathicgrunt.bumblezone.features.decorators.BzPlacements;
import net.telepathicgrunt.bumblezone.generation.BzBiomeProvider;
import net.telepathicgrunt.bumblezone.generation.BzChunkGenerator;
import net.telepathicgrunt.bumblezone.surfacebuilders.BzSurfaceBuilders;

public class BzDimension {
    public static final RegistryKey<DimensionType> BZ_DIMENSION_KEY = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, Bumblezone.MOD_DIMENSION_ID);
    public static final RegistryKey<World> BZ_WORLD_KEY = RegistryKey.of(Registry.DIMENSION, Bumblezone.MOD_DIMENSION_ID);

    public static void setupDimension(){
        BzChunkGenerator.registerChunkgenerator();
        BzBiomeProvider.registerBiomeProvider();

        BzSurfaceBuilders.registerSurfaceBuilders();
        BzPlacements.registerPlacements();
        BzFeatures.registerFeatures();
        BzConfiguredFeatures.registerConfiguredFeatures();
    }
}
