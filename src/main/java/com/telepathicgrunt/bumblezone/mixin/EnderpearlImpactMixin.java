package com.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import com.telepathicgrunt.bumblezone.entities.PlayerTeleportation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderPearlEntity.class)
public class EnderpearlImpactMixin {

    // Teleports player to Bumblezone when pearl hits bee nest
    @Inject(method = "onEntityHit",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void onPearlHit(EntityRayTraceResult hitResult, CallbackInfo ci) {
        if(PlayerTeleportation.runEnderpearlImpact(hitResult, ((EnderPearlEntity) (Object) this))) ci.cancel();
    }
}