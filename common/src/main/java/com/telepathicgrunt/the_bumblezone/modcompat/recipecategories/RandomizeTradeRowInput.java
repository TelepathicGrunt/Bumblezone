package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Optional;

public record RandomizeTradeRowInput(Optional<TagKey<Item>> tagKey) {
    public static final Codec<RandomizeTradeRowInput> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            TagKey.codec(Registries.ITEM).optionalFieldOf("tagkey").forGetter(e -> e.tagKey)
    ).apply(instance, instance.stable(RandomizeTradeRowInput::new)));

    public HolderSet<Item> getWantItems() {
        return tagKey.map(BuiltInRegistries.ITEM::getOrCreateTag).orElse(null);
    }
}
