package com.telepathicgrunt.the_bumblezone.world.dimension.layer;

import com.telepathicgrunt.the_bumblezone.world.dimension.BiomeRegistryHolder;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzBiomeProvider;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.CastleTransformer;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.Context;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;


public record BzBiomePillarLayer() implements CastleTransformer {

    @Override
    public int apply(Context context, int n, int e, int s, int w, int c) {

        if (context.nextRandom(12) == 0 && n == c && e == c && s == c && w == c) {
            return BiomeRegistryHolder.convertToID(BzBiomeProvider.HIVE_PILLAR);
        }

        return c;
    }
}