package com.telepathicgrunt.the_bumblezone.events;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.List;

public record RegisterVillagerTradesEvent(VillagerProfession type, Int2ObjectMap<List<VillagerTrades.ItemListing>> trades) {

    public static final EventHandler<RegisterVillagerTradesEvent> EVENT = new EventHandler<>();
}
