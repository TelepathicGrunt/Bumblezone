package com.telepathicgrunt.the_bumblezone.worldgen.dimension.layer;

import com.telepathicgrunt.the_bumblezone.worldgen.dimension.BiomeRegistryHolder;
import com.telepathicgrunt.the_bumblezone.worldgen.dimension.BzBiomeSource;
import com.telepathicgrunt.the_bumblezone.worldgen.dimension.layer.vanilla.CastleTransformer;
import com.telepathicgrunt.the_bumblezone.worldgen.dimension.layer.vanilla.Context;


public record BzBiomePollinatedFieldsLayer() implements CastleTransformer {

    public int apply(Context context, int n, int e, int s, int w, int center) {
        int hivePillarId = BiomeRegistryHolder.convertToID(BzBiomeSource.POLLINATED_PILLAR);
        int pollinatedFields = BiomeRegistryHolder.convertToID(BzBiomeSource.POLLINATED_FIELDS);

        if (center != hivePillarId) {
            boolean borderingHivePillar = false;

            if ((n == hivePillarId || e == hivePillarId) || (w == hivePillarId || s == hivePillarId)) {
                borderingHivePillar = true;
            }

            if (borderingHivePillar) {
                return pollinatedFields;
            }
        }
        return center;
    }
}