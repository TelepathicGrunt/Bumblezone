package com.telepathicgrunt.the_bumblezone.entities.queentrades;

import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.Item;

public record TradeEntryReducedObj(Item item, int count, int xpReward, int weight) implements WeightedEntry {
    @Override
    public Weight getWeight() {
        return Weight.of(weight);
    }
}
