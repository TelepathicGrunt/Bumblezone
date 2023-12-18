package com.telepathicgrunt.the_bumblezone.packets.networking.neoforge;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.Packet;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketHandler;
import com.telepathicgrunt.the_bumblezone.platform.ModInfo;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.simple.SimpleChannel;

import java.util.HashMap;
import java.util.Map;

public class PacketChannelHelperImpl {
    public static final Map<ResourceLocation, Channel> CHANNELS = new HashMap<>();

    public static void registerChannel(ResourceLocation name) {
        ModInfo info = PlatformHooks.getModInfo(Bumblezone.MODID, true);
        String protocolVersion = info.version();
        Channel channel = new Channel(0, NetworkRegistry.newSimpleChannel(name, () -> protocolVersion, protocolVersion::equals, protocolVersion::equals));
        CHANNELS.put(name, channel);
    }

    public static <T extends Packet<T>> void registerS2CPacket(ResourceLocation name, ResourceLocation id, PacketHandler<T> handler, Class<T> packetClass) {
        Channel channel = CHANNELS.get(name);
        if (channel == null) {
            throw new IllegalStateException("Channel " + name + " not registered");
        }
        channel.channel.registerMessage(++channel.packets, packetClass, handler::encode, handler::decode, (msg, ctx) -> {
            Player sender = ctx.getSender();

            ctx.enqueueWork(() -> {
                Player player = null;
                if (sender == null) {
                    player = GeneralUtilsClient.getClientPlayer();
                }

                if (player != null) {
                    handler.handle(msg).apply(player, player.level());
                }
            });

            ctx.setPacketHandled(true);
        });
    }

    public static <T extends Packet<T>> void registerC2SPacket(ResourceLocation name, ResourceLocation id, PacketHandler<T> handler, Class<T> packetClass) {
        Channel channel = CHANNELS.get(name);
        if (channel == null) {
            throw new IllegalStateException("Channel " + name + " not registered");
        }
        channel.channel.registerMessage(++channel.packets, packetClass, handler::encode, handler::decode, (msg, ctx) -> {
            Player player = ctx.getSender();

            ctx.enqueueWork(() -> {
                if (player != null) {
                    handler.handle(msg).apply(player, player.level());
                }
            });

            ctx.setPacketHandled(true);
        });
    }

    public static <T extends Packet<T>> void sendToServer(ResourceLocation name, T packet) {
        Channel channel = CHANNELS.get(name);
        if (channel == null) {
            throw new IllegalStateException("Channel " + name + " not registered");
        }
        channel.channel.sendToServer(packet);
    }

    public static <T extends Packet<T>> void sendToPlayer(ResourceLocation name, T packet, Player player) {
        Channel channel = CHANNELS.get(name);
        if (channel == null) {
            throw new IllegalStateException("Channel " + name + " not registered");
        }
        if (player instanceof ServerPlayer serverPlayer) {
            channel.channel.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
        }
    }

    private static final class Channel {
        private int packets;
        private final SimpleChannel channel;

        private Channel(int packets, SimpleChannel channel) {
            this.packets = packets;
            this.channel = channel;
        }
    }
}
