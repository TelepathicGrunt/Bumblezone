package com.telepathicgrunt.the_bumblezone.packets.handlers;

import com.telepathicgrunt.the_bumblezone.packets.SyncHorseOwnerUUIDPacketFromServer;
import com.telepathicgrunt.the_bumblezone.packets.SyncHorseOwnerUUIDPacketToServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;

public class SyncHorseOwnerUUIDPacketToServerHandleBody {
    public static void handle(SyncHorseOwnerUUIDPacketToServer message, Player player) {
        Entity entity = ((ServerLevel)player.level()).getEntity(message.horseUUID());
        if (entity instanceof AbstractHorse abstractHorse && abstractHorse.getOwnerUUID() != null) {
            SyncHorseOwnerUUIDPacketFromServer.sendToClient(abstractHorse, abstractHorse.getId(), abstractHorse.getOwnerUUID());
        }
    }
}