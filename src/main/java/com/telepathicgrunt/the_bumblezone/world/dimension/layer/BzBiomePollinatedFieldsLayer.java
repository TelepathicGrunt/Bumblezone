package com.telepathicgrunt.the_bumblezone.world.dimension.layer;

import com.telepathicgrunt.the_bumblezone.world.dimension.BiomeRegistryHolder;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzBiomeProvider;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.CastleTransformer;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.Context;


public record BzBiomePollinatedFieldsLayer() implements CastleTransformer {

    public int apply(Context context, int n, int e, int s, int w, int center) {
        int hivePillarId = BiomeRegistryHolder.convertToID(BzBiomeProvider.POLLINATED_PILLAR);
        int pollinatedFields = BiomeRegistryHolder.convertToID(BzBiomeProvider.POLLINATED_FIELDS);

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