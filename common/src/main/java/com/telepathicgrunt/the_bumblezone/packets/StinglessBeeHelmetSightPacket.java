package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public record StinglessBeeHelmetSightPacket(byte giveAdvancement) {

    public static void sendToServer(boolean giveAdvancement) {
        MessageHandler.DEFAULT_CHANNEL.send(PacketDistributor.SERVER.with(() -> null), new StinglessBeeHelmetSightPacket((byte) (giveAdvancement ? 1 : 0)));
    }

    /*
     * How the server will read the packet.
     */
    public static StinglessBeeHelmetSightPacket parse(final FriendlyByteBuf buf) {
        return new StinglessBeeHelmetSightPacket(buf.readByte());
    }

    /*
     * creates the packet buffer and sets its values
     */
    public static void compose(final StinglessBeeHelmetSightPacket pkt, final FriendlyByteBuf buf) {
        buf.writeByte(pkt.giveAdvancement);
    }

    /*
     * What the server will do with the packet
     */
    public static class Handler {
        //this is what gets run on the server
        public static void handle(final StinglessBeeHelmetSightPacket pkt, final Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer serverPlayer = ctx.get().getSender();
                if(serverPlayer != null && pkt.giveAdvancement() != 0) {
                    BzCriterias.STINGLESS_BEE_HELMET_SUPER_SIGHT_TRIGGER.trigger(serverPlayer);
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
