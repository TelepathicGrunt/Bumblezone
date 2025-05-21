package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.events.player.BzPlayerItemUseOnBlockEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.HangingEntityItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class PlayerInteractivity {
    public static InteractionResult disableBlockPlacingOn(BzPlayerItemUseOnBlockEvent event) {
        Player player = event.user();
        InteractionHand interactionHand = event.hand();
        BlockState blockState = event.level().getBlockState(event.hitResult().getBlockPos());
        if (player != null && blockState.is(BzTags.DISABLE_BLOCK_PLACING_ON)) {
            ItemStack itemStack = player.getItemInHand(interactionHand);
            Item item = itemStack.getItem();
            if (item instanceof BlockItem) {
                return InteractionResult.FAIL;
            }
            else if (item instanceof BucketItem) {
                return InteractionResult.FAIL;
            }
            else if (item instanceof HangingEntityItem) {
                return InteractionResult.FAIL;
            }
        }
        return null;
    }
}
