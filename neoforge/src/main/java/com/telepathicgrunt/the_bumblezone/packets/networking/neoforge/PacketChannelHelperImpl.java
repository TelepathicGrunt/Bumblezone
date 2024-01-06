package com.telepathicgrunt.the_bumblezone.packets.networking.neoforge;

import com.google.common.base.Preconditions;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.Packet;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

import java.util.HashMap;
import java.util.Map;

public class PacketChannelHelperImpl {

    private static boolean frozen = false;
    private static final Map<ResourceLocation, PacketRegistration<?>> PACKETS = new HashMap<>();

    public static void registerPayloads(RegisterPayloadHandlerEvent event) {
        frozen = true;

        var registrar = event.registrar(Bumblezone.MODID);
        for (var registration : PACKETS.values()) {
            registration.register(registrar);
        }
    }

    public static <T extends Packet<T>> void registerS2CPacket(ResourceLocation name, ResourceLocation id, PacketHandler<T> handler, Class<T> packetClass) {
        Preconditions.checkState(!frozen, "Packets were already registered with the platform");
        Preconditions.checkState(!PACKETS.containsKey(id), "Duplicate packet id %s", id);
        PACKETS.put(id, new PacketRegistration<>(id, handler, packetClass, PacketFlow.CLIENTBOUND));
    }

    public static <T extends Packet<T>> void registerC2SPacket(ResourceLocation name, ResourceLocation id, PacketHandler<T> handler, Class<T> packetClass) {
        Preconditions.checkState(!frozen, "Packets were already registered with the platform");
        Preconditions.checkState(!PACKETS.containsKey(id), "Duplicate packet id %s", id);
        PACKETS.put(id, new PacketRegistration<>(id, handler, packetClass, PacketFlow.SERVERBOUND));
    }

    public static <T extends Packet<T>> void sendToServer(ResourceLocation name, T packet) {
        PacketDistributor.SERVER.noArg().send(new PayloadWrapper<>(packet));
    }

    public static <T extends Packet<T>> void sendToPlayer(ResourceLocation name, T packet, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.PLAYER.with(serverPlayer).send(new PayloadWrapper<>(packet));
        }
    }

    record PacketRegistration<T extends Packet<T>>(
            ResourceLocation packetId,
            PacketHandler<T> packetHandler,
            Class<T> packetClass,
            PacketFlow flow
    ) {
        public void register(IPayloadRegistrar registrar) {
            registrar.play(packetId, this::decode, builder -> {
                if (flow.isClientbound()) {
                    builder.client(this::handlePacketOnMainThread);
                }
                else if (flow.isServerbound()) {
                    builder.server(this::handlePacketOnMainThread);
                }
            });
        }

        private PayloadWrapper<T> decode(FriendlyByteBuf buffer) {
            return new PayloadWrapper<>(packetHandler.decode(buffer));
        }

        private void handlePacketOnMainThread(PayloadWrapper<T> payload, PlayPayloadContext context) {
            var player = context.player().orElse(null);
            var level = context.level().orElse(null);

            context.workHandler().execute(() -> {
                var packet = payload.packet();
                packet.getHandler().handle(packet).apply(player, level);
            });
        }
    }

    record PayloadWrapper<T extends Packet<T>>(T packet) implements CustomPacketPayload {
        @Override
        public void write(FriendlyByteBuf buffer) {
            packet.getHandler().encode(packet, buffer);
        }

        @Override
        public ResourceLocation id() {
            return packet.getID();
        }
    }
}