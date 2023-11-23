package com.telepathicgrunt.the_bumblezone.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzLootFunctionTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;
import java.util.UUID;

public class UniquifyIfHasItems extends LootItemConditionalFunction {
    public static final Codec<UniquifyIfHasItems> CODEC = RecordCodecBuilder.create(instance -> UniquifyIfHasItems.commonFields(instance).apply(instance, UniquifyIfHasItems::new));

    public UniquifyIfHasItems(List<LootItemCondition> itemConditions) {
        super(itemConditions);
    }

    @Override
    public LootItemFunctionType getType() {
        return BzLootFunctionTypes.UNIQUIFY_IF_HAS_ITEMS.get();
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
}