package com.telepathicgrunt.the_bumblezone.packets.handlers;

import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.packets.BeehemothControlsPacket;
import net.minecraft.world.entity.player.Player;

public class BeehemothControlsPacketHandleBody {
    public static void handle(BeehemothControlsPacket message, Player player) {
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
    }
}