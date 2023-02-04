package com.telepathicgrunt.the_bumblezone.packets.networking;

import com.telepathicgrunt.the_bumblezone.packets.networking.base.Packet;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketHandler;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.ApiStatus;

/**
 * DO NOT USE THIS DIRECTLY USE {@link NetworkChannel}
 */
@ApiStatus.Internal
public class PacketChannelHelper {

    @ExpectPlatform
    public static void registerChannel(ResourceLocation channel, int protocolVersion) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static <T extends Packet<T>> void registerS2CPacket(ResourceLocation channel, ResourceLocation id, PacketHandler<T> handler, Class<T> packetClass) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static <T extends Packet<T>> void registerC2SPacket(ResourceLocation channel, ResourceLocation id, PacketHandler<T> handler, Class<T> packetClass) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static <T extends Packet<T>> void sendToServer(ResourceLocation channel, T packet) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static <T extends Packet<T>> void sendToPlayer(ResourceLocation channel, T packet, Player player) {
        throw new NotImplementedException();
    }




}
