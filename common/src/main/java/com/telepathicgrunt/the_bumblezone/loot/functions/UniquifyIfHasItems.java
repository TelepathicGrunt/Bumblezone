package com.telepathicgrunt.the_bumblezone.loot.functions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;
import java.util.UUID;

public class UniquifyIfHasItems extends LootItemConditionalFunction {
    public static final MapCodec<UniquifyIfHasItems> CODEC = RecordCodecBuilder.mapCodec(instance -> UniquifyIfHasItems.commonFields(instance).apply(instance, UniquifyIfHasItems::new));

    public UniquifyIfHasItems(List<LootItemCondition> itemConditions) {
        super(itemConditions);
    }

    @Override
    public MapCodec<UniquifyIfHasItems> codec() {
        return CODEC;
    }

    @Override
    public ItemStack run(ItemStack itemStack, LootContext lootContext) {
        ItemContainerContents itemContainerContents = itemStack.get(DataComponents.CONTAINER);
        if (itemContainerContents == null || itemContainerContents.allItemsCopyStream().findAny().isEmpty()) {
            return itemStack;
        }

        CompoundTag tag = new CompoundTag();
        tag.putString("UUID", UUID.randomUUID().toString());
        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return itemStack;
    }
}