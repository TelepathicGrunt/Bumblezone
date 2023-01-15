package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.Packet;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketContext;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record StinglessBeeHelmetSightPacket(byte giveAdvancement) implements Packet<StinglessBeeHelmetSightPacket> {

    public static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "stingless_bee_helmet_sight");
    public static final Handler HANDLER = new Handler();

    public static void sendToServer(boolean giveAdvancement) {
        MessageHandler.DEFAULT_CHANNEL.sendToServer(new StinglessBeeHelmetSightPacket((byte) (giveAdvancement ? 1 : 0)));
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<StinglessBeeHelmetSightPacket> getHandler() {
        return HANDLER;
    }

    private static final class Handler implements PacketHandler<StinglessBeeHelmetSightPacket> {

        @Override
        public void encode(StinglessBeeHelmetSightPacket message, FriendlyByteBuf buffer) {
            buffer.writeByte(message.giveAdvancement);
        }

        @Override
        public StinglessBeeHelmetSightPacket decode(FriendlyByteBuf buffer) {
            return new StinglessBeeHelmetSightPacket(buffer.readByte());
        }

        @Override
        public PacketContext handle(StinglessBeeHelmetSightPacket message) {
            return (player, level) -> {
                if(player instanceof ServerPlayer serverPlayer && message.giveAdvancement() != 0) {
                    BzCriterias.STINGLESS_BEE_HELMET_SUPER_SIGHT_TRIGGER.trigger(serverPlayer);
                }
            };
        }
    }
}
