package com.telepathicgrunt.the_bumblezone.events.entity;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.function.BiConsumer;

public record BzRegisterVillagerTradesEvent(VillagerProfession type, BiConsumer<Integer, VillagerTrades.ItemListing> trade) {

    public static final EventHandler<BzRegisterVillagerTradesEvent> EVENT = new EventHandler<>();

    public void addTrade(int level, VillagerTrades.ItemListing trade) {
        this.trade.accept(level, trade);
    }
}
