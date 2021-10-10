package com.telepathicgrunt.bumblezone.world.dimension.layer;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.world.dimension.BzBiomeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.CastleTransformer;


public enum BzBiomePillarLayer implements CastleTransformer {
    INSTANCE;

    private static final ResourceLocation HIVE_PILLAR = new ResourceLocation(Bumblezone.MODID, "hive_pillar");

    @Override
    public int apply(Context context, int n, int e, int s, int w, int center) {

        if (context.nextRandom(12) == 0 && n == center && e == center && s == center && w == center) {
            return BzBiomeProvider.LAYERS_BIOME_REGISTRY.getId(BzBiomeProvider.LAYERS_BIOME_REGISTRY.get(HIVE_PILLAR));
        }

        return center;
    }
}