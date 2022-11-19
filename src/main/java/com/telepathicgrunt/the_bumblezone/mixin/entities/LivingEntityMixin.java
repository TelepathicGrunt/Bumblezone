package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.telepathicgrunt.the_bumblezone.effects.ParalyzedEffect;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.items.StinglessBeeHelmet;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
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

    @ModifyReturnValue(method = "isImmobile()Z",
            at = @At(value = "RETURN"))
    private boolean thebumblezone_isParalyzedCheck(boolean isImmobile) {
        if(!isImmobile && ParalyzedEffect.isParalyzed((LivingEntity)(Object)this)) {
            return true;
        }
        return isImmobile;
    }

    //-----------------------------------------------------------//

    // make jumping in honey and sugar water weaker
    @WrapOperation(method = "aiStep()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getFluidHeight(Lnet/minecraft/tags/TagKey;)D", ordinal = 1),
            require = 0)
    private double thebumblezone_customFluidJumpWeaker(LivingEntity livingEntity, TagKey<Fluid> tagKey, Operation<Double> original) {
        double newFluidHeight = this.getFluidTypeHeight(BzFluids.HONEY_FLUID_TYPE.get());
        if(newFluidHeight > 0) {
            return newFluidHeight;
        }
        newFluidHeight = this.getFluidTypeHeight(BzFluids.ROYAL_JELLY_FLUID_TYPE.get());
        if(newFluidHeight > 0) {
            return newFluidHeight;
        }
        newFluidHeight = this.getFluidTypeHeight(BzFluids.SUGAR_WATER_FLUID_TYPE.get());
        if(newFluidHeight > 0) {
            return newFluidHeight;
        }
        return original.call(livingEntity, tagKey);
    }
}