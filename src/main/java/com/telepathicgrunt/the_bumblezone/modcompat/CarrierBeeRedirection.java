package com.telepathicgrunt.the_bumblezone.modcompat;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.BlockPos;

/**
 * This class is used so java wont load CarrierBeeCompat class and crash if
 * the mod isn't on. This is because java will load classes if their method is
 * present even though it isn't called when going through some code. However,
 * java won't load classes referenced in the method ahead of time so the
 * redirection works to keep CarrierBeeCompat unloaded by "nesting" the method
 * dependent on the mod.
 */
public class CarrierBeeRedirection {
	public static void CBMobSpawn(LivingEntity entity, BlockPos spawnBlockPos) {
		CarrierBeesCompat.CBMobSpawn(entity, spawnBlockPos);
	}

	public static Class<? extends MobEntity> CBGetAppleBeeClass() {
		return CarrierBeesCompat.CBGetAppleBeeClass();
	}
}
