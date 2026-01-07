package com.telepathicgrunt.the_bumblezone.loot.functions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public class TagItemRemovals extends LootItemConditionalFunction {
    final TagKey<Item> tagKey;

    public static final MapCodec<TagItemRemovals> CODEC = RecordCodecBuilder.mapCodec(instance -> TagItemRemovals.commonFields(instance).and(
            instance.group(TagKey.codec(Registries.ITEM).fieldOf("tag").forGetter(tagItemRemovals -> tagItemRemovals.tagKey)).t1()
    ).apply(instance, TagItemRemovals::new));

    public TagItemRemovals(List<LootItemCondition> itemConditions, TagKey<Item> tagKey) {
        super(itemConditions);
        this.tagKey = tagKey;
    }

    @Override
    public MapCodec<TagItemRemovals> codec() {
        return CODEC;
    }

    @Override
    public ItemStack run(ItemStack itemStack, LootContext lootContext) {
        return itemStack.is(this.tagKey) ? ItemStack.EMPTY : itemStack;
    }
}