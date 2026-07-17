package com.telepathicgrunt.the_bumblezone.mixin.fabric.entity;

import com.telepathicgrunt.the_bumblezone.events.entity.EntityAttackedEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class, priority = 1001)
public abstract class LivingEntityHighPriorityMixin {
    @Inject(method = "hurt", at = @At("HEAD"),cancellable = true)
    private void bumblezone$onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (EntityAttackedEvent.EVENT_HIGH.invoke(new EntityAttackedEvent((LivingEntity) ((Object) this), source, amount))) {
            cir.setReturnValue(false);
        }
    }
}
