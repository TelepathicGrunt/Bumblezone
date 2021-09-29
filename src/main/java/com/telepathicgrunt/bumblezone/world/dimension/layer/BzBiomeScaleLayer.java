package com.telepathicgrunt.bumblezone.world.dimension.layer;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.world.dimension.BzBiomeProvider;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.layer.type.CrossSamplingLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;


public class BzBiomeScaleLayer implements CrossSamplingLayer {
    private final Identifier biomeToExpand;

    public BzBiomeScaleLayer(Identifier biomeToExpand) {
        this.biomeToExpand = biomeToExpand;
    }

    @Override
    public int sample(LayerRandomnessSource context, int n, int e, int s, int w, int center) {
        int hivePillarId = BzBiomeProvider.LAYERS_BIOME_REGISTRY.getRawId(
                BzBiomeProvider.LAYERS_BIOME_REGISTRY.get(this.biomeToExpand));

        if(center != hivePillarId){
            boolean borderingHivePillar = false;

            if((n == hivePillarId || e == hivePillarId) ||
                    (w == hivePillarId || s == hivePillarId)) {
                borderingHivePillar = true;
            }

            if (borderingHivePillar) {
                return hivePillarId;
            }
        }
        return center;
    }
}