package com.telepathicgrunt.the_bumblezone.packets;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;

public class StinglessBeeHelmetClientNetworking {

    public static void sendDataToServer(FriendlyByteBuf passedData) {
        ClientPlayNetworking.send(StinglessBeeHelmetSightPacket.PACKET_ID, passedData);
    }
}
