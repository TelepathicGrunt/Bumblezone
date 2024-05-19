package com.telepathicgrunt.the_bumblezone.modinit;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.loot.functions.DropContainerItems;
import com.telepathicgrunt.the_bumblezone.loot.functions.HoneyCompassLocateStructure;
import com.telepathicgrunt.the_bumblezone.loot.functions.TagItemRemovals;
import com.telepathicgrunt.the_bumblezone.loot.functions.UniquifyIfHasItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class BzLootFunctionTypes {

    public static final ResourcefulRegistry<LootItemFunctionType<?>> LOOT_ITEM_FUNCTION_TYPE = ResourcefulRegistries.create(BuiltInRegistries.LOOT_FUNCTION_TYPE, Bumblezone.MODID);

    public static final RegistryEntry<LootItemFunctionType<?>> DROP_CONTAINER_ITEMS = LOOT_ITEM_FUNCTION_TYPE.register("drop_container_loot", () -> new LootItemFunctionType<>(DropContainerItems.CODEC));
    public static final RegistryEntry<LootItemFunctionType<?>> UNIQUIFY_IF_HAS_ITEMS = LOOT_ITEM_FUNCTION_TYPE.register("uniquify_if_has_items", () -> new LootItemFunctionType<>(UniquifyIfHasItems.CODEC));
    public static final RegistryEntry<LootItemFunctionType<?>> HONEY_COMPASS_LOCATE_STRUCTURE = LOOT_ITEM_FUNCTION_TYPE.register("honey_compass_locate_structure", () -> new LootItemFunctionType<>(HoneyCompassLocateStructure.CODEC));
    public static final RegistryEntry<LootItemFunctionType<?>> TAG_ITEM_REMOVALS = LOOT_ITEM_FUNCTION_TYPE.register("tag_item_removals", () -> new LootItemFunctionType<>(TagItemRemovals.CODEC));
}
