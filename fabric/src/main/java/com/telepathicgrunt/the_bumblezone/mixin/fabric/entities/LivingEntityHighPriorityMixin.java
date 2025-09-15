package com.telepathicgrunt.the_bumblezone.mixin.fabric.entities;

import com.telepathicgrunt.the_bumblezone.events.entity.BzEntityAttackedEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class)
public abstract class LivingEntityHighPriorityMixin {
    @Inject(method = "hurtServer", at = @At("HEAD"), order = 1001, cancellable = true)
    private void bumblezone$onHurt(ServerLevel serverLevel, DamageSource damageSource, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (BzEntityAttackedEvent.EVENT_HIGH.invoke(new BzEntityAttackedEvent((LivingEntity) ((Object) this), damageSource, amount))) {
            cir.setReturnValue(false);
        }
    }
}
