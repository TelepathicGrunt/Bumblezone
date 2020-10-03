package com.telepathicgrunt.the_bumblezone.modCompat;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;

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
	public static void PBAddHoneycombs(BiomeLoadingEvent event){
		ProductiveBeesCompat.PBAddHoneycombs(event);
	}

	public static boolean PBIsAdvancedBeehiveAbstractBlock(BlockState block) {
		return ProductiveBeesCompat.PBIsAdvancedBeehiveAbstractBlock(block);
	}

	public static Pair<BlockState, String> PBGetRottenedHoneycomb(Random random) {
		return ProductiveBeesCompat.PBGetRottenedHoneycomb(random);
	}

	public static void PBMobSpawnEvent(LivingSpawnEvent.CheckSpawn event) {
		ProductiveBeesCompat.PBMobSpawnEvent(event);
	}

	public static Pair<BlockState, String> PBGetRandomHoneycomb(Random random, int lowerBoundBias) {
		return ProductiveBeesCompat.PBGetRandomHoneycomb(random, lowerBoundBias);
	}
}
