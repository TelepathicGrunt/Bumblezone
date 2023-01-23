package com.telepathicgrunt.the_bumblezone.events;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.List;
import java.util.function.BiConsumer;

public record RegisterVillagerTradesEvent(VillagerProfession type, BiConsumer<Integer, VillagerTrades.ItemListing> trade) {

    public static final EventHandler<RegisterVillagerTradesEvent> EVENT = new EventHandler<>();

    public void addTrade(int level, VillagerTrades.ItemListing trade) {
        this.trade.accept(level, trade);
    }
}
