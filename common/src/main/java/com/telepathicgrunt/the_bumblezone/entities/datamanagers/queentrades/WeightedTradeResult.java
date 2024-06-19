package com.telepathicgrunt.the_bumblezone.entities.datamanagers.queentrades;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WeightedTradeResult implements WeightedEntry {
    public static final Codec<WeightedTradeResult> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            TagKey.codec(Registries.ITEM).optionalFieldOf("t").forGetter(e -> e.tagKey),
            ItemStack.CODEC.listOf().optionalFieldOf("w").forGetter(e -> e.items),
            Codec.intRange(1, 64).fieldOf("c").forGetter(e -> e.count),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("xp").forGetter(e -> e.xpReward),
            ExtraCodecs.POSITIVE_INT.fieldOf("w8").forGetter(e -> e.weight),
            ExtraCodecs.POSITIVE_INT.fieldOf("tgw").forGetter(e -> e.totalGroupWeight)
    ).apply(instance, instance.stable(WeightedTradeResult::new)));

    public final Optional<TagKey<Item>> tagKey;

    private final Optional<List<ItemStack>> items;

    public final int count;

    public final int xpReward;

    public final int weight;

    private int totalGroupWeight;

    public WeightedTradeResult(Optional<TagKey<Item>> tagKey, Optional<List<ItemStack>> items, int count, int xpReward, int weight, int totalGroupWeight) {
        this.tagKey = tagKey;
        this.items = tagKey != null && tagKey.isPresent() ? Optional.empty() : items;
        this.count = count;
        this.xpReward = xpReward;
        this.weight = weight;
        this.totalGroupWeight = totalGroupWeight;
    }

    public WeightedTradeResult(Optional<TagKey<Item>> tagKey, Optional<List<ItemStack>> items, int count, int xpReward, int weight) {
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

    public List<ItemStack> getItems() {
        List<ItemStack> itemsToReturn = new ArrayList<>();
        if (tagKey != null && tagKey.isPresent()) {
            itemsToReturn = tagKey.map(BuiltInRegistries.ITEM::getOrCreateTag).get().stream().map(v -> {
                ItemStack stack = v.value().getDefaultInstance();
                stack.grow(this.count);
                return stack;
            }).toList();
        }
        else if (items.isPresent()){
            itemsToReturn = items.get();
        }

        return itemsToReturn;
    }
}
