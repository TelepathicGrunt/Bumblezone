package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public record MainTradeRowInput(TagKey<Item> tagKey, Item item) {
}
