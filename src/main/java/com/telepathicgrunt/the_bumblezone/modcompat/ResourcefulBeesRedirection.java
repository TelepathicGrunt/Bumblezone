package com.telepathicgrunt.the_bumblezone.modcompat;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

import java.util.List;
import java.util.Random;

/**
 * This class is used so java wont load ResourcefulBeesCompat class and crash if
 * the mod isn't on. This is because java will load classes if their method is
 * present even though it isn't called when going through some code. However,
 * java won't load classes referenced in the method ahead of time so the
 * redirection works to keep ResourcefulBeesCompat unloaded by "nesting" the method
 * dependent on the mod.
 */
public class ResourcefulBeesRedirection {
	public static boolean RBIsApairyBlock(BlockState blockState){ return ResourcefulBeesCompat.RBIsApairyBlock(blockState); }

	public static void RBAddWorldgen(List<Biome> bumblezone_biomes){
		ResourcefulBeesCompat.RBAddWorldgen(bumblezone_biomes);
	}

	public static BlockState getRBBeesWaxBlock(){
		return ResourcefulBeesCompat.getRBBeesWaxBlock();
	}

	public static BlockState getRBHoneyBlock(Random random){
		return ResourcefulBeesCompat.getRBHoneyBlock(random);
	}

	public static BlockState RBGetSpiderHoneycomb(Random random){
		return ResourcefulBeesCompat.RBGetSpiderHoneycomb(random);
	}

	public static BlockState RBGetRandomHoneycomb(Random random, int lowerBoundBias){
		return ResourcefulBeesCompat.RBGetRandomHoneycomb(random, lowerBoundBias);
	}

	public static void RBMobSpawnEvent(LivingSpawnEvent.CheckSpawn event, boolean isChild) {
		ResourcefulBeesCompat.RBMobSpawnEvent(event, isChild);
	}
}
