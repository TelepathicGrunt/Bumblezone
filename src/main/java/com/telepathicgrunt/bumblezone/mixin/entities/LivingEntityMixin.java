package com.telepathicgrunt.bumblezone.mixin.entities;

import com.telepathicgrunt.bumblezone.effects.WrathOfTheHiveEffect;
import com.telepathicgrunt.bumblezone.entities.BeeAggression;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    //bees attacks bear mobs that is in the dimension
    @Inject(method = "applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V",
            at = @At(value = "HEAD"))
    private void onEntityDamaged(DamageSource source, float amount, CallbackInfo ci) {
        //Bumblezone.LOGGER.log(Level.INFO, "started");
        BeeAggression.beeHitAndAngered(((LivingEntity)(Object)this), source.getAttacker());
    }

    //clear the wrath effect from all bees if they killed their target
    @Inject(method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V",
            at = @At(value = "HEAD"))
    private void onDeath(DamageSource source, CallbackInfo ci) {
        WrathOfTheHiveEffect.calmTheBees(((LivingEntity)(Object)this).world, (LivingEntity)(Object)this);
    }
}