package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.modinit.BzEnchantments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {

    @Shadow protected abstract ItemStack getPickupItem();

    @Inject(method = "doPostHurtEffects(Lnet/minecraft/world/entity/LivingEntity;)V",
            at = @At(value = "HEAD"))
    private void bumblezone$enchantmentEffects(LivingEntity livingEntity, CallbackInfo ci) {
        AbstractArrow abstractArrow = ((AbstractArrow)(Object)this);

        ItemStack pickupItem = getPickupItem();
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(pickupItem);
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            if (entry.getKey() == BzEnchantments.POTENT_POISON.get() && entry.getValue() > 0) {
                entry.getKey().doPostAttack(
                        abstractArrow.getOwner() instanceof LivingEntity livingEntity1 ? livingEntity1 : null,
                        livingEntity,
                        entry.getValue());
            }
        }
    }
}