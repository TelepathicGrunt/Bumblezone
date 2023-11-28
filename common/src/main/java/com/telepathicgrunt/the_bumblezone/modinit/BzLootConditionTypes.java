package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.loot.conditions.BumblezoneDimensionCheck;
import com.telepathicgrunt.the_bumblezone.loot.conditions.DyingBeeEntitiesDrops;
import com.telepathicgrunt.the_bumblezone.loot.conditions.EssenceOnlySpawn;
import com.telepathicgrunt.the_bumblezone.loot.conditions.NonBumblezoneDimensionCheck;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class BzLootConditionTypes {

    public static final ResourcefulRegistry<LootItemConditionType> LOOT_ITEM_CONDITION_TYPE = ResourcefulRegistries.create(BuiltInRegistries.LOOT_CONDITION_TYPE, Bumblezone.MODID);

    public static final RegistryEntry<LootItemConditionType> ESSENCE_ONLY_SPAWN = LOOT_ITEM_CONDITION_TYPE.register("essence_only_spawn", () -> new LootItemConditionType(EssenceOnlySpawn.CODEC));
    public static final RegistryEntry<LootItemConditionType> DYING_BEE_ENTITY_DROPS = LOOT_ITEM_CONDITION_TYPE.register("dying_bee_entity_drops", () -> new LootItemConditionType(DyingBeeEntitiesDrops.CODEC));
    public static final RegistryEntry<LootItemConditionType> BUMBLEZONE_DIMENSION_CHECK = LOOT_ITEM_CONDITION_TYPE.register("bumblezone_dimension_check", () -> new LootItemConditionType(BumblezoneDimensionCheck.CODEC));
    public static final RegistryEntry<LootItemConditionType> NON_BUMBLEZONE_DIMENSION_CHECK = LOOT_ITEM_CONDITION_TYPE.register("non_bumblezone_dimension_check", () -> new LootItemConditionType(NonBumblezoneDimensionCheck.CODEC));
}
