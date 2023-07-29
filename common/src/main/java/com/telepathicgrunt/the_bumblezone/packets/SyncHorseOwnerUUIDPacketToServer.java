package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.Packet;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketContext;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

import java.util.UUID;

public record SyncHorseOwnerUUIDPacketToServer(UUID horseUUID) implements Packet<SyncHorseOwnerUUIDPacketToServer> {

    public static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "sync_horse_owner_uuid_to_server");
    public static final Handler HANDLER = new Handler();

    public static void sendToServer(UUID horseUUID) {
        MessageHandler.DEFAULT_CHANNEL.sendToServer(new SyncHorseOwnerUUIDPacketToServer(horseUUID));
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<SyncHorseOwnerUUIDPacketToServer> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<SyncHorseOwnerUUIDPacketToServer> {

        @Override
        public void encode(SyncHorseOwnerUUIDPacketToServer message, FriendlyByteBuf buffer) {
            buffer.writeUUID(message.horseUUID());
        }

        @Override
        public SyncHorseOwnerUUIDPacketToServer decode(FriendlyByteBuf buffer) {
            return new SyncHorseOwnerUUIDPacketToServer(buffer.readUUID());
        }

        @Override
        public PacketContext handle(SyncHorseOwnerUUIDPacketToServer message) {
            return (player, level) -> {
                Entity entity = ((ServerLevel)level).getEntity(message.horseUUID());
                if (entity instanceof AbstractHorse abstractHorse && abstractHorse.getOwnerUUID() != null) {
                    SyncHorseOwnerUUIDPacketFromServer.sendToClient(abstractHorse, abstractHorse.getId(), abstractHorse.getOwnerUUID());
                }
            };
        }
    }

}