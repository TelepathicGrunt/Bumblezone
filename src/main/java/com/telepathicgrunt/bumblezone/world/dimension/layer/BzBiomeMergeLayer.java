package com.telepathicgrunt.bumblezone.world.dimension.layer;


import net.minecraft.world.biome.layer.type.MergingLayer;
import net.minecraft.world.biome.layer.util.IdentityCoordinateTransformer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;
import net.minecraft.world.biome.layer.util.LayerSampler;

public enum BzBiomeMergeLayer implements MergingLayer, IdentityCoordinateTransformer {
    INSTANCE;

    public int sample(LayerRandomnessSource iNoiseRandom, LayerSampler iArea, LayerSampler iArea1, int x, int z) {
        int biomeID1 = iArea.sample(this.transformX(x), this.transformZ(z));
        int biomeID2 = iArea1.sample(this.transformX(x), this.transformZ(z));

        if (biomeID1 == -1) {
            return biomeID2;
        }
        else {
            return biomeID1;
        }
    }
}
