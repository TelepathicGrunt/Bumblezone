package com.telepathicgrunt.the_bumblezone.dimension;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.generation.BzBiomeProvider;
import com.telepathicgrunt.the_bumblezone.generation.BzChunkGenerator;
import com.telepathicgrunt.the_bumblezone.modCompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modCompat.ProductiveBeesRedirection;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public class BzDimension {
    public static final RegistryKey<World> BZ_WORLD_KEY = RegistryKey.of(Registry.DIMENSION, Bumblezone.MOD_DIMENSION_ID);

    public static void setupDimension(){
        BzChunkGenerator.registerChunkgenerator();
        BzBiomeProvider.registerBiomeProvider();
    }

    public static void biomeModification(final BiomeLoadingEvent event) {
        boolean needToAddModCompatFeatures = ModChecker.productiveBeesPresent;

        if(needToAddModCompatFeatures && event.getName().getNamespace().equals(Bumblezone.MODID)){
            //Add our features to the bumblezone biomes
            ProductiveBeesRedirection.PBAddHoneycombs(event);
        }
    }
}
