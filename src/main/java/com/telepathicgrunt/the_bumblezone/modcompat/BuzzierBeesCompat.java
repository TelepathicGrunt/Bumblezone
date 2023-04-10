package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.DispenserBlockInvoker;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.registries.ForgeRegistries;

public class BuzzierBeesCompat {
	private static final ResourceLocation BEE_BOTTLE_RL = new ResourceLocation("buzzier_bees", "bee_bottle");

	public static void setupCompat() {
		Item BOTTLED_BEES = ForgeRegistries.ITEMS.getValue(BEE_BOTTLE_RL);

		if (BOTTLED_BEES != null && BzModCompatibilityConfigs.allowBeeBottleRevivingEmptyBroodBlock.get()) {
			BuzzierBeesBottledBeeDispenseBehavior.DEFAULT_BOTTLED_BEE_DISPENSE_BEHAVIOR = ((DispenserBlockInvoker) Blocks.DISPENSER).invokeGetDispenseMethod(new ItemStack(BOTTLED_BEES));
			DispenserBlock.registerBehavior(BOTTLED_BEES, new BuzzierBeesBottledBeeDispenseBehavior()); // adds compatibility with bottled bee in dispensers
		}

		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.buzzierBeesPresent = true;
	}

	public static InteractionResult bottledBeeInteract(ItemStack itemstack, Player playerEntity, InteractionHand playerHand) {
		if (Registry.ITEM.getKey(itemstack.getItem()).equals(BEE_BOTTLE_RL)) {
			if (!playerEntity.isCrouching()) {
				if (!playerEntity.isCreative()) {
					playerEntity.setItemInHand(playerHand, new ItemStack(Items.GLASS_BOTTLE)); //replaced bottled bee with glass bottle
				}

				return itemstack.hasTag() && itemstack.getOrCreateTag().getInt("Age") < 0 ? InteractionResult.CONSUME_PARTIAL : InteractionResult.SUCCESS;
			}
		}

		return InteractionResult.FAIL;
	}
}
