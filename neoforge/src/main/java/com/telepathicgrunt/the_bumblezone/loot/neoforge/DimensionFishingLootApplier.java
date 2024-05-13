package com.telepathicgrunt.the_bumblezone.loot.neoforge;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.loot.NewLootInjectorApplier;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.function.Supplier;

public class DimensionFishingLootApplier extends LootModifier {

    public static final Supplier<MapCodec<DimensionFishingLootApplier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.mapCodec(inst -> codecStart(inst).apply(inst, DimensionFishingLootApplier::new)));

    public DimensionFishingLootApplier(final LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext lootContext) {
        if (NewLootInjectorApplier.checkIfValidForDimensionFishingLoot(lootContext)) {
            ObjectArrayList<ItemStack> newItems = new ObjectArrayList<>();
            NewLootInjectorApplier.injectLoot(lootContext, newItems, NewLootInjectorApplier.BZ_DIMENSION_FISHING_LOOT_TABLE_RL);
            return newItems;
        }

        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}