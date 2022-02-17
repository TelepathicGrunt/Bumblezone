package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.items.TickingArmorItem;
import com.telepathicgrunt.the_bumblezone.modinit.BzEnchantments;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {

    @Inject(method = "doPostHurtEffects(Lnet/minecraft/world/entity/LivingEntity;)V",
            at = @At(value = "HEAD"))
    private void thebumblezone_enchantmentEffects(LivingEntity livingEntity, CallbackInfo ci) {
        if (((AbstractArrow)(Object)this) instanceof ThrownTrident thrownTrident) {
            ItemStack tridentItem = ((ThrownTridentAccessor)thrownTrident).getTridentItem();
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(tridentItem);
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                if (entry.getKey() == BzEnchantments.POTENT_POISON && entry.getValue() > 0) {
                    entry.getKey().doPostAttack(
                            thrownTrident.getOwner() instanceof LivingEntity livingEntity1 ? livingEntity1 : null,
                            livingEntity,
                            entry.getValue());
                }
            }
        }
    }
}