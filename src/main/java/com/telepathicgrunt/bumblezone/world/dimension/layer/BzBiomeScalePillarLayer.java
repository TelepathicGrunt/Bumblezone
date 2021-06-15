package com.telepathicgrunt.bumblezone.world.dimension.layer;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.world.dimension.BzBiomeProvider;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.layer.type.CrossSamplingLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;


public enum BzBiomeScalePillarLayer implements CrossSamplingLayer {
    INSTANCE;

    private static final Identifier HIVE_PILLAR = new Identifier(Bumblezone.MODID, "hive_pillar");

    @Override
    public int sample(LayerRandomnessSource context, int n, int e, int s, int w, int center) {
        int hive_pillar_id = BzBiomeProvider.layersBiomeRegistry.getRawId(BzBiomeProvider.layersBiomeRegistry.get(HIVE_PILLAR));
        if(center != hive_pillar_id){
            boolean borderingHivePillar = false;

            if((n == hive_pillar_id || e == hive_pillar_id) ||
                (w == hive_pillar_id || s == hive_pillar_id)) {
                borderingHivePillar = true;
            }

            if (borderingHivePillar) {
                return hive_pillar_id;
            }
        }
        return center;
    }
}