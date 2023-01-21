package com.telepathicgrunt.the_bumblezone.mixins.fabric.entities;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.telepathicgrunt.the_bumblezone.events.entity.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow public abstract int getUseItemRemainingTicks();

    @ModifyReturnValue(
            method = "getVisibilityPercent",
            at = @At(value = "RETURN")
    )
    private double bumblezone$onEntityVisibility(double visibility, @Nullable Entity entity) {
        EntityVisibilityEvent event = new EntityVisibilityEvent(visibility, (LivingEntity) ((Object)this), entity);
        EntityVisibilityEvent.EVENT.invoke(event);
        return event.visibility();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void bumblezone$onTick(CallbackInfo ci) {
        EntityTickEvent.EVENT.invoke(new EntityTickEvent((LivingEntity) ((Object)this)));
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void bumblezone$onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (EntityAttackedEvent.EVENT.invoke(new EntityAttackedEvent((LivingEntity) ((Object)this), source, amount))) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    private void bumblezone$onDeath(DamageSource source, CallbackInfo ci) {
        if (EntityDeathEvent.EVENT.invoke(new EntityDeathEvent((LivingEntity) ((Object)this), source))) {
            ci.cancel();
        } else if (EntityDeathEvent.EVENT_LOWEST.invoke(new EntityDeathEvent((LivingEntity) ((Object)this), source))) {
            ci.cancel();
        }
    }

    @Inject(method = "actuallyHurt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F",
            ordinal = 0
        ),
        cancellable = true
    )
    private void bumblezone$onActualHurt(DamageSource source, float amount, CallbackInfo cir) {
        if (EntityHurtEvent.EVENT_LOWEST.invoke(new EntityHurtEvent((LivingEntity) ((Object)this), source, amount))) {
            cir.cancel();
        }
    }

    @WrapOperation(
            method = "completeUsingItem",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/ItemStack;finishUsingItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;"
            )
    )
    private ItemStack bumblezone$onCompleteUsingItem(ItemStack instance, Level level, LivingEntity livingEntity, Operation<ItemStack> operation) {
        ItemStack copy = instance.copy();
        ItemStack stack = operation.call(instance, level, livingEntity);
        return FinishUseItemEvent.EVENT.invoke(new FinishUseItemEvent((LivingEntity) ((Object) this), copy, getUseItemRemainingTicks()), stack);
    }

}
