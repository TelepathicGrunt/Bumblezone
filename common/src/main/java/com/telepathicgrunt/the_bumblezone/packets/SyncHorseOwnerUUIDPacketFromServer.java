package com.telepathicgrunt.the_bumblezone.packets;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

import java.util.UUID;

public record SyncHorseOwnerUUIDPacketFromServer(int horseId, UUID ownerUUID) implements Packet<SyncHorseOwnerUUIDPacketFromServer> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "sync_horse_owner_uuid_from_server");
    public static final ClientboundPacketType<SyncHorseOwnerUUIDPacketFromServer> TYPE = new SyncHorseOwnerUUIDPacketFromServer.Handler();

    public static void sendToClient(Entity entity, int horseId, UUID ownerUUID) {
        MessageHandler.DEFAULT_CHANNEL.sendToPlayersInLevel(new SyncHorseOwnerUUIDPacketFromServer(horseId, ownerUUID), entity.level());
    }

    @Override
    public PacketType<SyncHorseOwnerUUIDPacketFromServer> type() {
        return TYPE;
    }

    private static final class Handler implements ClientboundPacketType<SyncHorseOwnerUUIDPacketFromServer> {

        @Override
        public void encode(SyncHorseOwnerUUIDPacketFromServer message, RegistryFriendlyByteBuf buffer) {
            buffer.writeVarInt(message.horseId());
            buffer.writeUUID(message.ownerUUID());
        }

        @Override
        public SyncHorseOwnerUUIDPacketFromServer decode(RegistryFriendlyByteBuf buffer) {
            return new SyncHorseOwnerUUIDPacketFromServer(buffer.readVarInt(), buffer.readUUID());
        }

        @Override
        public Runnable handle(SyncHorseOwnerUUIDPacketFromServer message) {
            return () -> {
                Entity entity = GeneralUtilsClient.getClientLevel().getEntity(message.horseId());
                if (entity instanceof AbstractHorse abstractHorse) {
                    abstractHorse.setOwnerUUID(message.ownerUUID());
                }
            };
        }

        @Override
        public Class<SyncHorseOwnerUUIDPacketFromServer> type() {
            return SyncHorseOwnerUUIDPacketFromServer.class;
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }
    }
}
