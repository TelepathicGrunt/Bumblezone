package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluidBlock;
import com.telepathicgrunt.the_bumblezone.fluids.SugarWaterFluid;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public class GlassBottleBehavior {

    public static ItemStack useBottleOnSugarWater(BottleItem bottleItem, Level world, Player playerEntity, InteractionHand playerHand, BlockPos blockPos) {
        if (world.getFluidState(blockPos).getType() instanceof SugarWaterFluid) {
            ItemStack handStack = playerEntity.getItemInHand(playerHand);
            playerEntity.awardStat(Stats.ITEM_USED.get(bottleItem));
            return ItemUtils.createFilledResult(handStack, playerEntity, BzItems.SUGAR_WATER_BOTTLE.get().getDefaultInstance());
        }

        return ItemStack.EMPTY;
    }

    public static boolean useBottleOnBzHoneyLikeFluid(Level world, Player playerEntity, InteractionHand playerHand, BlockPos blockPos) {
        FluidState currentFluidState = world.getFluidState(blockPos);

        return convertHoneyFluidToBottleForm(world, playerEntity, playerHand, blockPos, currentFluidState, BzTags.BZ_HONEY_FLUID, Items.HONEY_BOTTLE) ||
                convertHoneyFluidToBottleForm(world, playerEntity, playerHand, blockPos, currentFluidState, BzTags.ROYAL_JELLY_FLUID, BzItems.ROYAL_JELLY_BOTTLE.get());
    }

    private static boolean convertHoneyFluidToBottleForm(Level world, Player playerEntity, InteractionHand playerHand, BlockPos blockPos, FluidState currentFluidState, TagKey<Fluid> fluidTag, Item resultItem) {
        if (currentFluidState.is(fluidTag) && currentFluidState.isSource()) {
            world.setBlock(blockPos, currentFluidState.createLegacyBlock().setValue(HoneyFluidBlock.LEVEL, 5), 3); // reduce honey
            world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);

            GeneralUtils.givePlayerItem(playerEntity, playerHand, new ItemStack(resultItem), false, true);

            world.playSound(playerEntity, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
            world.gameEvent(playerEntity, GameEvent.FLUID_PICKUP, blockPos);
            return true;
        }
        return false;
    }
}
