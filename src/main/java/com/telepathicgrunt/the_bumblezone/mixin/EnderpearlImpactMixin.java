package com.telepathicgrunt.the_bumblezone.mixin;

import com.telepathicgrunt.the_bumblezone.entities.PlayerTeleportation;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.util.math.RayTraceResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderPearlEntity.class)
public class EnderpearlImpactMixin {

    // Teleports player to Bumblezone when pearl hits bee nest
    @Inject(method = "onImpact(Lnet/minecraft/util/math/RayTraceResult;)V",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void onPearlHit(RayTraceResult hitResult, CallbackInfo ci) {
        if(PlayerTeleportation.runEnderpearlImpact(hitResult, ((EnderPearlEntity) (Object) this))) ci.cancel();
    }
}