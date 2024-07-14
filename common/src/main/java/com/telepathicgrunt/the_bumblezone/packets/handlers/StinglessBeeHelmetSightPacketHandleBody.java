package com.telepathicgrunt.the_bumblezone.packets.handlers;

import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.packets.StinglessBeeHelmetSightPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class StinglessBeeHelmetSightPacketHandleBody {
    public static void handle(StinglessBeeHelmetSightPacket message, Player player) {
        if(player instanceof ServerPlayer serverPlayer && message.giveAdvancement() != 0) {
            BzCriterias.STINGLESS_BEE_HELMET_SUPER_SIGHT_TRIGGER.get().trigger(serverPlayer);
        }
    }
}