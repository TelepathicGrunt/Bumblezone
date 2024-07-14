package com.telepathicgrunt.the_bumblezone.packets.handlers;

import com.telepathicgrunt.the_bumblezone.menus.CrystallineFlowerMenu;
import com.telepathicgrunt.the_bumblezone.packets.CrystallineFlowerClickedEnchantmentButtonPacket;
import net.minecraft.world.entity.player.Player;

public class CrystallineFlowerClickedEnchantmentButtonPacketHandleBody {
    public static void handle(CrystallineFlowerClickedEnchantmentButtonPacket message, Player player) {
        if(player == null) {
            return;
        }

        if (player.containerMenu.containerId == message.containerId() && player.containerMenu instanceof CrystallineFlowerMenu flowerMenu) {
            flowerMenu.clickMenuEnchantment(player, message.clickedButton());
        }
    }
}