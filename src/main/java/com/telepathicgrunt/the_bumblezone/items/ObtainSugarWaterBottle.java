package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.fluids.SugarWaterFluid;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ObtainSugarWaterBottle {

    public static boolean useBottleOnSugarWater(World world, PlayerEntity playerEntity, Hand playerHand, BlockPos blockPos) {
        if (world.getFluidState(blockPos).getFluid() instanceof SugarWaterFluid) {
            ItemStack itemstack = playerEntity.getHeldItem(playerHand);

            if (itemstack.getItem() == Items.GLASS_BOTTLE) {
                world.playSound(playerEntity, playerEntity.getPosX(), playerEntity.getPosY(), playerEntity.getPosZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);

                if(!playerEntity.isCreative())
                    itemstack.shrink(1); // remove current honey bottle

                if (itemstack.isEmpty()) {
                    playerEntity.setHeldItem(playerHand, new ItemStack(BzItems.SUGAR_WATER_BOTTLE.get())); // places sugar water bottle in hand
                } else if (!playerEntity.inventory.addItemStackToInventory(new ItemStack(BzItems.SUGAR_WATER_BOTTLE.get()))) // places sugar water bottle in inventory
                {
                    playerEntity.dropItem(new ItemStack(BzItems.SUGAR_WATER_BOTTLE.get()), false); // drops sugar water bottle if inventory is full
                }

                return true;
            }
        }

        return false;
    }
}
