package com.telepathicgrunt.the_bumblezone.events.player;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.world.entity.player.Player;

public record BzPlayerTickEvent(Player player, boolean end) {

    public static final EventHandler<BzPlayerTickEvent> EVENT = new EventHandler<>();

    public static final EventHandler<BzPlayerTickEvent> CLIENT_EVENT = new EventHandler<>();
}
