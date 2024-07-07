package com.telepathicgrunt.the_bumblezone.events.entity;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.function.Consumer;

public record BzRegisterWanderingTradesEvent(Consumer<VillagerTrades.ItemListing> basic, Consumer<VillagerTrades.ItemListing> rare) {

    public static final EventHandler<BzRegisterWanderingTradesEvent> EVENT = new EventHandler<>();

    public void addBasicTrade(VillagerTrades.ItemListing trade) {
        basic.accept(trade);
    }

    public void addRareTrade(VillagerTrades.ItemListing trade) {
        rare.accept(trade);
    }
}
