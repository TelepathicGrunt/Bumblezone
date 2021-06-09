package com.telepathicgrunt.bumblezone.mixin.entities;

import com.telepathicgrunt.bumblezone.entities.PlayerTeleportationBackend;
import com.telepathicgrunt.bumblezone.entities.PlayerTeleportationHookup;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class PlayerDimensionChangeMixin {
    // Handles storing of past non-bumblezone dimension the player is leaving
    @Inject(method = "worldChanged(Lnet/minecraft/server/world/ServerWorld;)V",
            at = @At(value = "HEAD"))
    private void thebumblezone_onDimensionChange(ServerWorld origin, CallbackInfo ci) {
        PlayerTeleportationBackend.playerLeavingBz(origin.getRegistryKey().getValue(), ((ServerPlayerEntity)(Object)this));
    }
}