package com.telepathicgrunt.the_bumblezone.worldgen.dimension.layer;

import com.telepathicgrunt.the_bumblezone.worldgen.dimension.BiomeRegistryHolder;
import com.telepathicgrunt.the_bumblezone.worldgen.dimension.BzBiomeSource;
import com.telepathicgrunt.the_bumblezone.worldgen.dimension.layer.vanilla.CastleTransformer;
import com.telepathicgrunt.the_bumblezone.worldgen.dimension.layer.vanilla.Context;


public record BzBiomePillarLayer() implements CastleTransformer {

    @Override
    public int apply(Context context, int n, int e, int s, int w, int c) {

        if (context.nextRandom(12) == 0 && n == c && e == c && s == c && w == c) {
            return BiomeRegistryHolder.convertToID(BzBiomeSource.HIVE_PILLAR);
        }

        return c;
    }
}