package com.telepathicgrunt.bumblezone.world.dimension.layer;

import com.telepathicgrunt.bumblezone.world.dimension.layer.vanilla.CastleTransformer;
import com.telepathicgrunt.bumblezone.world.dimension.layer.vanilla.Context;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;


public record BzBiomeScaleLayer(ResourceLocation biomeToExpand, Registry<Biome> biomeRegistry) implements CastleTransformer {

    @Override
    public int apply(Context context, int n, int e, int s, int w, int center) {
        int hivePillarId = biomeRegistry.getId(biomeRegistry.get(this.biomeToExpand));

        if (center != hivePillarId) {
            boolean borderingHivePillar = false;

            if ((n == hivePillarId || e == hivePillarId) || (w == hivePillarId || s == hivePillarId)) {
                borderingHivePillar = true;
            }

            if (borderingHivePillar) {
                return hivePillarId;
            }
        }
        return center;
    }
}