package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import net.minecraft.resources.ResourceLocation;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

public class BeehemothControlsPacket {

    public static ResourceLocation PACKET_ID = new ResourceLocation(Bumblezone.MODID, "beehemoth_controls_packet");

    public static void registerPacket() {
        ServerPlayNetworking.registerGlobalReceiver(PACKET_ID,
            (minecraftServer, serverPlayer, packetListener, buf, packetSender) -> {
                int upPressed = buf.readByte();
                int downPressed = buf.readByte();
                minecraftServer.execute(() -> {
                    if(serverPlayer == null) {
                        return;
                    }

                    if (serverPlayer.getVehicle() instanceof BeehemothEntity beehemothEntity) {
                        if (upPressed != 2) {
                            beehemothEntity.movingStraightUp = upPressed == 1;
                        }
                        if (downPressed != 2) {
                            beehemothEntity.movingStraightDown = downPressed == 1;
                        }
                    }
                });
            });
    }
}
