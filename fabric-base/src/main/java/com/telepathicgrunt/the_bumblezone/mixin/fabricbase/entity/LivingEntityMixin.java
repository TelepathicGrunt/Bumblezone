package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityAttackedEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityDeathEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityHurtEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityTickEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityVisibilityEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.FinishUseItemEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    public abstract int getUseItemRemainingTicks();

    @ModifyReturnValue(
            method = "getVisibilityPercent",
            at = @At(value = "RETURN")
    )
    private double bumblezone$onEntityVisibility(double visibility, @Nullable Entity entity) {
        EntityVisibilityEvent event = new EntityVisibilityEvent(visibility, (LivingEntity) ((Object) this), entity);
        EntityVisibilityEvent.EVENT.invoke(event);
        return event.visibility();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void bumblezone$onTick(CallbackInfo ci) {
        EntityTickEvent.EVENT.invoke(new EntityTickEvent((LivingEntity) ((Object) this)));
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void bumblezone$onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (EntityAttackedEvent.EVENT.invoke(new EntityAttackedEvent((LivingEntity) ((Object) this), source, amount))) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    private void bumblezone$onDeath(DamageSource source, CallbackInfo ci) {
        if (EntityDeathEvent.EVENT.invoke(new EntityDeathEvent((LivingEntity) ((Object) this), source))) {
            ci.cancel();
        } else if (EntityDeathEvent.EVENT_LOWEST.invoke(new EntityDeathEvent((LivingEntity) ((Object) this), source))) {
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
        if (EntityHurtEvent.EVENT_LOWEST.invoke(new EntityHurtEvent((LivingEntity) ((Object) this), source, amount))) {
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

    @Inject(method = "baseTick()V", at = @At(value = "TAIL"))
    private void bumblezone$breathing(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        boolean invulnerable = livingEntity instanceof Player && ((Player) livingEntity).getAbilities().invulnerable;
        if (livingEntity.isAlive()) {
            if (livingEntity.isEyeInFluid(BzTags.SPECIAL_HONEY_LIKE)) {
                if (!livingEntity.canBreatheUnderwater() && !MobEffectUtil.hasWaterBreathing(livingEntity) && !invulnerable) {
                    int respiration = EnchantmentHelper.getRespiration(livingEntity);
                    livingEntity.setAirSupply(
                            respiration > 0 && livingEntity.level.random.nextInt(respiration + 1) > 0 ?
                                    livingEntity.getAirSupply() - 4 :
                                    livingEntity.getAirSupply() - 5
                    );
                    if (livingEntity.getAirSupply() == -20) {
                        livingEntity.setAirSupply(0);
                        Vec3 vector3d = livingEntity.getDeltaMovement();
                        SimpleParticleType simpleParticleType = livingEntity.isEyeInFluid(BzTags.BZ_HONEY_FLUID) ? BzParticles.HONEY_PARTICLE.get() : BzParticles.ROYAL_JELLY_PARTICLE.get();

                        for (int i = 0; i < 8; ++i) {
                            double d2 = livingEntity.getRandom().nextDouble() - livingEntity.getRandom().nextDouble();
                            double d3 = livingEntity.getRandom().nextDouble() - livingEntity.getRandom().nextDouble();
                            double d4 = livingEntity.getRandom().nextDouble() - livingEntity.getRandom().nextDouble();
                            livingEntity.level.addParticle(simpleParticleType, livingEntity.getX() + d2, livingEntity.getY() + d3, livingEntity.getZ() + d4, vector3d.x, vector3d.y, vector3d.z);
                        }

                        livingEntity.hurt(livingEntity.level.damageSources().drown(), 2.0F);
                    }
                }

                if (!livingEntity.level.isClientSide() && livingEntity.isPassenger() && livingEntity.getVehicle() != null && livingEntity.getVehicle().dismountsUnderwater()) {
                    livingEntity.stopRiding();
                }
            }
        }
    }

    @ModifyReturnValue(method = "getEquipmentSlotForItem",
            at = @At(value = "RETURN"))
    private static EquipmentSlot bumblezone$correctSlotForItems(EquipmentSlot equipmentSlot, ItemStack stack) {
        if(stack.getItem() instanceof ItemExtension extension) {
            return extension.bz$getEquipmentSlot(stack);
        }
        return equipmentSlot;
    }

    // make jumping in honey and sugar water weaker
    @WrapOperation(method = "aiStep()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getFluidHeight(Lnet/minecraft/tags/TagKey;)D", ordinal = 1),
            require = 0)
    private double bumblezone$customFluidJumpWeaker(LivingEntity livingEntity, TagKey<Fluid> tagKey, Operation<Double> original) {
        double newFluidHeight = PlatformHooks.getFluidHeight(livingEntity, BzTags.SPECIAL_HONEY_LIKE, BzFluids.HONEY_FLUID_TYPE.get(), BzFluids.ROYAL_JELLY_FLUID_TYPE.get());
        if(newFluidHeight > 0) {
            return newFluidHeight;
        }
        newFluidHeight = PlatformHooks.getFluidHeight(livingEntity, BzTags.SUGAR_WATER_FLUID, BzFluids.SUGAR_WATER_FLUID_TYPE.get());
        if(newFluidHeight > 0) {
            return newFluidHeight;
        }
        return original.call(livingEntity, tagKey);
    }
}
