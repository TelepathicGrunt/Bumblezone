package com.telepathicgrunt.the_bumblezone.mixin.gameplay;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(ServerBossEvent.class)
public interface ServerBossEventAccessor {
    @Accessor("players")
    Set<ServerPlayer> bumblezone$getPlayers();
}
