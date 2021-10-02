package com.telepathicgrunt.bumblezone.items;

import com.telepathicgrunt.bumblezone.blocks.HoneyFluidBlock;
import com.telepathicgrunt.bumblezone.fluids.SugarWaterFluid;
import com.telepathicgrunt.bumblezone.modinit.BzItems;
import com.telepathicgrunt.bumblezone.tags.BzFluidTags;
import com.telepathicgrunt.bumblezone.utils.GeneralUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GlassBottleBehavior {

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


    public static boolean useBottleOnHoneyFluid(World world, PlayerEntity playerEntity, Hand playerHand, BlockPos blockPos) {
        FluidState currentFluidState = world.getFluidState(blockPos);
        if (currentFluidState.isIn(BzFluidTags.BZ_HONEY_FLUID) && currentFluidState.isStill()) {
            ItemStack itemstack = playerEntity.getStackInHand(playerHand);
            world.setBlockState(blockPos, currentFluidState.getBlockState().with(HoneyFluidBlock.LEVEL, 5), 3); // reduce honey
            world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);

            if(!playerEntity.isCreative()) {
                itemstack.decrement(1);
                GeneralUtils.givePlayerItem(playerEntity, playerHand, new ItemStack(Items.HONEY_BOTTLE), false);
            }

//            if ((playerEntity.getCommandSenderWorld().dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) ||
//                    Bumblezone.BzBeeAggressionConfig.allowWrathOfTheHiveOutsideBumblezone.get()) &&
//                    !playerEntity.isCreative() &&
//                    !playerEntity.isSpectator() &&
//                    Bumblezone.BzBeeAggressionConfig.aggressiveBees.get())
//            {
//                if(playerEntity.hasEffect(BzEffects.PROTECTION_OF_THE_HIVE.get())){
//                    playerEntity.removeEffect(BzEffects.PROTECTION_OF_THE_HIVE.get());
//                }
//                else{
//                    //Now all bees nearby in Bumblezone will get VERY angry!!!
//                    playerEntity.addEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE.get(), Bumblezone.BzBeeAggressionConfig.howLongWrathOfTheHiveLasts.get(), 2, false, Bumblezone.BzBeeAggressionConfig.showWrathOfTheHiveParticles.get(), true));
//                }
//            }

            return true;
        }

        return false;
    }
}
