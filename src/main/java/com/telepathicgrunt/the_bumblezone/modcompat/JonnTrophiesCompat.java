package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class JonnTrophiesCompat {
	private static ResourceLocation TROPHY_RL = new ResourceLocation("trophymanager", "trophy");

	public static void setupCompat() {

		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.jonnTrophiesPresent = true;
	}

	public static boolean isTrophy(ItemStack itemStack) {
		return Registry.ITEM.getKey(itemStack.getItem()).equals(TROPHY_RL);
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
		return BeeAggression.isBeelikeEntityType(Registry.ENTITY_TYPE.get(entityRL));
	}
}
