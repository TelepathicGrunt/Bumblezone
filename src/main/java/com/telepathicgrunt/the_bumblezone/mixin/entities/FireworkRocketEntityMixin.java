package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.telepathicgrunt.the_bumblezone.entities.ProjectileImpact;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FireworkRocketEntity.class)
public class FireworkRocketEntityMixin {

    // Teleports player to Bumblezone when projectile hits bee nest
    @WrapWithCondition(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/FireworkRocketEntity;onHit(Lnet/minecraft/world/phys/HitResult;)V"))
    private boolean thebumblezone_onProjectileHit(FireworkRocketEntity projectile, HitResult hitResult) {
        return ProjectileImpact.projectileImpactNotHandledByBz(hitResult, projectile);
    }
}