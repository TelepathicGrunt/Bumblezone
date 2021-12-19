package com.telepathicgrunt.the_bumblezone.mixin.items;

import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationHookup;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownEnderpearl.class)
public class EnderpearlImpactMixin {

    // Teleports player to Bumblezone when pearl hits bee nest
    @Inject(method = "onHit",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void thebumblezone_onPearlHit(HitResult hitResult, CallbackInfo ci) {
        if(EntityTeleportationHookup.runEnderpearlImpact(hitResult, ((ThrownEnderpearl) (Object) this))) ci.cancel();
    }
}