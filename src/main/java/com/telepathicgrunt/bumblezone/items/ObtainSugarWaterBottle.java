package com.telepathicgrunt.bumblezone.items;

import com.telepathicgrunt.bumblezone.fluids.SugarWaterFluid;
import com.telepathicgrunt.bumblezone.modinit.BzItems;
import com.telepathicgrunt.bumblezone.utils.GeneralUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ObtainSugarWaterBottle {

    public static boolean useBottleOnSugarWater(World world, PlayerEntity playerEntity, Hand playerHand, BlockPos blockPos) {
        if (world.getFluidState(blockPos).getFluid() instanceof SugarWaterFluid) {
            ItemStack itemstack = playerEntity.getStackInHand(playerHand);

            if (itemstack.getItem() == Items.GLASS_BOTTLE) {
                world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);

                if(!playerEntity.isCreative()) {
                    itemstack.decrement(1);
                    GeneralUtils.givePlayerItem(playerEntity, playerHand, new ItemStack(BzItems.SUGAR_WATER_BOTTLE), false);
                }
                return true;
            }
        }

        return false;
    }
}
