package com.telepathicgrunt.the_bumblezone.world.dimension.layer;

import com.telepathicgrunt.the_bumblezone.world.dimension.BzBiomeProvider;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;


public enum BzBiomeNonstandardLayer implements IAreaTransformer0 {
    INSTANCE;

    public int applyPixel(INoiseRandom noise, int x, int z) {
        if(noise.nextRandom(10) == 0){
            return BzBiomeProvider.LAYERS_BIOME_REGISTRY.getId(
                        BzBiomeProvider.NONSTANDARD_BIOME.get(
                            noise.nextRandom(BzBiomeProvider.NONSTANDARD_BIOME.size())));
        }
        else{
            return -1;
        }
    }
}