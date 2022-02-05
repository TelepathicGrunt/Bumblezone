package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.telepathicgrunt.the_bumblezone.effects.ParalyzedEffect;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> {

    @Inject(method = "isShaking(Lnet/minecraft/world/entity/LivingEntity;)Z",
            at = @At(value = "HEAD"), cancellable = true)
    private void thebumblezone_shakeForParalysis(T entity, CallbackInfoReturnable<Boolean> cir) {
        if (ParalyzedEffect.isParalyzedClient(entity)) {
            cir.setReturnValue(true);
        }
    }
}