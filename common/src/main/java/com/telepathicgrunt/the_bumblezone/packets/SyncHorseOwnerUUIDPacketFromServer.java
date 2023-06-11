package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.Packet;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketContext;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

import java.util.UUID;

public record SyncHorseOwnerUUIDPacketFromServer(int horseId, UUID ownerUUID) implements Packet<SyncHorseOwnerUUIDPacketFromServer> {

    public static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "sync_horse_owner_uuid_from_server");
    public static final Handler HANDLER = new Handler();

    public static void sendToClient(Entity entity, int horseId, UUID ownerUUID) {
        MessageHandler.DEFAULT_CHANNEL.sendToAllLoaded(new SyncHorseOwnerUUIDPacketFromServer(horseId, ownerUUID), entity.level(), entity.blockPosition());
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<SyncHorseOwnerUUIDPacketFromServer> getHandler() {
        return HANDLER;
    }

    private static final class Handler implements PacketHandler<SyncHorseOwnerUUIDPacketFromServer> {

        @Override
        public void encode(SyncHorseOwnerUUIDPacketFromServer message, FriendlyByteBuf buffer) {
            buffer.writeVarInt(message.horseId());
            buffer.writeUUID(message.ownerUUID());
        }

        @Override
        public SyncHorseOwnerUUIDPacketFromServer decode(FriendlyByteBuf buffer) {
            return new SyncHorseOwnerUUIDPacketFromServer(buffer.readVarInt(), buffer.readUUID());
        }

        @Override
        public PacketContext handle(SyncHorseOwnerUUIDPacketFromServer message) {
            return (player, level) -> {
                Entity entity = level.getEntity(message.horseId());
                if (entity instanceof AbstractHorse abstractHorse) {
                    abstractHorse.setOwnerUUID(message.ownerUUID());
                }
            };
        }
    }
}
