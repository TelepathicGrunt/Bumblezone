package com.telepathicgrunt.the_bumblezone.world.dimension.layer;


import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.Area;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.AreaTransformer2;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.Context;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.DimensionOffset0Transformer;

public class BzBiomeMergeLayer implements AreaTransformer2, DimensionOffset0Transformer {

    public int applyPixel(Context iNoiseRandom, Area iArea, Area iArea1, int x, int z) {
        int biomeID1 = iArea.get(this.getParentX(x), this.getParentZ(z));
        int biomeID2 = iArea1.get(this.getParentX(x), this.getParentZ(z));

        if (biomeID1 == -1) {
            return biomeID2;
        }
        else {
            return biomeID1;
        }
    }
}
