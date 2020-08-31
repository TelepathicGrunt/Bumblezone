package com.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import com.telepathicgrunt.bumblezone.entities.BeeAggression;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class MobHitMixin {
    //bees attacks bear mobs that is in the dimension
    @Inject(method = "damageEntity(Lnet/minecraft/util/DamageSource;F)V",
            at = @At(value = "HEAD"))
    private void onEntityDamaged(DamageSource source, float amount, CallbackInfo ci) {
        //Bumblezone.LOGGER.log(Level.INFO, "started");
        BeeAggression.beeHitAndAngered(((LivingEntity)(Object)this), source.getTrueSource());
    }

}