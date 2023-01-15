package com.telepathicgrunt.the_bumblezone.packets.networking.base;

import net.minecraft.resources.ResourceLocation;

public interface Packet<T extends Packet<T>> {
    ResourceLocation getID();
    PacketHandler<T> getHandler();
}
