package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.screens.CrystallineFlowerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record CrystallineFlowerClickedEnchantmentButtonPacket(int containerId, int clickedButton) {

    public static void sendToServer(int containIdIn, int ClickedButtonIn) {
        MessageHandler.DEFAULT_CHANNEL.sendToServer(new CrystallineFlowerClickedEnchantmentButtonPacket(containIdIn, ClickedButtonIn));
    }

    /*
     * How the server will read the packet.
     */
    public static CrystallineFlowerClickedEnchantmentButtonPacket parse(final FriendlyByteBuf buf) {
        return new CrystallineFlowerClickedEnchantmentButtonPacket(buf.readInt(), buf.readInt());
    }

    /*
     * creates the packet buffer and sets its values
     */
    public static void compose(final CrystallineFlowerClickedEnchantmentButtonPacket pkt, final FriendlyByteBuf buf) {
        buf.writeInt(pkt.containerId());
        buf.writeInt(pkt.clickedButton());
    }

    public static class Handler {
        //this is what gets run on the server
        public static void handle(final CrystallineFlowerClickedEnchantmentButtonPacket pkt, final Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer serverPlayer = ctx.get().getSender();
                if(serverPlayer == null) {
                    return;
                }

                if (serverPlayer.containerMenu.containerId == pkt.containerId() && serverPlayer.containerMenu instanceof CrystallineFlowerMenu flowerMenu) {
                    flowerMenu.clickMenuButton(serverPlayer, pkt.clickedButton());
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}