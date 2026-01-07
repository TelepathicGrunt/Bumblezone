package com.telepathicgrunt.the_bumblezone.modinit;

import com.mojang.serialization.MapCodec;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.loot.functions.DropContainerLoot;
import com.telepathicgrunt.the_bumblezone.loot.functions.HoneyCompassLocateStructure;
import com.telepathicgrunt.the_bumblezone.loot.functions.TagItemRemovals;
import com.telepathicgrunt.the_bumblezone.loot.functions.UniquifyIfHasItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

public class BzLootFunctionTypes {

    public static final ResourcefulRegistry<MapCodec<? extends LootItemFunction>> LOOT_ITEM_FUNCTION_TYPE = ResourcefulRegistries.create(BuiltInRegistries.LOOT_FUNCTION_TYPE, Bumblezone.MODID);

    public static final RegistryEntry<MapCodec<? extends LootItemFunction>> DROP_CONTAINER_ITEMS = LOOT_ITEM_FUNCTION_TYPE.register("drop_container_loot", () -> DropContainerLoot.CODEC);
    public static final RegistryEntry<MapCodec<? extends LootItemFunction>> UNIQUIFY_IF_HAS_ITEMS = LOOT_ITEM_FUNCTION_TYPE.register("uniquify_if_has_items", () -> UniquifyIfHasItems.CODEC);
    public static final RegistryEntry<MapCodec<? extends LootItemFunction>> HONEY_COMPASS_LOCATE_STRUCTURE = LOOT_ITEM_FUNCTION_TYPE.register("honey_compass_locate_structure", () -> HoneyCompassLocateStructure.CODEC);
    public static final RegistryEntry<MapCodec<? extends LootItemFunction>> TAG_ITEM_REMOVALS = LOOT_ITEM_FUNCTION_TYPE.register("tag_item_removals", () -> TagItemRemovals.CODEC);
}
