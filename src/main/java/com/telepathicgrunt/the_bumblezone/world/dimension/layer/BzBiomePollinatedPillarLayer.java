package com.telepathicgrunt.the_bumblezone.world.dimension.layer;

import com.telepathicgrunt.the_bumblezone.world.dimension.BzBiomeProvider;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;


public enum BzBiomePollinatedPillarLayer implements ICastleTransformer {
    INSTANCE;

    public int apply(INoiseRandom context, int n, int e, int s, int w, int center) {

        if (context.nextRandom(100) == 0 && n == center && e == center && s == center && w == center) {
            return BzBiomeProvider.LAYERS_BIOME_REGISTRY.getId(
                    BzBiomeProvider.LAYERS_BIOME_REGISTRY.get(BzBiomeProvider.POLLINATED_PILLAR));
        }

        return center;
    }

}