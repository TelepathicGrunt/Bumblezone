package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.configs.BzConfig;
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

public class GoodallCompat {
    private static Item BOTTLED_BEE;

    public static void setupCompat() {
        BOTTLED_BEE = Registry.ITEM.get(new ResourceLocation("goodall", "bottled_bee"));

        if (BOTTLED_BEE != Items.AIR && BzConfig.allowGoodallBottledBeesRevivingEmptyBroodBlock) {
            GoodallBottledBeeDispenseBehavior.DEFAULT_BOTTLED_BEE_DISPENSE_BEHAVIOR = ((DispenserBlockInvoker) Blocks.DISPENSER).invokeGetDispenseMethod(new ItemStack(BOTTLED_BEE));
            DispenserBlock.registerBehavior(BOTTLED_BEE, new GoodallBottledBeeDispenseBehavior()); // adds compatibility with bottled bee in dispensers
        }

       // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.goodallPresent = true;
    }

    public static boolean isBottledBeesItem(ItemStack itemStack) {
        return itemStack.is(BOTTLED_BEE);
    }

    public static InteractionResult bottledBeeInteract(ItemStack itemstack, Player playerEntity, InteractionHand playerHand) {
        if (isBottledBeesItem(itemstack)) {
            if (!playerEntity.isCrouching()) {
                if (!playerEntity.isCreative()) {
                    playerEntity.setItemInHand(playerHand, new ItemStack(Items.GLASS_BOTTLE)); //replaced potion of bee with glass bottle
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.FAIL;
    }
}
