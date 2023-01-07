package com.telepathicgrunt.the_bumblezone.packets;

import net.minecraft.network.FriendlyByteBuf;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

public class StinglessBeeHelmetClientNetworking {

    public static void sendDataToServer(FriendlyByteBuf passedData) {
        ClientPlayNetworking.send(StinglessBeeHelmetSightPacket.PACKET_ID, passedData);
    }
}
