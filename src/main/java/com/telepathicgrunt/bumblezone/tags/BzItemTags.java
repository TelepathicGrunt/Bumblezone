package com.telepathicgrunt.bumblezone.tags;

import com.telepathicgrunt.bumblezone.Bumblezone;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;

public class BzItemTags {
    // Sole purpose is to initalize the tag wrappers at mod startup
    public static void initTags(){}

    public static final Tag<Item> TURN_SLIME_TO_HONEY_SLIME = TagRegistry.item(new ResourceLocation(Bumblezone.MODID, "turn_slime_to_honey_slime"));
    public static final Tag<Item> HONEY_CRYSTAL_SHIELD_REPAIR_ITEMS = TagRegistry.item(new ResourceLocation(Bumblezone.MODID, "honey_crystal_shield_repair_items"));
    public static final Tag<Item> BEE_FEEDING_ITEMS = TagRegistry.item(new ResourceLocation(Bumblezone.MODID, "bee_feeding_items"));
    public static final Tag<Item> WRATH_ACTIVATING_ITEMS_WHEN_PICKED_UP = TagRegistry.item(new ResourceLocation(Bumblezone.MODID, "wrath_activating_items_when_picked_up"));

    public static final Tag<Item> HONEY_BUCKETS = TagRegistry.item(new ResourceLocation("c:buckets/honey"));
}
