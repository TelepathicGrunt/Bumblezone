package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.telepathicgrunt.the_bumblezone.effects.ParalyzedEffect;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Shadow
    public boolean hasEffect(MobEffect mobEffect) {
        return false;
    }

    @Shadow
    public double getAttributeValue(Attribute attribute) {
        return 0;
    }

    @ModifyReturnValue(method = "isImmobile()Z",
            at = @At(value = "RETURN"))
    private boolean bumblezone$isParalyzedCheck(boolean isImmobile) {
        if(!isImmobile && ParalyzedEffect.isParalyzed((LivingEntity)(Object)this)) {
            return true;
        }
        return isImmobile;
    }

    @ModifyReturnValue(method = "getFlyingSpeed()F",
            at = @At(value = "RETURN"))
    private float bumblezone$flyingSpeedBeenergized(float flyingSpeed) {
        if(hasEffect(BzEffects.BEENERGIZED.get())) {
            return ((float) (getAttributeValue(Attributes.FLYING_SPEED) / Attributes.FLYING_SPEED.getDefaultValue()) * flyingSpeed);
        }
        return flyingSpeed;
    }
}