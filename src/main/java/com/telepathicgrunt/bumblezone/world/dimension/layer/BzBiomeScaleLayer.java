package com.telepathicgrunt.bumblezone.world.dimension.layer;

import com.telepathicgrunt.bumblezone.world.dimension.layer.vanilla.CastleTransformer;
import com.telepathicgrunt.bumblezone.world.dimension.layer.vanilla.Context;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;


public class BzBiomeScaleLayer implements CastleTransformer {

    private final ResourceLocation biomeToExpand;
    private final Registry<Biome> biomeRegistry;

    public BzBiomeScaleLayer(ResourceLocation biomeToExpand, Registry<Biome> biomeRegistry) {
        this.biomeToExpand = biomeToExpand;
        this.biomeRegistry = biomeRegistry;
    }

    @Override
    public int apply(Context context, int n, int e, int s, int w, int center) {
        int hivePillarId = biomeRegistry.getId(biomeRegistry.get(this.biomeToExpand));

        if(center != hivePillarId) {
            boolean borderingHivePillar = false;

            if((n == hivePillarId || e == hivePillarId) || (w == hivePillarId || s == hivePillarId)) {
                borderingHivePillar = true;
            }

            if (borderingHivePillar) {
                return hivePillarId;
            }
        }
        return center;
    }
}