package com.telepathicgrunt.the_bumblezone.dimension.layer;

import com.telepathicgrunt.the_bumblezone.dimension.BzBiomeProvider;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;


public enum BzBiomeScalePillarLayer implements ICastleTransformer {
    INSTANCE;

    public int apply(INoiseRandom context, int n, int e, int s, int w, int center) {
        int hive_pillar_id = BzBiomeProvider.LAYERS_BIOME_REGISTRY.getId(
                BzBiomeProvider.LAYERS_BIOME_REGISTRY.getOrDefault(BzBiomeProvider.HIVE_PILLAR));

        if(center != hive_pillar_id){
            boolean borderingHivePillar = false;

            if((n == hive_pillar_id ||
                e == hive_pillar_id) ||
                    (w == hive_pillar_id ||
                    s == hive_pillar_id)) {
                borderingHivePillar = true;
            }

            if (borderingHivePillar) {
                return hive_pillar_id;
            }
        }
        return center;
    }
}