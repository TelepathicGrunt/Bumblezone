package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Optional;

public record MainTradeRowInput(Optional<TagKey<Item>> tagKey, Item item) {
    public static final Codec<MainTradeRowInput> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            TagKey.codec(Registries.ITEM).optionalFieldOf("tagkey").forGetter(e -> e.tagKey),
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("wantItems").forGetter(e -> e.item)
    ).apply(instance, instance.stable(MainTradeRowInput::new)));
}
