package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public class StinglessBeeHelmetSightPacket {

    public static ResourceLocation PACKET_ID = new ResourceLocation(Bumblezone.MODID, "stingless_bee_helmet_sight_packet");

    public static void registerPacket() {
        ServerPlayNetworking.registerGlobalReceiver(PACKET_ID,
            (minecraftServer, serverPlayer, packetListener, buf, packetSender) -> {
                boolean giveAdvancement = buf.readByte() != 0;
                minecraftServer.execute(() -> {
                    if(serverPlayer != null && giveAdvancement) {
                        BzCriterias.STINGLESS_BEE_HELMET_SUPER_SIGHT_TRIGGER.trigger(serverPlayer);
                    }
                });
            });
    }
}
