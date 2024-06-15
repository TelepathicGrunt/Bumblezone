package com.telepathicgrunt.the_bumblezone.packets;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public record StinglessBeeHelmetSightPacket(byte giveAdvancement) implements Packet<StinglessBeeHelmetSightPacket> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "stingless_bee_helmet_sight");
    public static final ServerboundPacketType<StinglessBeeHelmetSightPacket> TYPE = new StinglessBeeHelmetSightPacket.Handler();

    public static void sendToServer(boolean giveAdvancement) {
        MessageHandler.DEFAULT_CHANNEL.sendToServer(new StinglessBeeHelmetSightPacket((byte) (giveAdvancement ? 1 : 0)));
    }

    @Override
    public PacketType<StinglessBeeHelmetSightPacket> type() {
        return TYPE;
    }

    private static final class Handler implements ServerboundPacketType<StinglessBeeHelmetSightPacket> {

        @Override
        public void encode(StinglessBeeHelmetSightPacket message, RegistryFriendlyByteBuf buffer) {
            buffer.writeByte(message.giveAdvancement);
        }

        @Override
        public StinglessBeeHelmetSightPacket decode(RegistryFriendlyByteBuf buffer) {
            return new StinglessBeeHelmetSightPacket(buffer.readByte());
        }

        @Override
        public Consumer<Player> handle(StinglessBeeHelmetSightPacket message) {
            return (player) -> {
                if(player instanceof ServerPlayer serverPlayer && message.giveAdvancement() != 0) {
                    BzCriterias.STINGLESS_BEE_HELMET_SUPER_SIGHT_TRIGGER.get().trigger(serverPlayer);
                }
            };
        }

        @Override
        public Class<StinglessBeeHelmetSightPacket> type() {
            return StinglessBeeHelmetSightPacket.class;
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }
    }
}
