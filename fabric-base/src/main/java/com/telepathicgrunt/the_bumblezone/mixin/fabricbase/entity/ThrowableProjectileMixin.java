package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.entity;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.telepathicgrunt.the_bumblezone.events.ProjectileHitEvent;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ThrowableProjectile.class)
public class ThrowableProjectileMixin {


    @WrapWithCondition(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/projectile/ThrowableProjectile;onHit(Lnet/minecraft/world/phys/HitResult;)V"
            )
    )
    private boolean bumblezone$onHit(ThrowableProjectile projectile, HitResult result) {
        ProjectileHitEvent event = new ProjectileHitEvent(projectile, result);
        if (ProjectileHitEvent.EVENT_HIGH.invoke(event)) {
            return false;
        }
        return !ProjectileHitEvent.EVENT.invoke(event);
    }
}
