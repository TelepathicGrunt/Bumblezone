package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationHookup;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Contract;

import java.util.EnumSet;

public class TwilightForestCompat implements ModCompat {
	private static final String ENDER_BOW_ATTACHED_TAG = "twilightforest:ender";
	private static final ResourceLocation ENDER_BOW_RL = new ResourceLocation("twilightforest", "ender_bow");

	public TwilightForestCompat() {
		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.twilightForestPresent = true;
	}

	public boolean isTeleportHandled(HitResult hitResult, Entity owner, Projectile projectile) {
		if (hitResult instanceof EntityHitResult entityHitResult &&
			projectile != null &&
			getPersistentData(projectile).getBoolean(ENDER_BOW_ATTACHED_TAG) &&
			GeneralUtils.isInTag(BuiltInRegistries.ITEM, BzTags.ITEM_SPECIAL_DEDICATED_COMPAT, BuiltInRegistries.ITEM.get(ENDER_BOW_RL)))
		{
			return EntityTeleportationHookup.runEntityHitCheck(entityHitResult, owner, projectile);
		}
		return false;
	}

	@Override
	public EnumSet<Type> compatTypes() {
		return EnumSet.of(Type.PROJECTILE_IMPACT_HANDLED);
	}

	@Contract
	@ExpectPlatform
	public static CompoundTag getPersistentData(Entity entity) {
		throw new NotImplementedException("TwilightForestCompat getPesistentData is not implemented!");
	}
}
