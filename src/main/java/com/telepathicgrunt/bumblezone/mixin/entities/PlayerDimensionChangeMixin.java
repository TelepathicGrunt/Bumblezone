package com.telepathicgrunt.bumblezone.mixin.entities;

import com.telepathicgrunt.bumblezone.entities.EntityTeleportationBackend;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class PlayerDimensionChangeMixin {

    @Shadow
    public World world;

    // Handles storing of past non-bumblezone dimension the entity is leaving
    @Inject(method = "moveToWorld(Lnet/minecraft/server/world/ServerWorld;)Lnet/minecraft/entity/Entity;",
            at = @At(value = "HEAD"))
    private void thebumblezone_onDimensionChange(ServerWorld destination, CallbackInfoReturnable<Entity> cir) {
        EntityTeleportationBackend.playerLeavingBz(world.getRegistryKey().getValue(), ((Entity)(Object)this));
    }
}