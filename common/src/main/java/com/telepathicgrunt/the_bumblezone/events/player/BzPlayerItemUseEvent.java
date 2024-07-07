package com.telepathicgrunt.the_bumblezone.events.player;

import com.telepathicgrunt.the_bumblezone.events.base.CancellableEventHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public record BzPlayerItemUseEvent(Player user, Level level, ItemStack usingStack) {

    public static final CancellableEventHandler<BzPlayerItemUseEvent> EVENT_HIGH = new CancellableEventHandler<>();

}
