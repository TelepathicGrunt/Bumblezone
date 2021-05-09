package com.telepathicgrunt.the_bumblezone.world.dimension.layer;

import com.telepathicgrunt.the_bumblezone.world.dimension.BzBiomeProvider;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.PerlinNoiseGenerator;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;

import java.util.stream.IntStream;


public enum BzBiomeLayer implements IAreaTransformer0 {
    INSTANCE;

    private static PerlinNoiseGenerator perlinGen;
//	private double max = -100;
//	private double min = 100;


    public int applyPixel(INoiseRandom noise, int x, int z) {
        double perlinNoise = perlinGen.getValue((double) x * 0.1D, (double) z * 0.0001D, false);
//
//		max = Math.max(max, perlinNoise);
//		min = Math.min(min, perlinNoise);
//		Bumblezone.LOGGER.log(Level.INFO, "Max: " + max +", Min: "+min + ", perlin: "+perlinNoise);

        if (Math.abs(perlinNoise) % 0.1D < 0.07D) {
            return BzBiomeProvider.LAYERS_BIOME_REGISTRY.getId(
                    BzBiomeProvider.LAYERS_BIOME_REGISTRY.get(BzBiomeProvider.HIVE_WALL));
        }
        else {
            return BzBiomeProvider.LAYERS_BIOME_REGISTRY.getId(
                    BzBiomeProvider.LAYERS_BIOME_REGISTRY.get(BzBiomeProvider.SUGAR_WATER_FLOOR));
        }
    }


    public static void setSeed(long seed) {
        if (perlinGen == null) {
            SharedSeedRandom sharedseedrandom = new SharedSeedRandom(seed);
            perlinGen = new PerlinNoiseGenerator(sharedseedrandom, IntStream.rangeClosed(-1, 0));
        }
    }
}