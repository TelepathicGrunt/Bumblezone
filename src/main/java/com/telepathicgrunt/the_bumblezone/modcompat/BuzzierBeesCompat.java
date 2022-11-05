package com.telepathicgrunt.the_bumblezone.modcompat;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BuzzierBeesCompat {
	private static final ResourceLocation BEE_BOTTLE_RL = new ResourceLocation("buzzier_bees", "bee_bottle");

	public static void setupBuzzierBees() {
		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.buzzierBeesPresent = true;
	}

	public static InteractionResult bottledBeeInteract(ItemStack itemstack, Player playerEntity, InteractionHand playerHand) {
		if (Registry.ITEM.getKey(itemstack.getItem()).equals(BEE_BOTTLE_RL)) {
			if (!playerEntity.isCrouching()) {
				if (!playerEntity.isCreative()) {
					playerEntity.setItemInHand(playerHand, new ItemStack(Items.GLASS_BOTTLE)); //replaced bottled bee with glass bottle
				}

				return InteractionResult.SUCCESS;
			}
		}

		return InteractionResult.FAIL;
	}
}
