package com.telepathicgrunt.the_bumblezone.events;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.function.Consumer;

public record RegisterWanderingTradesEvent(Consumer<VillagerTrades.ItemListing> basic, Consumer<VillagerTrades.ItemListing> rare) {

    public static final EventHandler<RegisterWanderingTradesEvent> EVENT = new EventHandler<>();

    public void addBasicTrade(VillagerTrades.ItemListing trade) {
        basic.accept(trade);
    }

    public void addRareTrade(VillagerTrades.ItemListing trade) {
        rare.accept(trade);
    }
}
