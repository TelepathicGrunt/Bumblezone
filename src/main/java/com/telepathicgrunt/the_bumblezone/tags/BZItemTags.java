package com.telepathicgrunt.the_bumblezone.tags;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

public class BZItemTags {
    // All tag wrappers need to be made at mod init.
    public static void tagInit(){}

    public static final ITag.INamedTag<Item> TURN_SLIME_TO_HONEY_SLIME = ItemTags.bind(Bumblezone.MODID+":turn_slime_to_honey_slime");
    public static final ITag.INamedTag<Item> HONEY_CRYSTAL_SHIELD_REPAIR_ITEMS = ItemTags.bind(Bumblezone.MODID+":honey_crystal_shield_repair_items");
}
