package com.telepathicgrunt.the_bumblezone.worldgen.dimension.layer;

import com.telepathicgrunt.the_bumblezone.worldgen.dimension.BiomeRegistryHolder;
import com.telepathicgrunt.the_bumblezone.worldgen.dimension.BzBiomeSource;
import com.telepathicgrunt.the_bumblezone.worldgen.dimension.layer.vanilla.CastleTransformer;
import com.telepathicgrunt.the_bumblezone.worldgen.dimension.layer.vanilla.Context;


public record BzBiomePollinatedPillarLayer() implements CastleTransformer {

    public int apply(Context context, int n, int e, int s, int w, int center) {

        if (context.nextRandom(150) == 0 && n == center && e == center && s == center && w == center) {
            return BiomeRegistryHolder.convertToID(BzBiomeSource.POLLINATED_PILLAR);
        }

        return center;
    }
}