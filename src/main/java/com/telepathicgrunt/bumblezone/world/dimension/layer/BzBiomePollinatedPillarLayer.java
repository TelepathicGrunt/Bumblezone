package com.telepathicgrunt.bumblezone.world.dimension.layer;

import com.telepathicgrunt.bumblezone.world.dimension.BzBiomeProvider;
import com.telepathicgrunt.bumblezone.world.dimension.layer.vanilla.CastleTransformer;
import com.telepathicgrunt.bumblezone.world.dimension.layer.vanilla.Context;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;


public class BzBiomePollinatedPillarLayer implements CastleTransformer {

    private final Registry<Biome> biomeRegistry;

    public BzBiomePollinatedPillarLayer(Registry<Biome> biomeRegistry) {
        this.biomeRegistry = biomeRegistry;
    }

    public int apply(Context context, int n, int e, int s, int w, int center) {

        if (context.nextRandom(150) == 0 && n == center && e == center && s == center && w == center) {
            return biomeRegistry.getId(biomeRegistry.get(BzBiomeProvider.POLLINATED_PILLAR));
        }

        return center;
    }

}