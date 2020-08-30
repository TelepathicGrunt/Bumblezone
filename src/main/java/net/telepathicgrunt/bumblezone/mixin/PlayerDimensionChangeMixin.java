package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;
import net.telepathicgrunt.bumblezone.entities.PlayerTeleportation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public class PlayerDimensionChangeMixin {
    // Handles storing of past non-bumblezone dimension the player is leaving
    @Inject(method = "changeDimension",
            at = @At(value = "HEAD"))
    private void onDimensionChange(ServerWorld serverWorld, ITeleporter teleporter, CallbackInfoReturnable<Entity> cir) {
        PlayerTeleportation.playerLeavingBz(serverWorld.getRegistryKey().getValue(), ((ServerPlayerEntity)(Object)this));
    }
}