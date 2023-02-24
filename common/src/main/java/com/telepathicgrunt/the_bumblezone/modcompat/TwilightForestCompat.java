package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationHookup;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import org.apache.commons.lang3.NotImplementedException;

public class TwilightForestCompat implements ModCompat {
	private static final String ENDER_BOW_ATTACHED_TAG = "twilightforest:ender";
	private static final ResourceLocation ENDER_BOW_RL = new ResourceLocation("twilightforest", "ender_bow");

	public TwilightForestCompat() {
		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.twilightForestPresent = true;
	}

	public static boolean isTeleportHandled(EntityHitResult entityHitResult, Entity owner, Projectile projectile) {
		if (projectile != null &&
			getPersistentData(projectile).getBoolean(ENDER_BOW_ATTACHED_TAG) &&
			BuiltInRegistries.ITEM.get(ENDER_BOW_RL).getDefaultInstance().is(BzTags.ITEM_SPECIAL_DEDICATED_COMPAT))
		{
			return EntityTeleportationHookup.runEntityHitCheck(entityHitResult, owner, projectile);
		}
		return false;
	}

	@ExpectPlatform
	public static CompoundTag getPersistentData(Entity entity) {
		throw new NotImplementedException("TwilightForestCompat getPesistentData is not implemented!");
	}
}
