package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.items.functions.DropContainerItems;
import com.telepathicgrunt.the_bumblezone.items.functions.HoneyCompassLocateStructure;
import com.telepathicgrunt.the_bumblezone.items.functions.PrefillMap;
import com.telepathicgrunt.the_bumblezone.items.functions.UniquifyIfHasItems;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class BzLootFunctionTypes {

    public static final ResourcefulRegistry<LootItemFunctionType> LOOT_ITEM_FUNCTION_TYPE = ResourcefulRegistries.create(Registries.LOOT_FUNCTION_TYPE, Bumblezone.MODID);

    public static final RegistryEntry<LootItemFunctionType> DROP_CONTAINER_ITEMS = LOOT_ITEM_FUNCTION_TYPE.register("drop_container_loot", () -> new LootItemFunctionType(new DropContainerItems.Serializer()));
    public static final RegistryEntry<LootItemFunctionType> UNIQUIFY_IF_HAS_ITEMS = LOOT_ITEM_FUNCTION_TYPE.register("uniquify_if_has_items", () -> new LootItemFunctionType(new UniquifyIfHasItems.Serializer()));
    public static final RegistryEntry<LootItemFunctionType> HONEY_COMPASS_LOCATE_STRUCTURE = LOOT_ITEM_FUNCTION_TYPE.register("honey_compass_locate_structure", () -> new LootItemFunctionType(new HoneyCompassLocateStructure.Serializer()));
    public static final RegistryEntry<LootItemFunctionType> PREFILL_MAP = LOOT_ITEM_FUNCTION_TYPE.register("prefill_map", () -> new LootItemFunctionType(new PrefillMap.Serializer()));
}
