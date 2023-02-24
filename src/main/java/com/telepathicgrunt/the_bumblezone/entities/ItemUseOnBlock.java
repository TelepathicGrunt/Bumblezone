package com.telepathicgrunt.the_bumblezone.entities;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class ItemUseOnBlock {

    public static InteractionResult onItemUseOnBlock(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
        if(EntityTeleportationHookup.runItemUseOn(player, hitResult.getBlockPos(), world.getBlockState(hitResult.getBlockPos()), player.getItemInHand(hand))) {
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
