package com.telepathicgrunt.bumblezone.world.dimension.layer;

import com.telepathicgrunt.bumblezone.world.dimension.BzBiomeProvider;
import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.CastleTransformer;


public enum BzBiomePollinatedPillarLayer implements CastleTransformer {
    INSTANCE;

    public int apply(Context context, int n, int e, int s, int w, int center) {

        if (context.nextRandom(150) == 0 && n == center && e == center && s == center && w == center) {
            return BzBiomeProvider.LAYERS_BIOME_REGISTRY.getId(
                    BzBiomeProvider.LAYERS_BIOME_REGISTRY.get(BzBiomeProvider.POLLINATED_PILLAR));
        }

        return center;
    }

}