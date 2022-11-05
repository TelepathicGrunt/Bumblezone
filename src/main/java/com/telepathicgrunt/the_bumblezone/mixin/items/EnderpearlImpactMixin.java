package com.telepathicgrunt.the_bumblezone.mixin.items;

import com.telepathicgrunt.the_bumblezone.modcompat.DreamlandBiomesCompat;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ProductiveBeesCompat;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownEnderpearl.class)
public class EnderpearlImpactMixin {

    // Teleports player to Bumblezone when pearl hits bee nest-like entities
    @Inject(method = "onHit",
            at = @At(value = "HEAD"),
            cancellable = true,
            require = 0)
    private void thebumblezone_onPearlHit(HitResult hitResult, CallbackInfo ci) {
        if (ModChecker.productiveBeesPresent) {
            if (ProductiveBeesCompat.runTeleportCodeIfBeeHelmentHitHigh(hitResult, ((ThrownEnderpearl) (Object) this))) {
                ci.cancel();
            }
        }
        if (ModChecker.dreamlandBiomesPresent) {
            if (DreamlandBiomesCompat.runTeleportCodeIfBumbleBeastHitHigh(hitResult, ((ThrownEnderpearl) (Object) this))) {
                ci.cancel();
            }
        }
    }
}