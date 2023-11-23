package com.telepathicgrunt.the_bumblezone.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.CrystallineFlowerBlockEntity;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.HoneyCocoonBlockEntity;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
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

import java.util.List;

public class DropContainerItems extends LootItemConditionalFunction {
    public static final Codec<DropContainerItems> CODEC = RecordCodecBuilder.create(instance -> DropContainerItems.commonFields(instance).apply(instance, DropContainerItems::new));

    public DropContainerItems(List<LootItemCondition> itemConditions) {
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
            if (!(ModChecker.lootrPresent &&
                container instanceof HoneyCocoonBlockEntity honeyCocoonBlockEntity &&
                honeyCocoonBlockEntity.getLootTable() != null))
            {
                Containers.dropContents(level, BlockPos.containing(spawnPos), container);
            }
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
}