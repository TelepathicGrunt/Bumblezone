package com.telepathicgrunt.the_bumblezone.modcompat;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

import java.util.List;
import java.util.Random;

/**
 * This class is used so java wont load ProductiveBeesCompat class and crash if
 * the mod isn't on. This is because java will load classes if their method is
 * present even though it isn't called when going through some code. However,
 * java won't load classes referenced in the method ahead of time so the
 * redirection works to keep ProductiveBeesCompat unloaded by "nesting" the method
 * dependent on the mod.
 */
public class ProductiveBeesRedirection {
	public static void PBAddWorldgen(List<Biome> bumblezone_biomes){
		ProductiveBeesCompat.PBAddWorldgen(bumblezone_biomes);
	}

	public static boolean PBIsExpandedBeehiveBlock(BlockState block) {
		return ProductiveBeesCompat.PBIsExpandedBeehiveBlock(block);
	}

	public static Pair<BlockState, String> PBGetRottenedHoneycomb(Random random) {
		return ProductiveBeesCompat.PBGetRottenedHoneycomb(random);
	}

	public static void PBMobSpawnEvent(LivingSpawnEvent.CheckSpawn event, boolean isChild) {
		ProductiveBeesCompat.PBMobSpawnEvent(event, isChild);
	}

	public static Pair<BlockState, String> PBGetRandomHoneycomb(Random random, int lowerBoundBias) {
		return ProductiveBeesCompat.PBGetRandomHoneycomb(random, lowerBoundBias);
	}
}
