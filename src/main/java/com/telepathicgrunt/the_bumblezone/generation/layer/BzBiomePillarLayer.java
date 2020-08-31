package com.telepathicgrunt.the_bumblezone.generation.layer;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.generation.BzBiomeProvider;


public enum BzBiomePillarLayer implements ICastleTransformer {
    INSTANCE;

    private static final ResourceLocation HIVE_PILLAR = new ResourceLocation(Bumblezone.MODID, "hive_pillar");
    public int apply(INoiseRandom context, int n, int e, int s, int w, int center) {

        if (context.random(12) == 0 && n == center && e == center && s == center && w == center) {
            return BzBiomeProvider.layersBiomeRegistry.getId(BzBiomeProvider.layersBiomeRegistry.getOrDefault(HIVE_PILLAR));
        }

        return center;
    }

}