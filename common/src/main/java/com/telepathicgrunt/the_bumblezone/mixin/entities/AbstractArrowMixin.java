package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.enchantments.PotentPoisonEnchantmentApplication;
import com.telepathicgrunt.the_bumblezone.modinit.BzEnchantments;
import com.telepathicgrunt.the_bumblezone.utils.EnchantmentUtils;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {

    @Shadow protected abstract ItemStack getPickupItem();

    @Inject(method = "doPostHurtEffects(Lnet/minecraft/world/entity/LivingEntity;)V",
            at = @At(value = "HEAD"))
    private void bumblezone$enchantmentEffects(LivingEntity livingEntity, CallbackInfo ci) {
        ItemStack pickupItem = getPickupItem();
        PotentPoisonEnchantmentApplication.doPostAttack(pickupItem, livingEntity);
    }
}