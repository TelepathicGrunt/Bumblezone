package com.telepathicgrunt.the_bumblezone.items.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.CrystallineFlowerBlockEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzLootFunctionTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;

public class DropContainerItems extends LootItemConditionalFunction {
    public DropContainerItems(LootItemCondition[] itemConditions) {
        super(itemConditions);
    }

    @Override
    public LootItemFunctionType getType() {
        return BzLootFunctionTypes.DROP_CONTAINER_ITEMS.get();
    }

    @Override
    public ItemStack run(ItemStack itemStack, LootContext lootContext) {
        Level level = lootContext.getLevel();
        Vec3 spawnPos = lootContext.getParamOrNull(LootContextParams.ORIGIN);
        BlockEntity be = lootContext.getParamOrNull(LootContextParams.BLOCK_ENTITY);
        if(spawnPos != null && be instanceof Container container) {
            Containers.dropContents(level, BlockPos.containing(spawnPos), container);
        }
        else if (spawnPos != null && be instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity) {
            BlockPos itemDropPos = BlockPos.containing(spawnPos);

            ItemStack bookItems = crystallineFlowerBlockEntity.getBookSlotItems();
            if (!bookItems.isEmpty()) {
                Containers.dropItemStack(level, itemDropPos.getX(), itemDropPos.getY(), itemDropPos.getZ(), bookItems);
            }

            ItemStack consumeItems = crystallineFlowerBlockEntity.getConsumeSlotItems();
            if (!consumeItems.isEmpty()) {
                Containers.dropItemStack(level, itemDropPos.getX(), itemDropPos.getY(), itemDropPos.getZ(), consumeItems);
            }
        }
        return itemStack;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<DropContainerItems> {
        public void serialize(JsonObject jsonObject, DropContainerItems dropContainerItems, JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, dropContainerItems, jsonSerializationContext);
        }

        public DropContainerItems deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootItemCondition[] containerItems) {
            return new DropContainerItems(containerItems);
        }
    }
}