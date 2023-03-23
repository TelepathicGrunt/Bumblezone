package com.telepathicgrunt.the_bumblezone.entities.queentrades;

import net.minecraft.tags.TagKey;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.Item;

import java.util.List;

public class WeightedTradeResult implements WeightedEntry {

    public final TagKey<Item> tagKey;

    public final List<Item> items;

    public final int count;

    public final int xpReward;

    public final int weight;

    private int totalGroupWeight;

    public WeightedTradeResult(TagKey<Item> tagKey, List<Item> items, int count, int xpReward, int weight) {
        this.tagKey = tagKey;
        this.items = items;
        this.count = count;
        this.xpReward = xpReward;
        this.weight = weight;
    }

    @Override
    public Weight getWeight() {
        return Weight.of(weight);
    }

    public int getTotalWeight() {
        return totalGroupWeight;
    }

    public void setTotalWeight(int totalGroupWeight) {
        this.totalGroupWeight = totalGroupWeight;
    }
}
