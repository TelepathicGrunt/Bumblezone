package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Optional;

public record MainTradeRowInput(Optional<TagKey<Item>> tagKey, Item item) {
    public static final Codec<MainTradeRowInput> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            TagKey.codec(Registry.ITEM_REGISTRY).optionalFieldOf("tagkey").forGetter(e -> e.tagKey),
            Registry.ITEM.byNameCodec().fieldOf("wantItems").forGetter(e -> e.item)
    ).apply(instance, instance.stable(MainTradeRowInput::new)));
}
