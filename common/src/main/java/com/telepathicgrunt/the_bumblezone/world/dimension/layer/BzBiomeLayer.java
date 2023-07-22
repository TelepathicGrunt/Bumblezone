package com.telepathicgrunt.the_bumblezone.world.dimension.layer;

import com.google.common.collect.ImmutableList;
import com.telepathicgrunt.the_bumblezone.world.dimension.BiomeRegistryHolder;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzBiomeSource;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.AreaTransformer0;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.Context;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;


public class BzBiomeLayer implements AreaTransformer0 {

    private final PerlinSimplexNoise perlinGen;
//	private double max = -100;
//	private double min = 100;

    public BzBiomeLayer(long seed) {
        WorldgenRandom sharedseedrandom = new WorldgenRandom(new LegacyRandomSource(seed));
        perlinGen = new PerlinSimplexNoise(sharedseedrandom, ImmutableList.of(-1, 0));
    }

    @Override
    public int applyPixel(Context noise, int x, int z) {
        double perlinNoise = perlinGen.getValue((double) x * 0.1D, (double) z * 0.0001D, false);
        double perlinNoise2 = perlinGen.getValue((double) x * 0.02D, (double) z * 0.02D, false);
////
//		max = Math.max(max, perlinNoise);
//		min = Math.min(min, perlinNoise);
//		Bumblezone.LOGGER.log(Level.INFO, "Max: " + max +", Min: "+min + ", perlin: "+perlinNoise);

        if (Math.abs(perlinNoise) % 0.1D < 0.07D) {
            return BiomeRegistryHolder.convertToID(BzBiomeSource.HIVE_WALL);
        }
        else {
            if (Math.abs(perlinNoise2) > 0.55D) {
                return BiomeRegistryHolder.convertToID(BzBiomeSource.CRYSTAL_CANYON);
            }
            else {
                return BiomeRegistryHolder.convertToID(BzBiomeSource.SUGAR_WATER_FLOOR);
            }
        }
    }
}