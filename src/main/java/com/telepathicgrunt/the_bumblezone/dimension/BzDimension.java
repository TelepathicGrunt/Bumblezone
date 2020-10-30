package com.telepathicgrunt.the_bumblezone.dimension;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.generation.BzBiomeProvider;
import com.telepathicgrunt.the_bumblezone.generation.BzChunkGenerator;
import com.telepathicgrunt.the_bumblezone.modCompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modCompat.ProductiveBeesRedirection;
import com.telepathicgrunt.the_bumblezone.modCompat.ResourcefulBeesRedirection;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public class BzDimension {
    public static final RegistryKey<World> BZ_WORLD_KEY = RegistryKey.of(Registry.DIMENSION, Bumblezone.MOD_DIMENSION_ID);

    public static void setupDimension() {
        BzChunkGenerator.registerChunkgenerator();
        BzBiomeProvider.registerBiomeProvider();
    }

    public static void biomeModification(final BiomeLoadingEvent event) {
        if (event.getName() != null && event.getName().getNamespace().equals(Bumblezone.MODID)) {
            //Add our features to the bumblezone biomes

            if (ModChecker.productiveBeesPresent)
                ProductiveBeesRedirection.PBAddWorldgen(event);

            if (ModChecker.resourcefulBeesPresent)
                ResourcefulBeesRedirection.RBAddWorldgen(event);
        }
    }
}
