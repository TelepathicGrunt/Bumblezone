package com.telepathicgrunt.the_bumblezone.events.block;

import com.telepathicgrunt.the_bumblezone.events.base.CancellableEventHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public record BzBlockBreakEvent(Player player, BlockState state) {

    public static final CancellableEventHandler<BzBlockBreakEvent> EVENT_LOWEST = new CancellableEventHandler<>();
}
