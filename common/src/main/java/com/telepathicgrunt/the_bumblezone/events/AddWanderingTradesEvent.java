package com.telepathicgrunt.the_bumblezone.events;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.List;

public record AddWanderingTradesEvent(List<VillagerTrades.ItemListing> basic, List<VillagerTrades.ItemListing> rare) {

    public static final EventHandler<AddWanderingTradesEvent> EVENT = new EventHandler<>();
}
