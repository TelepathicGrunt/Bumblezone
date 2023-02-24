package com.telepathicgrunt.the_bumblezone.entities;


import com.telepathicgrunt.the_bumblezone.events.ItemUseOnBlockEvent;

public class ItemUseOnBlock {

    public static boolean onItemUseOnBlock(ItemUseOnBlockEvent event) {
        return EntityTeleportationHookup.runItemUseOn(event.user(), event.clickedPos(), event.blockstate(), event.usingStack());
    }
}
