package com.telepathicgrunt.the_bumblezone.items.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.telepathicgrunt.the_bumblezone.modinit.BzLootFunctionTypes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class TagItemRemovals extends LootItemConditionalFunction {
    final TagKey<Item> tagKey;

    public TagItemRemovals(LootItemCondition[] itemConditions, TagKey<Item> tagKey) {
        super(itemConditions);
        this.tagKey = tagKey;
    }

    @Override
    public LootItemFunctionType getType() {
        return BzLootFunctionTypes.TAG_ITEM_REMOVALS;
    }

    @Override
    public ItemStack run(ItemStack itemStack, LootContext lootContext) {
        return itemStack.is(this.tagKey) ? ItemStack.EMPTY : itemStack;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<TagItemRemovals> {
        public void serialize(JsonObject jsonObject, TagItemRemovals tagItemRemovals, JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, tagItemRemovals, jsonSerializationContext);
            jsonObject.addProperty("tag", tagItemRemovals.tagKey.location().toString());
        }

        public TagItemRemovals deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootItemCondition[] containerItems) {
            return new TagItemRemovals(containerItems, TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(GsonHelper.getAsString(jsonObject, "tag"))));
        }
    }
}