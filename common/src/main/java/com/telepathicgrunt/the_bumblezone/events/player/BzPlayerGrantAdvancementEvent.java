package com.telepathicgrunt.the_bumblezone.events.player;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.advancements.Advancement;
import net.minecraft.world.entity.player.Player;

public record BzPlayerGrantAdvancementEvent(Advancement advancement, Player player) {

    public static final EventHandler<BzPlayerGrantAdvancementEvent> EVENT = new EventHandler<>();
}
