package com.telepathicgrunt.the_bumblezone.mixin.items;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ClearAllStatusEffectsConsumeEffect.class)
public class ClearAllStatusEffectsConsumeEffectMixin {

    @WrapOperation(method = "apply(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;removeAllEffects()Z", ordinal = 0))
    private boolean bumblezone$doNotClearWrath(LivingEntity livingEntity, Operation<Boolean> original)
    {
        MobEffectInstance wrathEffect = livingEntity.getEffect(BzEffects.WRATH_OF_THE_HIVE.holder());

        // Only wrath is active, do nothing
        if (wrathEffect != null && livingEntity.getActiveEffects().size() == 1) {
            return false;
        }

        // run vanilla clearing
        boolean operationResult = original.call(livingEntity);

        // re-add wrath
        if (wrathEffect != null) {
            livingEntity.addEffect(wrathEffect);
        }

        return operationResult;
    }
}