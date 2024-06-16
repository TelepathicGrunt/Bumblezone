package com.telepathicgrunt.the_bumblezone.enchantments;

import com.telepathicgrunt.the_bumblezone.modinit.BzEnchantments;
import com.telepathicgrunt.the_bumblezone.utils.EnchantmentUtils;
import net.minecraft.core.Holder;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public class PotentPoisonEnchantmentApplication {

    public static int getPotentPoisonEnchantLevel(ItemStack stack, Level level) {
        Holder<Enchantment> potentPoison = EnchantmentUtils.getEnchantmentHolder(BzEnchantments.POTENT_POISON, level);
        return EnchantmentHelper.getItemEnchantmentLevel(potentPoison, stack);
    }

    public static void doPostAttack(ItemStack enchantedItem, Entity victim) {
        if (victim instanceof LivingEntity livingEntity && !livingEntity.getType().is(EntityTypeTags.UNDEAD)) {
            int enchantmentLevel = PotentPoisonEnchantmentApplication.getPotentPoisonEnchantLevel(enchantedItem, victim.level());
            if (enchantmentLevel <= 0) {
                return;
            }

            livingEntity.addEffect(new MobEffectInstance(
                    MobEffects.POISON,
                    100 + 100 * (enchantmentLevel - ((enchantmentLevel - 1) / 2)), // 200, 300, 300 duration
                    ((enchantmentLevel - 1) / 2), // 0, 0, 1 level poison
                    false,
                    true,
                    true));
        }
    }

    public static void doPostAttackBoostedPoison(ItemStack enchantedItem, Entity victim) {
        int enchantmentLevel = PotentPoisonEnchantmentApplication.getPotentPoisonEnchantLevel(enchantedItem, victim.level());
        if (enchantmentLevel <= 0) {
            return;
        }

        if(victim instanceof LivingEntity livingEntity && !livingEntity.getType().is(EntityTypeTags.UNDEAD)) {
            livingEntity.addEffect(new MobEffectInstance(
                    MobEffects.POISON,
                    100 + 100 * (enchantmentLevel - ((enchantmentLevel - 1) / 2)), // 200, 300, 300 duration
                    enchantmentLevel, // 0, 1, 2, 3 level poison
                    false,
                    true,
                    true));
        }
    }
}
