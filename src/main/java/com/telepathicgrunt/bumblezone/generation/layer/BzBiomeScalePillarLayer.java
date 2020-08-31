package com.telepathicgrunt.bumblezone.generation.layer;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.generation.BzBiomeProvider;


public enum BzBiomeScalePillarLayer implements ICastleTransformer {
    INSTANCE;

    private static final ResourceLocation HIVE_PILLAR = new ResourceLocation(Bumblezone.MODID, "hive_pillar");

    public int apply(INoiseRandom context, int n, int e, int s, int w, int center) {
        int hive_pillar_id = BzBiomeProvider.layersBiomeRegistry.getId(BzBiomeProvider.layersBiomeRegistry.getOrDefault(HIVE_PILLAR));
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