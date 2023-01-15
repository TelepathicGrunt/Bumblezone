package com.telepathicgrunt.the_bumblezone.events.player;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.world.entity.player.Player;

public record PlayerTickEvent(Player player) {

    public static final EventHandler<PlayerTickEvent> EVENT = new EventHandler<>();
}
