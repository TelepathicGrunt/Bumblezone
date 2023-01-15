package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.Packet;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketContext;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record BeehemothControlsPacket(byte upPressed, byte downPressed) implements Packet<BeehemothControlsPacket> {

    public static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "beehemoth_controls");
    public static final Handler HANDLER = new Handler();

    /**
     * 2 means no action.
     * 1 means set it to true on serverside.
     * 0 means set it to false serverside.
     */
    public static void sendToServer(int upPressed, int downPressed) {
        MessageHandler.DEFAULT_CHANNEL.sendToServer(new BeehemothControlsPacket((byte) upPressed, (byte) downPressed));
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<BeehemothControlsPacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<BeehemothControlsPacket> {

        @Override
        public void encode(BeehemothControlsPacket message, FriendlyByteBuf buffer) {
            buffer.writeByte(message.upPressed);
            buffer.writeByte(message.downPressed);
        }

        @Override
        public BeehemothControlsPacket decode(FriendlyByteBuf buffer) {
            return new BeehemothControlsPacket(buffer.readByte(), buffer.readByte());
        }

        @Override
        public PacketContext handle(BeehemothControlsPacket message) {
            return (player, level) -> {
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
    }

}