package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.screens.CrystallineFlowerMenu;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public class CrystallineFlowerClickedEnchantmentButtonPacket {

    public static ResourceLocation PACKET_ID = new ResourceLocation(Bumblezone.MODID, "crystalline_flower_clicked_enchantment_button_packet");

    public static void registerPacket() {
        ServerPlayNetworking.registerGlobalReceiver(PACKET_ID,
            (minecraftServer, serverPlayer, packetListener, buf, packetSender) -> {
                int containerId = buf.readInt();
                int clickedButton = buf.readVarInt();
                minecraftServer.execute(() -> {
                    if(serverPlayer == null) {
                        return;
                    }

                    if (serverPlayer.containerMenu.containerId == containerId && serverPlayer.containerMenu instanceof CrystallineFlowerMenu flowerMenu) {
                        flowerMenu.clickMenuButton(serverPlayer, clickedButton);
                    }
                });
            });
    }
}
