package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.items.conditions.EssenceOnlySpawn;
import com.telepathicgrunt.the_bumblezone.items.functions.DropContainerItems;
import com.telepathicgrunt.the_bumblezone.items.functions.HoneyCompassLocateStructure;
import com.telepathicgrunt.the_bumblezone.items.functions.PrefillMap;
import com.telepathicgrunt.the_bumblezone.items.functions.TagItemRemovals;
import com.telepathicgrunt.the_bumblezone.items.functions.UniquifyIfHasItems;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class BzLootConditionTypes {

    public static final ResourcefulRegistry<LootItemConditionType> LOOT_ITEM_CONDITION_TYPE = ResourcefulRegistries.create(BuiltInRegistries.LOOT_CONDITION_TYPE, Bumblezone.MODID);

    public static final RegistryEntry<LootItemConditionType> ESSENCE_ONLY_SPAWN = LOOT_ITEM_CONDITION_TYPE.register("essence_only_spawn", () -> new LootItemConditionType(new EssenceOnlySpawn.Serializer()));
}
