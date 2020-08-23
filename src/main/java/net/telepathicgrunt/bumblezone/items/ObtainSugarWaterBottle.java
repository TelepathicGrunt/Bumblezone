package net.telepathicgrunt.bumblezone.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.fluids.SugarWaterFluid;

public class ObtainSugarWaterBottle {

    public static boolean useBottleOnSugarWater(World world, PlayerEntity playerEntity, Hand playerHand, BlockPos blockPos) {
        if (world.getFluidState(blockPos).getFluid() instanceof SugarWaterFluid) {
            ItemStack itemstack = playerEntity.getStackInHand(playerHand);

            if (itemstack.getItem() == Items.GLASS_BOTTLE) {
                world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);

                if(!playerEntity.isCreative())
                    itemstack.decrement(1); // remove current honey bottle

                if (itemstack.isEmpty()) {
                    playerEntity.setStackInHand(playerHand, new ItemStack(BzItems.SUGAR_WATER_BOTTLE)); // places sugar water bottle in hand
                } else if (!playerEntity.inventory.insertStack(new ItemStack(BzItems.SUGAR_WATER_BOTTLE))) // places sugar water bottle in inventory
                {
                    playerEntity.dropItem(new ItemStack(BzItems.SUGAR_WATER_BOTTLE), false); // drops sugar water bottle if inventory is full
                }

                return true;
            }
        }

        return false;
    }
}
