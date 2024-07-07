package com.telepathicgrunt.the_bumblezone.mixin.fabric.entity;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.telepathicgrunt.the_bumblezone.events.entity.BzProjectileHitEvent;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Projectile.class)
public class ProjectileMixin {

    // Teleports player to Bumblezone when projectile hits bee nest
    @WrapWithCondition(method = "hitTargetOrDeflectSelf(Lnet/minecraft/world/phys/HitResult;)Lnet/minecraft/world/entity/projectile/ProjectileDeflection;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;onHit(Lnet/minecraft/world/phys/HitResult;)V"))
    private boolean bumblezone$onHit(Projectile projectile, HitResult hitResult) {
        BzProjectileHitEvent event = new BzProjectileHitEvent(projectile, hitResult);
        if (BzProjectileHitEvent.EVENT_HIGH.invoke(event)) {
            return false;
        }
        return !BzProjectileHitEvent.EVENT.invoke(event);
    }
}