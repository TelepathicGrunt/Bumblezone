package com.telepathicgrunt.the_bumblezone.tags;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;

public class BzItemTags {
    // All tag wrappers need to be made at mod init.
    public static void tagInit(){}

    public static final Tag.Named<Item> TURN_SLIME_TO_HONEY_SLIME = ItemTags.createOptional(new ResourceLocation(Bumblezone.MODID, "turn_slime_to_honey_slime"));
    public static final Tag.Named<Item> HONEY_CRYSTAL_SHIELD_REPAIR_ITEMS = ItemTags.createOptional(new ResourceLocation(Bumblezone.MODID, "honey_crystal_shield_repair_items"));
    public static final Tag.Named<Item> STINGER_SPEAR_REPAIR_ITEMS = ItemTags.createOptional(new ResourceLocation(Bumblezone.MODID, "stinger_spear_repair_items"));
    public static final Tag.Named<Item> BEE_FEEDING_ITEMS = ItemTags.createOptional(new ResourceLocation(Bumblezone.MODID, "bee_feeding_items"));
    public static final Tag.Named<Item> WRATH_ACTIVATING_ITEMS_WHEN_PICKED_UP = ItemTags.createOptional(new ResourceLocation(Bumblezone.MODID, "wrath_activating_items_when_picked_up"));

    public static final Tag.Named<Item> HONEY_BUCKETS = ItemTags.createOptional(new ResourceLocation("forge", "buckets/honey"));
}
