package com.telepathicgrunt.the_bumblezone.entities.queentrades;

import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.Item;

public record TradeEntryReducedObj(Item item, int count, int xpReward, int weight, int totalGroupWeight, boolean randomizerTrade) implements WeightedEntry {

    public TradeEntryReducedObj(Item item, int count, int xpReward, int weight) {
        this(item, count, xpReward, weight, 1, false);
    }

    @Override
    public Weight getWeight() {
        return Weight.of(weight);
    }
}