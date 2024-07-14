package com.telepathicgrunt.the_bumblezone.packets.handlers;

import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.packets.SyncHorseOwnerUUIDPacketFromServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public class SyncHorseOwnerUUIDPacketFromServerHandleBody {
    public static void handle(SyncHorseOwnerUUIDPacketFromServer message) {
        Entity entity = GeneralUtilsClient.getClientLevel().getEntity(message.horseId());
        if (entity instanceof AbstractHorse abstractHorse) {
            abstractHorse.setOwnerUUID(message.ownerUUID());
        }
    }
}