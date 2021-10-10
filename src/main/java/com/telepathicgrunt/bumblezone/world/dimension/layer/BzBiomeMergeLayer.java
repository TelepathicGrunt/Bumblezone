package com.telepathicgrunt.bumblezone.world.dimension.layer;


import net.minecraft.world.level.newbiome.area.Area;
import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer2;
import net.minecraft.world.level.newbiome.layer.traits.DimensionOffset0Transformer;

public enum BzBiomeMergeLayer implements AreaTransformer2, DimensionOffset0Transformer {
    INSTANCE;

    public int applyPixel(Context iNoiseRandom, Area iArea, Area iArea1, int x, int z) {
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
