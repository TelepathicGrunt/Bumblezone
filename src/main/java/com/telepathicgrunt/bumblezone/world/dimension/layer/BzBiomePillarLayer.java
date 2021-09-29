package com.telepathicgrunt.bumblezone.world.dimension.layer;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.world.dimension.BzBiomeProvider;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.layer.type.CrossSamplingLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;


public enum BzBiomePillarLayer implements CrossSamplingLayer {
    INSTANCE;

    private static final Identifier HIVE_PILLAR = new Identifier(Bumblezone.MODID, "hive_pillar");

    @Override
    public int sample(LayerRandomnessSource context, int n, int e, int s, int w, int center) {

        if (context.nextInt(12) == 0 && n == center && e == center && s == center && w == center) {
            return BzBiomeProvider.LAYERS_BIOME_REGISTRY.getRawId(BzBiomeProvider.LAYERS_BIOME_REGISTRY.get(HIVE_PILLAR));
        }

        return center;
    }
}