package com.telepathicgrunt.bumblezone.world.dimension.layer;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.world.dimension.layer.vanilla.CastleTransformer;
import com.telepathicgrunt.bumblezone.world.dimension.layer.vanilla.Context;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;


public class BzBiomePillarLayer implements CastleTransformer {

    private static final ResourceLocation HIVE_PILLAR = new ResourceLocation(Bumblezone.MODID, "hive_pillar");
    private final Registry<Biome> biomeRegistry;

    public BzBiomePillarLayer(Registry<Biome> biomeRegistry) {
        this.biomeRegistry = biomeRegistry;
    }

    @Override
    public int apply(Context context, int n, int e, int s, int w, int c) {

        if (context.nextRandom(12) == 0 && n == c && e == c && s == c && w == c) {
            return biomeRegistry.getId(biomeRegistry.get(HIVE_PILLAR));
        }

        return c;
    }
}