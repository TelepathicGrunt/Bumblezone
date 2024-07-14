package com.telepathicgrunt.the_bumblezone.packets;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.packets.handlers.UpdateFallingBlockPacketHandleBody;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public record UpdateFallingBlockPacket(int fallingBlockId, short layer) implements Packet<UpdateFallingBlockPacket> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "update_falling_block");
    public static final ClientboundPacketType<UpdateFallingBlockPacket> TYPE = new UpdateFallingBlockPacket.Handler();

    public static void sendToClient(Entity entity, int fallingBlockId, short layer) {
        MessageHandler.DEFAULT_CHANNEL.sendToAllLoaded(new UpdateFallingBlockPacket(fallingBlockId, layer), entity.level(), entity.blockPosition());
    }

    @Override
    public PacketType<UpdateFallingBlockPacket> type() {
        return TYPE;
    }

    private static final class Handler implements ClientboundPacketType<UpdateFallingBlockPacket> {

        @Override
        public void encode(UpdateFallingBlockPacket message, RegistryFriendlyByteBuf buffer) {
            buffer.writeVarInt(message.fallingBlockId);
            buffer.writeShort(message.layer);
        }

        @Override
        public UpdateFallingBlockPacket decode(RegistryFriendlyByteBuf buffer) {
            return new UpdateFallingBlockPacket(buffer.readVarInt(), buffer.readShort());
        }

        @Override
        public Runnable handle(UpdateFallingBlockPacket message) {
            return () -> UpdateFallingBlockPacketHandleBody.handle(message);
        }

        @Override
        public Class<UpdateFallingBlockPacket> type() {
            return UpdateFallingBlockPacket.class;
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }
    }
}
