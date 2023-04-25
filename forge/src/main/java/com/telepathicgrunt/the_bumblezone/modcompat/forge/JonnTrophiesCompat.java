package com.telepathicgrunt.the_bumblezone.modcompat.forge;

import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;

public class JonnTrophiesCompat implements ModCompat {
	private static final ResourceLocation TROPHY_RL = new ResourceLocation("trophymanager", "trophy");

	public JonnTrophiesCompat() {
		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.jonnTrophiesPresent = true;
	}

	@Override
	public EnumSet<Type> compatTypes() {
		return EnumSet.of(Type.BEE_GEAR_BOOSTING);
	}

	@Override
	public boolean isItemExplicitlyDisallowedFromBeeGearBoosting(ItemStack itemStack) {
		if (JonnTrophiesCompat.isTrophy(itemStack)) {
			return !JonnTrophiesCompat.isTrophyBeeThemed(itemStack);
		}

		return false;
	}

	public static boolean isTrophy(ItemStack itemStack) {
		return BuiltInRegistries.ITEM.getKey(itemStack.getItem()).equals(TROPHY_RL);
	}

	public static boolean isTrophyBeeThemed(ItemStack itemStack) {
		if (!itemStack.hasTag()) {
			return false;
		}

		CompoundTag trophyEntityCompound = itemStack.getOrCreateTag().getCompound("TrophyEntity");
		String entityType = trophyEntityCompound.getString("entityType");

		if (entityType.isEmpty() || !ResourceLocation.isValidResourceLocation(entityType)) {
			return false;
		}

		ResourceLocation entityRL = new ResourceLocation(entityType);
		return BeeAggression.isBeelikeEntityType(BuiltInRegistries.ENTITY_TYPE.get(entityRL));
	}
}
