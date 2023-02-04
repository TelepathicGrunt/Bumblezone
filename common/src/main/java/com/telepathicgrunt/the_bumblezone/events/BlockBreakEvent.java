package com.telepathicgrunt.the_bumblezone.events;

import com.telepathicgrunt.the_bumblezone.events.base.CancellableEventHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public record BlockBreakEvent(Player player, BlockState state) {

    public static final CancellableEventHandler<BlockBreakEvent> EVENT_LOWEST = new CancellableEventHandler<>();
}
