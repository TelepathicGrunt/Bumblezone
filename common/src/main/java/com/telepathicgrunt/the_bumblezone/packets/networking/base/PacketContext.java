package com.telepathicgrunt.the_bumblezone.packets.networking.base;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@FunctionalInterface
public interface PacketContext {

    void apply(Player player, Level level);
}
