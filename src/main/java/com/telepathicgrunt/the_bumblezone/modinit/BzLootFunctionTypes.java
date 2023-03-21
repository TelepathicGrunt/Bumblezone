package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.items.functions.DropContainerItems;
import com.telepathicgrunt.the_bumblezone.items.functions.HoneyCompassLocateStructure;
import com.telepathicgrunt.the_bumblezone.items.functions.PrefillMap;
import com.telepathicgrunt.the_bumblezone.items.functions.TagItemRemovals;
import com.telepathicgrunt.the_bumblezone.items.functions.UniquifyIfHasItems;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BzLootFunctionTypes {

    public static final DeferredRegister<LootItemFunctionType> LOOT_ITEM_FUNCTION_TYPE = DeferredRegister.create(Registry.LOOT_FUNCTION_REGISTRY, Bumblezone.MODID);

    public static final RegistryObject<LootItemFunctionType> DROP_CONTAINER_ITEMS = LOOT_ITEM_FUNCTION_TYPE.register("drop_container_loot", () -> new LootItemFunctionType(new DropContainerItems.Serializer()));
    public static final RegistryObject<LootItemFunctionType> UNIQUIFY_IF_HAS_ITEMS = LOOT_ITEM_FUNCTION_TYPE.register("uniquify_if_has_items", () -> new LootItemFunctionType(new UniquifyIfHasItems.Serializer()));
    public static final RegistryObject<LootItemFunctionType> HONEY_COMPASS_LOCATE_STRUCTURE = LOOT_ITEM_FUNCTION_TYPE.register("honey_compass_locate_structure", () -> new LootItemFunctionType(new HoneyCompassLocateStructure.Serializer()));
    public static final RegistryObject<LootItemFunctionType> PREFILL_MAP = LOOT_ITEM_FUNCTION_TYPE.register("prefill_map", () -> new LootItemFunctionType(new PrefillMap.Serializer()));
    public static final RegistryObject<LootItemFunctionType> TAG_ITEM_REMOVALS = LOOT_ITEM_FUNCTION_TYPE.register("tag_item_removals", () -> new LootItemFunctionType(new TagItemRemovals.Serializer()));
}
