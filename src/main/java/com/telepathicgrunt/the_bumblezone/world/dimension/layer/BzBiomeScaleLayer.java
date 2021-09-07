package com.telepathicgrunt.the_bumblezone.world.dimension.layer;

import com.telepathicgrunt.the_bumblezone.world.dimension.BzBiomeProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;


public class BzBiomeScaleLayer implements ICastleTransformer {

    private ResourceLocation biomeToExpand;

    public BzBiomeScaleLayer(ResourceLocation biomeToExpand) {
        this.biomeToExpand = biomeToExpand;
    }

    public int apply(INoiseRandom context, int n, int e, int s, int w, int center) {
        int hivePillarId = BzBiomeProvider.LAYERS_BIOME_REGISTRY.getId(
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