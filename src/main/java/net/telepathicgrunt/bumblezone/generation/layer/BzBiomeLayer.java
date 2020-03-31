package net.telepathicgrunt.bumblezone.generation.layer;

import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.PerlinNoiseGenerator;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;
import net.telepathicgrunt.bumblezone.biome.BzBiomes;


@SuppressWarnings("deprecation")
public enum BzBiomeLayer implements IAreaTransformer0
{
	INSTANCE;

	private static final int SUGAR_WATER = Registry.BIOME.getId(BzBiomes.SUGAR_WATER);
	private static final int HIVE_WALL = Registry.BIOME.getId(BzBiomes.HIVE_WALL);
	private static final int HIVE_PILLAR = Registry.BIOME.getId(BzBiomes.HIVE_PILLAR);

	private static PerlinNoiseGenerator perlinGen;
//	private double max = 0;
//	private double min = 1;
	

	@Override
	public int apply(INoiseRandom noise, int x, int z)
	{
		double perlinNoise = perlinGen.noiseAt(x * 0.1D, z * 0.00001D, false) * 0.5D + 0.5D;
		
//		max = Math.max(max, perlinNoise);
//		min = Math.min(min, perlinNoise);
//		Bumblezone.LOGGER.log(Level.DEBUG, "Max: " + max +", Min: "+min + ", perlin: "+perlinNoise);

		if(noise.random(5) == 0) 
		{
			return HIVE_PILLAR;
		}
		else if(Math.abs(perlinNoise) < 0.7)
		{
			return SUGAR_WATER;
		}
		else
		{
			return HIVE_WALL;
		}
	}


	public static void setSeed(long seed)
	{
		if (perlinGen == null)
		{
			SharedSeedRandom sharedseedrandom = new SharedSeedRandom(seed);
			perlinGen = new PerlinNoiseGenerator(sharedseedrandom, 0, 0);
		}
	}
}