package com.telepathicgrunt.the_bumblezone.entities.queentrades;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Optional;

public class WeightedTradeResult implements WeightedEntry {
    public static final Codec<WeightedTradeResult> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            TagKey.codec(Registries.ITEM).optionalFieldOf("t").forGetter(e -> e.tagKey),
            BuiltInRegistries.ITEM.byNameCodec().listOf().fieldOf("w").forGetter(e -> e.items),
            Codec.intRange(1, 64).fieldOf("c").forGetter(e -> e.count),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("xp").forGetter(e -> e.xpReward),
            Codec.intRange(1, Integer.MAX_VALUE).fieldOf("w8").forGetter(e -> e.weight),
            Codec.intRange(1, Integer.MAX_VALUE).fieldOf("tgw").forGetter(e -> e.totalGroupWeight)
    ).apply(instance, instance.stable(WeightedTradeResult::new)));

    public final Optional<TagKey<Item>> tagKey;

    public final List<Item> items;

    public final int count;

    public final int xpReward;

    public final int weight;

    private int totalGroupWeight;

    public WeightedTradeResult(Optional<TagKey<Item>> tagKey, List<Item> items, int count, int xpReward, int weight, int totalGroupWeight) {
        this.tagKey = tagKey;
        this.items = items;
        this.count = count;
        this.xpReward = xpReward;
        this.weight = weight;
        this.totalGroupWeight = totalGroupWeight;
    }

    public WeightedTradeResult(Optional<TagKey<Item>> tagKey, List<Item> items, int count, int xpReward, int weight) {
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
