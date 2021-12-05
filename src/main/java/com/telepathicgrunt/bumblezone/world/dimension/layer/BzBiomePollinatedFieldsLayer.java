package com.telepathicgrunt.bumblezone.world.dimension.layer;

import com.telepathicgrunt.bumblezone.world.dimension.BzBiomeProvider;
import com.telepathicgrunt.bumblezone.world.dimension.layer.vanilla.CastleTransformer;
import com.telepathicgrunt.bumblezone.world.dimension.layer.vanilla.Context;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;


public record BzBiomePollinatedFieldsLayer(Registry<Biome> biomeRegistry) implements CastleTransformer {

    public int apply(Context context, int n, int e, int s, int w, int center) {
        int hivePillarId = biomeRegistry.getId(biomeRegistry.get(BzBiomeProvider.POLLINATED_PILLAR));
        int pollinatedFields = biomeRegistry.getId(biomeRegistry.get(BzBiomeProvider.POLLINATED_FIELDS));

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