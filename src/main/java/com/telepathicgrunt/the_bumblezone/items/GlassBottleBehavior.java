package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.blocks.HoneyFluidBlock;
import com.telepathicgrunt.the_bumblezone.fluids.SugarWaterFluid;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.tags.BzFluidTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GlassBottleBehavior {

    public static boolean useBottleOnSugarWater(World world, PlayerEntity playerEntity, Hand playerHand, BlockPos blockPos) {
        if (world.getFluidState(blockPos).getType() instanceof SugarWaterFluid) {
            ItemStack itemstack = playerEntity.getItemInHand(playerHand);

            if (itemstack.getItem() == Items.GLASS_BOTTLE) {
                world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);

                if(!playerEntity.isCreative()) {
                    itemstack.shrink(1);
                    GeneralUtils.givePlayerItem(playerEntity, playerHand, new ItemStack(BzItems.SUGAR_WATER_BOTTLE.get()), false);
                }

                return true;
            }
        }

        return false;
    }

    public static boolean useBottleOnHoneyFluid(World world, PlayerEntity playerEntity, Hand playerHand, BlockPos blockPos) {
        FluidState currentFluidState = world.getFluidState(blockPos);
        if (currentFluidState.is(BzFluidTags.BZ_HONEY_FLUID) && currentFluidState.isSource()) {
            ItemStack itemstack = playerEntity.getItemInHand(playerHand);
            world.setBlock(blockPos, currentFluidState.createLegacyBlock().setValue(HoneyFluidBlock.LEVEL, 5), 3); // reduce honey
            world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);

            if(!playerEntity.isCreative()) {
                itemstack.shrink(1);
                GeneralUtils.givePlayerItem(playerEntity, playerHand, new ItemStack(Items.HONEY_BOTTLE), false);
            }

//            if ((playerEntity.getCommandSenderWorld().dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) ||
//                    BzBeeAggressionConfigs.allowWrathOfTheHiveOutsideBumblezone.get()) &&
//                    !playerEntity.isCreative() &&
//                    !playerEntity.isSpectator() &&
//                    BzBeeAggressionConfigs.aggressiveBees.get())
//            {
//                if(playerEntity.hasEffect(BzEffects.PROTECTION_OF_THE_HIVE.get())){
//                    playerEntity.removeEffect(BzEffects.PROTECTION_OF_THE_HIVE.get());
//                }
//                else{
//                    //Now all bees nearby in Bumblezone will get VERY angry!!!
//                    playerEntity.addEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE.get(), BzBeeAggressionConfigs.howLongWrathOfTheHiveLasts.get(), 2, false, BzBeeAggressionConfigs.showWrathOfTheHiveParticles.get(), true));
//                }
//            }

            return true;
        }

        return false;
    }
}
