package com.telepathicgrunt.the_bumblezone.modinit;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.loot.conditions.EssenceOnlySpawn;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class BzLootConditionTypes {

    public static final ResourcefulRegistry<LootItemConditionType> LOOT_ITEM_CONDITION_TYPE = ResourcefulRegistries.create(BuiltInRegistries.LOOT_CONDITION_TYPE, Bumblezone.MODID);

    public static final RegistryEntry<LootItemConditionType> ESSENCE_ONLY_SPAWN = LOOT_ITEM_CONDITION_TYPE.register("essence_only_spawn", () -> new LootItemConditionType(EssenceOnlySpawn.CODEC));
}
