package com.telepathicgrunt.the_bumblezone.events.player;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record PlayerCraftedItemEvent(Player player, ItemStack item, Container table) {

    public static final EventHandler<PlayerCraftedItemEvent> EVENT = new EventHandler<>();


}
