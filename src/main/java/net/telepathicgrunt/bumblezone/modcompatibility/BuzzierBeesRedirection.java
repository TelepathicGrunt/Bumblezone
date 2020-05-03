package net.telepathicgrunt.bumblezone.modcompatibility;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

/**
 * This class is used so java wont load BuzzierBeesCompat class and crash
 * if the mod isn't on. This is because java will load classes if their method
 * is present even though it isn't called when going through some code. However,
 * java won't load classes referenced in the method ahead of time so the redirection
 * works to keep BuzzierBeesCompat unloaded by "nesting" the method dependant on the mod.
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
	
	public static ActionResultType honeyWandTakingHoney(ItemStack itemstack, BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand) {
		return BuzzierBeesCompat.honeyWandTakingHoney(itemstack, thisBlockState, world, position, playerEntity, playerHand);
	}
	
	public static ActionResultType honeyWandGivingHoney(ItemStack itemstack, BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand) {
		return BuzzierBeesCompat.honeyWandGivingHoney(itemstack, thisBlockState, world, position, playerEntity, playerHand);
	}
	
	public static ActionResultType bottledBeeInteract(ItemStack itemstack, BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand) {
		return BuzzierBeesCompat.bottledBeeInteract(itemstack, thisBlockState, world, position, playerEntity, playerHand);
	}
	
	public static Block BBGetRandomTier1Candle(Random random)
	{
		return BuzzierBeesCompat.BBGetRandomTier1Candle(random);
	}
	
	public static Block BBGetRandomTier2Candle(Random random, int lowerEndBias)
	{
		return BuzzierBeesCompat.BBGetRandomTier2Candle(random, lowerEndBias);
	}
	
	public static Block BBGetRandomTier3Candle(Random random, int lowerEndBias)
	{
		return BuzzierBeesCompat.BBGetRandomTier3Candle(random, lowerEndBias);
	}
}
