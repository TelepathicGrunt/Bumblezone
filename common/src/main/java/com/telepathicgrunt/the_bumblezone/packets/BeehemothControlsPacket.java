package com.telepathicgrunt.the_bumblezone.packets;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public record BeehemothControlsPacket(byte upPressed, byte downPressed) implements Packet<BeehemothControlsPacket> {

    public static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "beehemoth_controls");
    public static final ServerboundPacketType<BeehemothControlsPacket> TYPE = new Handler();

    /**
     * 2 means no action.
     * 1 means set it to true on serverside.
     * 0 means set it to false serverside.
     */
    public static void sendToServer(int upPressed, int downPressed) {
        MessageHandler.DEFAULT_CHANNEL.sendToServer(new BeehemothControlsPacket((byte) upPressed, (byte) downPressed));
    }

    @Override
    public PacketType<BeehemothControlsPacket> type() {
        return TYPE;
    }

    private static class Handler implements ServerboundPacketType<BeehemothControlsPacket> {

        @Override
        public void encode(BeehemothControlsPacket message, RegistryFriendlyByteBuf buffer) {
            buffer.writeByte(message.upPressed);
            buffer.writeByte(message.downPressed);
        }

        @Override
        public BeehemothControlsPacket decode(RegistryFriendlyByteBuf buffer) {
            return new BeehemothControlsPacket(buffer.readByte(), buffer.readByte());
        }

        @Override
        public Consumer<Player> handle(BeehemothControlsPacket message) {
            return (player) -> {
                if(player == null) {
                    return;
                }

                if (player.getVehicle() instanceof BeehemothEntity beehemothEntity) {
                    if (message.upPressed() != 2) {
                        beehemothEntity.movingStraightUp = message.upPressed() == 1;
                    }
                    if (message.downPressed() != 2) {
                        beehemothEntity.movingStraightDown = message.downPressed() == 1;
                    }
                }
            };
        }

        @Override
        public Class<BeehemothControlsPacket> type() {
            return BeehemothControlsPacket.class;
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }
    }
}