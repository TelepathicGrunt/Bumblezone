package com.telepathicgrunt.the_bumblezone.events.player;

import com.telepathicgrunt.the_bumblezone.events.base.ReturnableEventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

public record PlayerRightClickedBlockEvent(Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitResult) {

    public static final ReturnableEventHandler<PlayerRightClickedBlockEvent, InteractionResult> EVENT = new ReturnableEventHandler<>();


}
