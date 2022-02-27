package com.telepathicgrunt.the_bumblezone.tags;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class BzItemTags {
    // Sole purpose is to initalize the tag wrappers at mod startup
    public static void initTags() {}

    public static final TagKey<Item> TURN_SLIME_TO_HONEY_SLIME = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "turn_slime_to_honey_slime"));
    public static final TagKey<Item> HONEY_CRYSTAL_SHIELD_REPAIR_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "honey_crystal_shield_repair_items"));
    public static final TagKey<Item> STINGER_SPEAR_REPAIR_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "stinger_spear_repair_items"));
    public static final TagKey<Item> BEE_ARMOR_REPAIR_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "bee_armor_repair_items"));
    public static final TagKey<Item> BEE_FEEDING_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "bee_feeding_items"));
    public static final TagKey<Item> WRATH_ACTIVATING_ITEMS_WHEN_PICKED_UP = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "wrath_activating_items_when_picked_up"));

    public static final TagKey<Item> HONEY_BUCKETS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("c:buckets/honey"));
}
