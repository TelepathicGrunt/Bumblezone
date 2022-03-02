package com.telepathicgrunt.the_bumblezone.world.dimension;

import com.telepathicgrunt.the_bumblezone.modinit.BzBiomeHeightRegistry;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

public final class BiomeInfluencedNoiseSampler {

    /**
     * Table of weights used to weight faraway biomes less than nearby biomes.
     */
    private static final int RADIUS = 2;
    private static final float[] BIOME_WEIGHT_TABLE = Util.make(new float[(int) Math.pow((RADIUS * 2) + 1, 2)], (array) -> {
        for(int x = -RADIUS; x <= RADIUS; ++x) {
            for(int z = -RADIUS; z <= RADIUS; ++z) {
                float weight = 10.0F / Mth.sqrt((float)(x * x + z * z) + 0.2F);
                array[x + RADIUS + (z + RADIUS) * 5] = weight / 22f;
            }
        }
    });

    public static double calculateBaseNoise(int x, int y, int z, Climate.Sampler sampler, BiomeSource biomeSource, Registry<Biome> biomeRegistry) {
        BzBiomeHeightRegistry.BiomeTerrain centerBiomeInfo = BzBiomeHeightRegistry.BIOME_HEIGHT_REGISTRY.getOptional(biomeRegistry.getKey(
                biomeSource.getNoiseBiome(x >> 2, 40, z >> 2, sampler).value())).orElse(new BzBiomeHeightRegistry.BiomeTerrain(4, 1));

        float totalHeight = 0.0F;
        for(int xOffset = -2; xOffset <= 2; ++xOffset) {
            for(int zOffset = -2; zOffset <= 2; ++zOffset) {
                BzBiomeHeightRegistry.BiomeTerrain biomeTerrain = BzBiomeHeightRegistry.BIOME_HEIGHT_REGISTRY.getOptional(biomeRegistry.getKey(
                        biomeSource.getNoiseBiome((x >> 2) + xOffset, 40, (z >> 2) + zOffset, sampler).value())).orElse(new BzBiomeHeightRegistry.BiomeTerrain(4, 1));
                float biomeDepth = biomeTerrain.depth;
                float weight = BIOME_WEIGHT_TABLE[xOffset + 2 + (zOffset + 2) * 5];
                if(biomeDepth != centerBiomeInfo.depth) {
                    biomeDepth = Mth.lerp(centerBiomeInfo.weightModifier, biomeDepth, centerBiomeInfo.depth);
                }

                totalHeight += (biomeDepth * weight);
            }
        }

        return (totalHeight / 400f) * (y > 126 ? 1 : -1);
    }
}
