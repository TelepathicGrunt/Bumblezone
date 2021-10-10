package com.telepathicgrunt.bumblezone.world.dimension.layer;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.world.dimension.BzBiomeProvider;
import java.util.stream.IntStream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer0;


public enum BzBiomeLayer implements AreaTransformer0 {
    INSTANCE;

    private static final ResourceLocation SUGAR_WATER = new ResourceLocation(Bumblezone.MODID, "sugar_water_floor");
    private static final ResourceLocation HIVE_WALL = new ResourceLocation(Bumblezone.MODID, "hive_wall");

    private static PerlinSimplexNoise perlinGen;
//	private double max = -100;
//	private double min = 100;

    @Override
    public int applyPixel(Context noise, int x, int z) {
        double perlinNoise = perlinGen.getValue((double) x * 0.1D, (double) z * 0.0001D, false);
//
//		max = Math.max(max, perlinNoise);
//		min = Math.min(min, perlinNoise);
//		Bumblezone.LOGGER.log(Level.INFO, "Max: " + max +", Min: "+min + ", perlin: "+perlinNoise);

        if (Math.abs(perlinNoise) % 0.1D < 0.07D) {
            return BzBiomeProvider.LAYERS_BIOME_REGISTRY.getId(BzBiomeProvider.LAYERS_BIOME_REGISTRY.get(HIVE_WALL));
        }
        else {
            return BzBiomeProvider.LAYERS_BIOME_REGISTRY.getId(BzBiomeProvider.LAYERS_BIOME_REGISTRY.get(SUGAR_WATER));
        }
    }


    public static void setSeed(long seed) {
        if (perlinGen == null) {
            WorldgenRandom sharedseedrandom = new WorldgenRandom(seed);
            perlinGen = new PerlinSimplexNoise(sharedseedrandom, IntStream.rangeClosed(-1, 0));
        }
    }
}