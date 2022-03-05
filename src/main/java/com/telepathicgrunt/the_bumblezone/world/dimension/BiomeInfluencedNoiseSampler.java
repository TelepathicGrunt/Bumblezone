package com.telepathicgrunt.the_bumblezone.world.dimension;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzBiomeHeightRegistry;
import it.unimi.dsi.fastutil.longs.Long2FloatMap;
import it.unimi.dsi.fastutil.longs.Long2FloatOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import org.apache.logging.log4j.Level;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class BiomeInfluencedNoiseSampler {

    /**
     * Table of weights used to weight faraway biomes less than nearby biomes.
     */
    private static final int RADIUS = 2;
    private static final float[] BIOME_WEIGHT_TABLE = Util.make(new float[(int) Math.pow((RADIUS * 2) + 1, 2)], (array) -> {
        for(int x = -RADIUS; x <= RADIUS; ++x) {
            for(int z = -RADIUS; z <= RADIUS; ++z) {
                float weight = 10.0F / Mth.sqrt((float)(x * x + z * z) + 0.2F);
                array[x + RADIUS + (z + RADIUS) * ((RADIUS * 2) + 1)] = weight / 22f;
            }
        }
    });
    private static final Map<Long, Float> CACHED_INFLUENCE_RESULT = new ConcurrentHashMap<>();

    public static double calculateBaseNoise(int x, int z, Climate.Sampler sampler, BiomeSource biomeSource, Registry<Biome> biomeRegistry) {
        long longPos = (long)x & 0xFFFFFFFFL | ((long)z & 0xFFFFFFFFL) << 32;
        Float cachedResult = CACHED_INFLUENCE_RESULT.get(longPos);
        if (cachedResult != null) {
            return cachedResult;
        }

        BzBiomeHeightRegistry.BiomeTerrain centerBiomeInfo = BzBiomeHeightRegistry.BIOME_HEIGHT_REGISTRY.getOptional(biomeRegistry.getKey(
                biomeSource.getNoiseBiome(x >> 2, 40, z >> 2, sampler).value())).orElse(new BzBiomeHeightRegistry.BiomeTerrain(4, 1));

        float totalHeight = 0.0F;
        for(int xOffset = -RADIUS; xOffset <= RADIUS; ++xOffset) {
            for(int zOffset = -RADIUS; zOffset <= RADIUS; ++zOffset) {
                BzBiomeHeightRegistry.BiomeTerrain biomeTerrain = BzBiomeHeightRegistry.BIOME_HEIGHT_REGISTRY.getOptional(biomeRegistry.getKey(
                        biomeSource.getNoiseBiome((x >> 2) + xOffset, 40, (z >> 2) + zOffset, sampler).value())).orElse(new BzBiomeHeightRegistry.BiomeTerrain(4, 1));
                float biomeDepth = biomeTerrain.depth;
                float weight = BIOME_WEIGHT_TABLE[xOffset + RADIUS + (zOffset + RADIUS) * ((RADIUS * 2) + 1)];
                if(biomeDepth != centerBiomeInfo.depth) {
                    biomeDepth = Mth.lerp(centerBiomeInfo.weightModifier, biomeDepth, centerBiomeInfo.depth);
                }

                totalHeight += (biomeDepth * weight);
            }
        }

        float finalInfluence = totalHeight / 126f;
        if (CACHED_INFLUENCE_RESULT.size() > 2000) {
            CACHED_INFLUENCE_RESULT.clear();
        }
        CACHED_INFLUENCE_RESULT.put(longPos, finalInfluence);
        return finalInfluence;
    }
}
