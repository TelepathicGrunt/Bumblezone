package com.telepathicgrunt.bumblezone.mixin.items;

import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ThrowableProjectile.class)
public class ThrownEntityImpactMixin {

    // For mod compat with potion of bees
    @Inject(method = "tick()V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/projectile/ThrowableProjectile;onHit(Lnet/minecraft/world/phys/HitResult;)V"),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void thebumblezone_onThrownEntityHit(CallbackInfo ci, HitResult hitResult) {
        /*
        if(ModChecker.potionOfBeesPresent) {
            ThrownEntity thrownEntity = (ThrownEntity)(Object)this;
            if(!thrownEntity.getEntityWorld().isClientSide()){
                if(PotionOfBeesRedirection.POBReviveLarvaBlockEvent(thrownEntity, hitResult.getPos())){
                    thrownEntity.kill();
                    ci.cancel();
                }
            }
        }
        */
    }
}