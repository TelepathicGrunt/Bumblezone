package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ItemUseOnBlock {

    public static InteractionResult onItemUseOnBlock(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(BzTags.DO_ITEM_RIGHT_CLICK_CHECK_EARLIER)) {
            return InteractionResult.PASS;
        }

        if(EntityTeleportationHookup.runItemUseOn(player, hitResult.getBlockPos(), world.getBlockState(hitResult.getBlockPos()), itemStack)) {
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public static InteractionResultHolder<ItemStack> onEarlyItemUseOnBlock(Player player, Level level, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!itemStack.is(BzTags.DO_ITEM_RIGHT_CLICK_CHECK_EARLIER)) {
            return InteractionResultHolder.pass(itemStack);
        }

        HitResult hitResult = player.pick(player.isCreative() ? 5.0f : 4.5f, 1.0f, false);

        if (hitResult.getType() == HitResult.Type.BLOCK && hitResult instanceof BlockHitResult blockHitResult) {
            if (EntityTeleportationHookup.runItemUseOn(player, blockHitResult.getBlockPos(), level.getBlockState(blockHitResult.getBlockPos()), itemStack)) {
                return InteractionResultHolder.success(itemStack);
            }
        }

        return InteractionResultHolder.pass(itemStack);
    }
}
