package com.telepathicgrunt.the_bumblezone.entities.queentrades;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WeightedTradeResult implements WeightedEntry {
    public static final Codec<WeightedTradeResult> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            TagKey.codec(Registry.ITEM_REGISTRY).optionalFieldOf("t").forGetter(e -> e.tagKey),
            Registry.ITEM.byNameCodec().listOf().optionalFieldOf("w").forGetter(e -> e.items),
            Codec.intRange(1, 64).fieldOf("c").forGetter(e -> e.count),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("xp").forGetter(e -> e.xpReward),
            Codec.intRange(1, Integer.MAX_VALUE).fieldOf("w8").forGetter(e -> e.weight),
            Codec.intRange(1, Integer.MAX_VALUE).fieldOf("tgw").forGetter(e -> e.totalGroupWeight)
    ).apply(instance, instance.stable(WeightedTradeResult::new)));

    public final Optional<TagKey<Item>> tagKey;

    private final Optional<List<Item>> items;

    public final int count;

    public final int xpReward;

    public final int weight;

    private int totalGroupWeight;

    public WeightedTradeResult(Optional<TagKey<Item>> tagKey, Optional<List<Item>> items, int count, int xpReward, int weight, int totalGroupWeight) {
        this.tagKey = tagKey;
        this.items = tagKey != null && tagKey.isPresent() ? Optional.empty() : items;
        this.count = count;
        this.xpReward = xpReward;
        this.weight = weight;
        this.totalGroupWeight = totalGroupWeight;
    }

    public WeightedTradeResult(Optional<TagKey<Item>> tagKey, Optional<List<Item>> items, int count, int xpReward, int weight) {
        this.tagKey = tagKey;
        this.items = tagKey != null && tagKey.isPresent() ? Optional.empty() : items;
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

    public List<Item> getItems() {
        List<Item> itemsToReturn = new ArrayList<>();
        if (tagKey != null && tagKey.isPresent()) {
            itemsToReturn = tagKey.map(Registry.ITEM::getOrCreateTag).get().stream().map(Holder::value).toList();
        }
        else if (items != null && items.isPresent()){
            itemsToReturn = items.get();
        }

        return itemsToReturn;
    }
}
