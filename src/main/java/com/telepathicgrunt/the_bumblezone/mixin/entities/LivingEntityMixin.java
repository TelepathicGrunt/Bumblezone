package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.effects.ParalyzedEffect;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluid;
import com.telepathicgrunt.the_bumblezone.tags.BzFluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    //bees become angrier when hit in bumblezone
    @Inject(method = "actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V",
            at = @At(value = "HEAD"))
    private void thebumblezone_onEntityDamaged(DamageSource source, float amount, CallbackInfo ci) {
        //Bumblezone.LOGGER.log(Level.INFO, "started");
        BeeAggression.beeHitAndAngered(((LivingEntity)(Object)this), source.getEntity());
    }

    //clear the wrath effect from all bees if they killed their target
    @Inject(method = "die(Lnet/minecraft/world/damagesource/DamageSource;)V",
            at = @At(value = "HEAD"))
    private void thebumblezone_onDeath(DamageSource source, CallbackInfo ci) {
        WrathOfTheHiveEffect.calmTheBees(this.level, (LivingEntity)(Object)this);
    }

    @Inject(method = "isImmobile()Z",
            at = @At(value = "HEAD"), cancellable = true)
    private void thebumblezone_isParalyzedCheck(CallbackInfoReturnable<Boolean> cir) {
        if(ParalyzedEffect.isParalyzed((LivingEntity)(Object)this)) {
            cir.setReturnValue(true);
        }
    }

    //-----------------------------------------------------------//

    //deplete air supply
    @Inject(method = "baseTick()V",
            at = @At(value = "TAIL"))
    private void thebumblezone_breathing(CallbackInfo ci) {
        HoneyFluid.breathing((LivingEntity)(Object)this);
    }

    // make jumping in honey weaker
    @ModifyVariable(method = "aiStep()V", ordinal = 0,
            at = @At(value = "INVOKE_ASSIGN", target = "net/minecraft/world/entity/LivingEntity.getFluidHeight(Lnet/minecraft/tags/Tag;)D", ordinal = 1),
            slice = @Slice(
                    from = @At(value = "INVOKE_ASSIGN", target = "net/minecraft/world/entity/LivingEntity.isAffectedByFluids()Z"),
                    to = @At(value = "INVOKE", target = "net/minecraft/world/entity/LivingEntity.isInWater()Z")
            ))
    private double thebumblezone_honeyFluidJump(double fluidHeight) {
        if(fluidHeight == 0) {
            return this.getFluidHeight(BzFluidTags.BZ_HONEY_FLUID);
        }
        return fluidHeight;
    }
}