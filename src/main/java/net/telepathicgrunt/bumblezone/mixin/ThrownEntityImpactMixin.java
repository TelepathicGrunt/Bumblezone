package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.telepathicgrunt.bumblezone.entities.PlayerTeleportation;
import net.telepathicgrunt.bumblezone.modCompat.ModChecker;
import net.telepathicgrunt.bumblezone.modCompat.PotionOfBeesRedirection;
import org.apache.commons.lang3.concurrent.CircuitBreaker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ThrownEntity.class)
public class ThrownEntityImpactMixin {

    // For mod compat with potion of bees
    @Inject(method = "tick()V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/projectile/thrown/ThrownEntity;onCollision(Lnet/minecraft/util/hit/HitResult;)V"),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void onThrownEntityHit(CallbackInfo ci, HitResult hitResult) {
        if(ModChecker.potionOfBeesPresent) {
            ThrownEntity thrownEntity = (ThrownEntity)(Object)this;
            if(!thrownEntity.getEntityWorld().isClient()){
                if(PotionOfBeesRedirection.POBReviveLarvaBlockEvent(thrownEntity, hitResult.getPos())){
                    thrownEntity.kill();
                    ci.cancel();
                }
            }
        }
    }
}