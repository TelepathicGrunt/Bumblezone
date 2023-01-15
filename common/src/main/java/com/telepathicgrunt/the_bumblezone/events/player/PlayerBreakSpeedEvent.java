package com.telepathicgrunt.the_bumblezone.events.player;

import com.google.common.util.concurrent.AtomicDouble;
import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public record PlayerBreakSpeedEvent(Player player, BlockState state, AtomicDouble speed) {

    public static final EventHandler<PlayerBreakSpeedEvent> EVENT = new EventHandler<>();
}
