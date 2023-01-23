package com.telepathicgrunt.the_bumblezone.packets.networking.quilt;

import com.telepathicgrunt.the_bumblezone.packets.networking.base.Packet;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.quiltmc.loader.api.minecraft.MinecraftQuiltLoader;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

public class PacketChannelHelperImpl {
    public static void registerChannel(ResourceLocation channel, int protocolVersion) {
        //Do Nothing
    }

    public static <T extends Packet<T>> void registerS2CPacket(ResourceLocation channel, ResourceLocation id, PacketHandler<T> handler, Class<T> packetClass) {
        if (MinecraftQuiltLoader.getEnvironmentType().equals(EnvType.CLIENT)) {
            QuiltClientPacketHelper.clientOnlyRegister(createChannelLocation(channel, id), handler);
        }
    }

    public static <T extends Packet<T>> void registerC2SPacket(ResourceLocation channel, ResourceLocation id, PacketHandler<T> handler, Class<T> packetClass) {
        ServerPlayNetworking.registerGlobalReceiver(createChannelLocation(channel, id), (server, player, handler1, buf, responseSender) -> {
            T decode = handler.decode(buf);
            server.execute(() -> handler.handle(decode).apply(player, player.level));
        });
    }

    public static <T extends Packet<T>> void sendToServer(ResourceLocation channel, T packet) {
        if (MinecraftQuiltLoader.getEnvironmentType().equals(EnvType.CLIENT))
            QuiltClientPacketHelper.sendToServerClientOnly(channel, packet);
    }

    public static <T extends Packet<T>> void sendToPlayer(ResourceLocation channel, T packet, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            packet.getHandler().encode(packet, buf);
            ServerPlayNetworking.send(serverPlayer, createChannelLocation(channel, packet.getID()), buf);
        }
    }

    private static ResourceLocation createChannelLocation(ResourceLocation channel, ResourceLocation id) {
        return new ResourceLocation(channel.getNamespace(), channel.getPath() + "/" + id.getNamespace() + "/" + id.getPath());
    }
}
