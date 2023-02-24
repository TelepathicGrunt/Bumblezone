package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationHookup;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;

public class TwilightForestCompat {
	private static final String ENDER_BOW_ATTACHED_TAG = "twilightforest:ender";
	private static final ResourceLocation ENDER_BOW_RL = new ResourceLocation("twilightforest", "ender_bow");

	public static void setupCompat() {
		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.twilightForestPresent = true;
	}

	public static boolean isTeleportHandled(EntityHitResult entityHitResult, Projectile projectile) {
		if (projectile != null &&
			projectile.getExtraCustomData().getBoolean(ENDER_BOW_ATTACHED_TAG) &&
			Registry.ITEM.get(ENDER_BOW_RL).getDefaultInstance().is(BzTags.ITEM_SPECIAL_DEDICATED_COMPAT))
		{
			return EntityTeleportationHookup.runEntityHitCheck(entityHitResult, projectile);
		}
		return false;
	}
}
