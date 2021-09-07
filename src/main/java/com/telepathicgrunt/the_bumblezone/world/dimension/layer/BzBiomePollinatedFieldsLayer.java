package com.telepathicgrunt.the_bumblezone.world.dimension.layer;

import com.telepathicgrunt.the_bumblezone.world.dimension.BzBiomeProvider;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;


public enum BzBiomePollinatedFieldsLayer implements ICastleTransformer {
    INSTANCE;

    public int apply(INoiseRandom context, int n, int e, int s, int w, int center) {
        int hivePillarId = BzBiomeProvider.LAYERS_BIOME_REGISTRY.getId(
                BzBiomeProvider.LAYERS_BIOME_REGISTRY.get(BzBiomeProvider.POLLINATED_PILLAR));
        int pollinatedFields = BzBiomeProvider.LAYERS_BIOME_REGISTRY.getId(
                BzBiomeProvider.LAYERS_BIOME_REGISTRY.get(BzBiomeProvider.POLLINATED_FIELDS));

        if(center != hivePillarId){
            boolean borderingHivePillar = false;

            if((n == hivePillarId || e == hivePillarId) ||
                (w == hivePillarId || s == hivePillarId)) {
                borderingHivePillar = true;
            }

            if (borderingHivePillar) {
                return pollinatedFields;
            }
        }
        return center;
    }
}