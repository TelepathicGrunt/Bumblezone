package com.telepathicgrunt.the_bumblezone.loot.functions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzLootFunctionTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;
import java.util.UUID;

public class UniquifyIfHasItems extends LootItemConditionalFunction {
    public static final MapCodec<UniquifyIfHasItems> CODEC = RecordCodecBuilder.mapCodec(instance -> UniquifyIfHasItems.commonFields(instance).apply(instance, UniquifyIfHasItems::new));

    public UniquifyIfHasItems(List<LootItemCondition> itemConditions) {
        super(itemConditions);
    }

    @Override
    public LootItemFunctionType getType() {
        return BzLootFunctionTypes.UNIQUIFY_IF_HAS_ITEMS.get();
    }

    @Override
    public ItemStack run(ItemStack itemStack, LootContext lootContext) {
        ItemContainerContents itemContainerContents = itemStack.get(DataComponents.CONTAINER);
        if (itemContainerContents == null || itemContainerContents.nonEmptyStream().findAny().isEmpty()) {
            return itemStack;
        }

        CompoundTag tag = new CompoundTag();
        tag.putString("UUID", UUID.randomUUID().toString());
        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return itemStack;
    }
}