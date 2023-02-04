package com.telepathicgrunt.the_bumblezone.packets.networking.base;

import net.minecraft.network.FriendlyByteBuf;

public interface PacketHandler<T extends Packet<T>> {

    void encode(T message, FriendlyByteBuf buffer);

    T decode(FriendlyByteBuf buffer);

    PacketContext handle(T message);
}
