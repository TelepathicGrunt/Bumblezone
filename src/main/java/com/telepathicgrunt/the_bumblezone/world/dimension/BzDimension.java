package com.telepathicgrunt.the_bumblezone.world.dimension;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class BzDimension {
    public static final RegistryKey<World> BZ_WORLD_KEY = RegistryKey.create(Registry.DIMENSION_REGISTRY, Bumblezone.MOD_DIMENSION_ID);

    public static void setupDimension() {
        BzChunkGenerator.registerChunkgenerator();
        BzBiomeProvider.registerBiomeProvider();
    }

    // BiomeLoadEvent is too early for our tag blacklist to allow blacklisting features.
//    public static void biomeModification(final BiomeLoadingEvent event) {
//        if (event.getName() != null && event.getName().getNamespace().equals(Bumblezone.MODID)) {
//            //Add our features to the bumblezone biomes
//
//            if (ModChecker.productiveBeesPresent)
//                ProductiveBeesCompat.PBAddWorldgen(event);
//
//            if (ModChecker.resourcefulBeesPresent)
//                ResourcefulBeesCompat.RBAddWorldgen(event);
//        }
//    }
}
