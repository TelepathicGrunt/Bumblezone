
package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationHookup;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;

public class DragonEnchantsCompat {
	private static final String END_STEP_ENCHANT_ATTACHED_TAG = "dragonenchants:end_step";
	private static final ResourceLocation END_STEP_RL = new ResourceLocation("dragonenchants", "end_step");

	public static void setupCompat() {
		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.dragonEnchantsPresent = true;
	}

	public static boolean isTeleportHandled(BlockHitResult blockHitResult, Entity owner, Projectile projectile) {
		if (projectile != null &&
			projectile.getPersistentData().getBoolean(END_STEP_ENCHANT_ATTACHED_TAG) &&
			ForgeRegistries.ENCHANTMENTS.getHolder(END_STEP_RL).get().is(BzTags.ENCHANT_SPECIAL_DEDICATED_COMPAT))
		{
			return EntityTeleportationHookup.runTeleportProjectileImpact(blockHitResult, owner, projectile);
		}
		return false;
	}
}
