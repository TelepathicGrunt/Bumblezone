package com.telepathicgrunt.bumblezone.world.dimension.layer;


import com.telepathicgrunt.bumblezone.world.dimension.BzBiomeProvider;
import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer0;

public enum BzBiomeNonstandardLayer implements AreaTransformer0 {
    INSTANCE;

    public int applyPixel(Context noise, int x, int z) {
        if(!BzBiomeProvider.NONSTANDARD_BIOME.isEmpty() && noise.nextRandom(10) == 0){
            return BzBiomeProvider.LAYERS_BIOME_REGISTRY.getId(
                        BzBiomeProvider.NONSTANDARD_BIOME.get(
                            noise.nextRandom(BzBiomeProvider.NONSTANDARD_BIOME.size())));
        }
        else{
            return -1;
        }
    }
}