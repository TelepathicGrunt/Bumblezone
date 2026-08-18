package com.telepathicgrunt.the_bumblezone.packets.handlers;

import com.telepathicgrunt.the_bumblezone.items.BumbleBeeChestplate;
import com.telepathicgrunt.the_bumblezone.modinit.BzDataComponents;
import com.telepathicgrunt.the_bumblezone.packets.BumbleBeeChestplateFlyingPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BumbleBeeChestplateFlyingPacketHandleBody {
    public static void handle(BumbleBeeChestplateFlyingPacket message, Player player) {
        ItemStack itemStack = BumbleBeeChestplate.getEntityBeeChestplate(player);
        if(!itemStack.isEmpty()) {
            var data = itemStack.get(BzDataComponents.BUMBLEBEE_CHESTPLATE_DATA.get());
            if(data != null) {
                itemStack.set(BzDataComponents.BUMBLEBEE_CHESTPLATE_DATA.get(), data.withFlying(message.isFlying()));
            }
        }
    }
}