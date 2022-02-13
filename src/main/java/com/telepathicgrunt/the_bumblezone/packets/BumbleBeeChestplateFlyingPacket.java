package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.items.BumbleBeeChestplate;
import com.telepathicgrunt.the_bumblezone.utils.MessageHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public record BumbleBeeChestplateFlyingPacket(byte isFlying) {

    public static void sendToServer(boolean isFlying) {
        MessageHandler.DEFAULT_CHANNEL.send(PacketDistributor.SERVER.with(() -> null), new BumbleBeeChestplateFlyingPacket((byte) (isFlying ? 1 : 0)));
    }

    /*
     * How the server will read the packet.
     */
    public static BumbleBeeChestplateFlyingPacket parse(final FriendlyByteBuf buf) {
        return new BumbleBeeChestplateFlyingPacket(buf.readByte());
    }

    /*
     * creates the packet buffer and sets its values
     */
    public static void compose(final BumbleBeeChestplateFlyingPacket pkt, final FriendlyByteBuf buf) {
        buf.writeByte(pkt.isFlying);
    }

    /*
     * What the server will do with the packet
     */
    public static class Handler {
        //this is what gets run on the server
        public static void handle(final BumbleBeeChestplateFlyingPacket pkt, final Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer serverPlayer = ctx.get().getSender();
                if(serverPlayer == null) {
                    return;
                }

                ItemStack itemStack = BumbleBeeChestplate.getEntityBeeChestplate(serverPlayer);
                if(!itemStack.isEmpty()) {
                    CompoundTag tag = itemStack.getOrCreateTag();
                    tag.putBoolean("isFlying", pkt.isFlying() != 0);
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
