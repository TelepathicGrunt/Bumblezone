package com.telepathicgrunt.bumblezone.world.dimension.layer;


import com.telepathicgrunt.bumblezone.world.dimension.BzBiomeProvider;
import net.minecraft.world.biome.layer.type.InitLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;

public enum BzBiomeNonstandardLayer implements InitLayer {
    INSTANCE;

    public int sample(LayerRandomnessSource noise, int x, int z) {
        if(!BzBiomeProvider.NONSTANDARD_BIOME.isEmpty() && noise.nextInt(10) == 0){
            return BzBiomeProvider.LAYERS_BIOME_REGISTRY.getRawId(
                        BzBiomeProvider.NONSTANDARD_BIOME.get(
                            noise.nextInt(BzBiomeProvider.NONSTANDARD_BIOME.size())));
        }
        else{
            return -1;
        }
    }
}