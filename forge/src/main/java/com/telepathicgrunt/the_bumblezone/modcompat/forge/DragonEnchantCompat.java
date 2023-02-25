
package com.telepathicgrunt.the_bumblezone.modcompat.forge;

import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationHookup;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.EnumSet;

public class DragonEnchantCompat implements ModCompat {
	private static final String END_STEP_ENCHANT_ATTACHED_TAG = "dragon_enchants:end_step";
	private static final ResourceLocation END_STEP_RL = new ResourceLocation("dragon_enchants", "end_step");

	public DragonEnchantCompat() {
		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.dragonEnchantPresent = true;
	}

	@Override
	public EnumSet<Type> compatTypes() {
		return EnumSet.of(Type.PROJECTILE_IMPACT_HANDLED);
	}

	public boolean isTeleportHandled(HitResult hitResult, Entity owner, Projectile projectile) {
		if (hitResult instanceof BlockHitResult blockHitResult &&
			projectile != null &&
			projectile.getPersistentData().getBoolean(END_STEP_ENCHANT_ATTACHED_TAG) &&
			GeneralUtils.isInTag(BuiltInRegistries.ENCHANTMENT, BzTags.ENCHANT_SPECIAL_DEDICATED_COMPAT, BuiltInRegistries.ENCHANTMENT.get(END_STEP_RL)))
		{
			return EntityTeleportationHookup.runTeleportProjectileImpact(blockHitResult, owner, projectile);
		}
		return false;
	}
}
