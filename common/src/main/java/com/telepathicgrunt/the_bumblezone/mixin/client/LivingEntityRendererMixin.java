package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.telepathicgrunt.the_bumblezone.effects.ParalyzedEffect;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> {

    @ModifyReturnValue(method = "isShaking(Lnet/minecraft/world/entity/LivingEntity;)Z",
            at = @At(value = "RETURN"),
            require = 0)
    private boolean bumblezone$shakeForParalysis(boolean isShaking, T entity) {
        if (!isShaking && ParalyzedEffect.isParalyzedClient(entity)) {
            return true;
        }
        return isShaking;
    }
}