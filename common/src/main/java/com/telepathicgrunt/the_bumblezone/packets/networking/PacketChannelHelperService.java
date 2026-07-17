package com.telepathicgrunt.the_bumblezone.packets.networking;

import com.telepathicgrunt.the_bumblezone.packets.networking.base.Packet;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketHandler;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;

/**
 * DO NOT USE THIS DIRECTLY USE {@link NetworkChannel}
 */
@ApiStatus.Internal
public interface PacketChannelHelperService {
    PacketChannelHelperService INSTANCE = GeneralUtils.loadService(PacketChannelHelperService.class);

    void registerChannel(ResourceLocation channel);

    <T extends Packet<T>> void registerS2CPacket(ResourceLocation channel, ResourceLocation id, PacketHandler<T> handler, Class<T> packetClass);

    <T extends Packet<T>> void registerC2SPacket(ResourceLocation channel, ResourceLocation id, PacketHandler<T> handler, Class<T> packetClass);

    <T extends Packet<T>> void sendToServer(ResourceLocation channel, T packet);

    <T extends Packet<T>> void sendToPlayer(ResourceLocation channel, T packet, Player player);

}
