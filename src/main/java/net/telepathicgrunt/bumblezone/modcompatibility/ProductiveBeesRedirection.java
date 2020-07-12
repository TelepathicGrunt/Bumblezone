package net.telepathicgrunt.bumblezone.modcompatibility;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

/**
 * This class is used so java wont load BeesourcefulCompat class and crash if
 * the mod isn't on. This is because java will load classes if their method is
 * present even though it isn't called when going through some code. However,
 * java won't load classes referenced in the method ahead of time so the
 * redirection works to keep BeesourcefulCompat unloaded by "nesting" the method
 * dependent on the mod.
 */
public class ProductiveBeesRedirection {
	public static boolean PBIsAdvancedBeehiveAbstractBlock(BlockState block) {
		return ProductiveBeesCompat.PBIsAdvancedBeehiveAbstractBlock(block);
	}

	public static Block PBGetRottenedHoneycomb(Random random) {
		return ProductiveBeesCompat.PBGetRottenedHoneycomb(random);
	}

	public static void PBMobSpawnEvent(LivingSpawnEvent.CheckSpawn event) {
		ProductiveBeesCompat.PBMobSpawnEvent(event);
	}

	public static boolean PBIsHoneyTreatItem(Item heldItem) {
		return ProductiveBeesCompat.PBIsHoneyTreatItem(heldItem);
	}

	public static Block PBGetRandomHoneycomb(Random random, int lowerBoundBias) {
		return ProductiveBeesCompat.PBGetRandomHoneycomb(random, lowerBoundBias);
	}
}
