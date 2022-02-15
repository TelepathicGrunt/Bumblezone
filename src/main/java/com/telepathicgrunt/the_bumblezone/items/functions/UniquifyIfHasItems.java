package com.telepathicgrunt.the_bumblezone.items.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.telepathicgrunt.the_bumblezone.modinit.BzLootFunctionTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UniquifyIfHasItems extends LootItemConditionalFunction {
    public UniquifyIfHasItems(LootItemCondition[] itemConditions) {
        super(itemConditions);
    }

    @Override
    public LootItemFunctionType getType() {
        return BzLootFunctionTypes.UNIQUIFY_IF_HAS_ITEMS;
    }

    @Override
    public ItemStack run(ItemStack itemStack, LootContext lootContext) {
        CompoundTag parentTag = null;
        CompoundTag tag = itemStack.getTag();
        if(tag != null && tag.contains("BlockEntityTag")) {
            parentTag = tag;
            tag = tag.getCompound("BlockEntityTag");
        }

        if(tag != null) {
            ListTag listtag = tag.getList("Items", 10);
            if(listtag.size() == 0 && tag.size() == 1 && (parentTag == null || parentTag.size() == 1)) {
                itemStack.setTag(null);
            }
            else if(!tag.contains("UUID")) {
                if(parentTag != null) {
                    parentTag.putString("UUID", UUID.randomUUID().toString());
                }
                else {
                    tag.putString("UUID", UUID.randomUUID().toString());
                }
            }
        }
        return itemStack;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<UniquifyIfHasItems> {
        public void serialize(JsonObject jsonObject, UniquifyIfHasItems uniquifyIfHasItems, JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, uniquifyIfHasItems, jsonSerializationContext);
        }

        public UniquifyIfHasItems deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootItemCondition[] containerItems) {
            return new UniquifyIfHasItems(containerItems);
        }
    }
}