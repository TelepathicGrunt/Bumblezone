package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.entity;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.telepathicgrunt.the_bumblezone.events.ProjectileHitEvent;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LlamaSpit.class)
public class LlamaSpitMixin {

    // Teleports player to Bumblezone when projectile hits bee nest
    @WrapWithCondition(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/LlamaSpit;onHit(Lnet/minecraft/world/phys/HitResult;)V"))
    private boolean bumblezone$onHit(LlamaSpit projectile, HitResult result) {
        ProjectileHitEvent event = new ProjectileHitEvent(projectile, result);
        if (ProjectileHitEvent.EVENT_HIGH.invoke(event)) {
            return false;
        }
        return !ProjectileHitEvent.EVENT.invoke(event);
    }
}