package net.telepathicgrunt.bumblezone.modcompatibility;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

/**
 * This class is used so java wont load BuzzierBeesCompat class and crash
 * if the mod isn't on as java will load classes if their method is present
 * even though it isn't called when going through a method but it only loads
 * so many classes deep ahead of time so the redirection works to keep 
 * BuzzierBeesCompat unloaded.
 */
public class BuzzierBeesRedirection
{
	public static void BBMobSpawnEvent(LivingSpawnEvent.CheckSpawn event)
	{
		BuzzierBeesCompat.BBMobSpawnEvent(event);
	}
	
	public static void buildSurface(Random random, IChunk chunk, Biome biome, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config)
	{
		BuzzierBeesCompat.buildSurface(random, chunk, biome, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, config);
	}
	
	public static void makeBedrock(IChunk chunk, Random random) {
		BuzzierBeesCompat.makeBedrock(chunk, random);
	}
}
