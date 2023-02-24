package com.telepathicgrunt.the_bumblezone.entities;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ItemUseOnBlock {

    public static void onItemUseOnBlock(PlayerInteractEvent.RightClickBlock event) {
        if(EntityTeleportationHookup.runItemUseOn(event.getEntity(), event.getPos(), event.getEntity().level.getBlockState(event.getPos()), event.getItemStack())) {
            event.setCanceled(true);
        }
    }
}
