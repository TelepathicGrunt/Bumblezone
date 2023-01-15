package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public record BeehemothControlsPacket(byte upPressed, byte downPressed) {

    /**
     * 2 means no action.
     * 1 means set it to true on serverside.
     * 0 means set it to false serverside.
     */
    public static void sendToServer(int upPressed, int downPressed) {
        MessageHandler.DEFAULT_CHANNEL.send(PacketDistributor.SERVER.with(() -> null), new BeehemothControlsPacket((byte) upPressed, (byte) downPressed));
    }

    /*
     * How the server will read the packet.
     */
    public static BeehemothControlsPacket parse(final FriendlyByteBuf buf) {
        return new BeehemothControlsPacket(buf.readByte(), buf.readByte());
    }

    /*
     * creates the packet buffer and sets its values
     */
    public static void compose(final BeehemothControlsPacket pkt, final FriendlyByteBuf buf) {
        buf.writeByte(pkt.upPressed);
        buf.writeByte(pkt.downPressed);
    }

    /*
     * What the server will do with the packet
     */
    public static class Handler {
        //this is what gets run on the server
        public static void handle(final BeehemothControlsPacket pkt, final Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer serverPlayer = ctx.get().getSender();
                if(serverPlayer == null) {
                    return;
                }

                if (serverPlayer.getVehicle() instanceof BeehemothEntity beehemothEntity) {
                    if (pkt.upPressed() != 2) {
                        beehemothEntity.movingStraightUp = pkt.upPressed() == 1;
                    }
                    if (pkt.downPressed() != 2) {
                        beehemothEntity.movingStraightDown = pkt.downPressed() == 1;
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
