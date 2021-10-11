package com.telepathicgrunt.bumblezone.world.dimension.layer;

import com.telepathicgrunt.bumblezone.world.dimension.BzBiomeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.CastleTransformer;


public class BzBiomeScaleLayer implements CastleTransformer {
    private final ResourceLocation biomeToExpand;

    public BzBiomeScaleLayer(ResourceLocation biomeToExpand) {
        this.biomeToExpand = biomeToExpand;
    }

    @Override
    public int apply(Context context, int n, int e, int s, int w, int center) {
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