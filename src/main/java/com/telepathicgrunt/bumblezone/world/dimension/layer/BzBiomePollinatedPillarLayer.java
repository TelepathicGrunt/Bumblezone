package com.telepathicgrunt.bumblezone.world.dimension.layer;

import com.telepathicgrunt.bumblezone.world.dimension.BzBiomeProvider;
import net.minecraft.world.biome.layer.type.CrossSamplingLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;


public enum BzBiomePollinatedPillarLayer implements CrossSamplingLayer {
    INSTANCE;

    public int sample(LayerRandomnessSource context, int n, int e, int s, int w, int center) {

        if (context.nextInt(150) == 0 && n == center && e == center && s == center && w == center) {
            return BzBiomeProvider.LAYERS_BIOME_REGISTRY.getRawId(
                    BzBiomeProvider.LAYERS_BIOME_REGISTRY.get(BzBiomeProvider.POLLINATED_PILLAR));
        }

        return center;
    }

}