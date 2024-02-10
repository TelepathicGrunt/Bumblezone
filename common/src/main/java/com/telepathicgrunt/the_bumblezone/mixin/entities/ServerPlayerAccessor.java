package com.telepathicgrunt.the_bumblezone.mixin.entities;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerPlayer.class)
public interface ServerPlayerAccessor {
    @Accessor("startingToFallPosition")
    void setStartingToFallPosition(Vec3 startingToFallPosition);
}
