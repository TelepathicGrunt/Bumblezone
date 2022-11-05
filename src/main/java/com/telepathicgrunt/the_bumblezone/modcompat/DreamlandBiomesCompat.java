package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationHookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;

public class DreamlandBiomesCompat {

	private static final ResourceLocation BUMBLE_BEAST_RL = new ResourceLocation("dreamland", "bumble_beast");

	public static void setupDreamlandBiomes() {
		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.dreamlandBiomesPresent = true;
	}

	public static boolean runTeleportCodeIfBumbleBeastHitHigh(HitResult hitResult, Projectile pearlEntity) {
		Level world = pearlEntity.level; // world we threw in

		if (!BzModCompatibilityConfigs.allowEnderpearledBumbleBeastTeleporation.get()) {
			return false;
		}

		return EntityTeleportationHookup.attemptEntityBasedTeleportation(
				hitResult,
				pearlEntity,
				world,
				(entityHitResult) -> Registry.ENTITY_TYPE.getKey(entityHitResult.getEntity().getType()).equals(BUMBLE_BEAST_RL),
				(entityHitResult, hitPos) -> {
					AABB boundBox = entityHitResult.getEntity().getBoundingBox();
					double minYThreshold = ((boundBox.maxY - boundBox.minY) * 0.55d) + boundBox.minY;
					return hitPos.y() < minYThreshold;
				}
		);
	}
}
