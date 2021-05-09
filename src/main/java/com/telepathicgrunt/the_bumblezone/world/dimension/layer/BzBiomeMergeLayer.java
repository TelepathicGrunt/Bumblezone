package com.telepathicgrunt.the_bumblezone.world.dimension.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.layer.traits.IAreaTransformer2;
import net.minecraft.world.gen.layer.traits.IDimOffset0Transformer;


public enum BzBiomeMergeLayer implements IAreaTransformer2, IDimOffset0Transformer {
    INSTANCE;

    public int applyPixel(INoiseRandom iNoiseRandom, IArea iArea, IArea iArea1, int x, int z) {
        int biomeID1 = iArea.get(this.getParentX(x), this.getParentY(z));
        int biomeID2 = iArea1.get(this.getParentX(x), this.getParentY(z));

        if (biomeID1 == -1) {
            return biomeID2;
        }
        else {
            return biomeID1;
        }
    }
}
