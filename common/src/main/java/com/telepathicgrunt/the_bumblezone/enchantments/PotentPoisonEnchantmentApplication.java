package com.telepathicgrunt.the_bumblezone.enchantments;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.enchantments.datacomponents.PoisonMarker;
import com.telepathicgrunt.the_bumblezone.modinit.BzEnchantments;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class PotentPoisonEnchantmentApplication {

    public static Pair<PoisonMarker, Integer> getPotentPoisonEnchant(ItemStack stack) {
        return EnchantmentHelper.getHighestLevel(stack, BzEnchantments.POISON_MARKER.get());
    }

    public static void doPostAttack(ItemStack enchantedItem, LivingEntity victim) {
        if (!victim.getType().is(EntityTypeTags.UNDEAD)) {
            Pair<PoisonMarker, Integer> enchantmentAndLevel = PotentPoisonEnchantmentApplication.getPotentPoisonEnchant(enchantedItem);
            if (enchantmentAndLevel == null || enchantmentAndLevel.getSecond() <= 0) {
                return;
            }

            PoisonMarker poisonMarker = enchantmentAndLevel.getFirst();
            victim.addEffect(new MobEffectInstance(
                    MobEffects.POISON,
                    100 + (poisonMarker.bonusDurationAmount() * ((enchantmentAndLevel.getSecond() + 1) / poisonMarker.enchantLevelIntervalForBonusDuration())), // 200, 300, 300 duration
                    (int)(poisonMarker.poisonLevelPerEnchantLevel() * enchantmentAndLevel.getSecond()), // 0, 0, 1 level poison
                    false,
                    true,
                    true));
        }
    }

    public static boolean doPostAttackBoostedPoison(ItemStack enchantedItem, LivingEntity victim) {
        Pair<PoisonMarker, Integer> enchantmentAndLevel = PotentPoisonEnchantmentApplication.getPotentPoisonEnchant(enchantedItem);
        if (enchantmentAndLevel == null || enchantmentAndLevel.getSecond() <= 0) {
            return false;
        }

        PoisonMarker poisonMarker = enchantmentAndLevel.getFirst();
        if (!victim.getType().is(EntityTypeTags.UNDEAD)) {
            victim.addEffect(new MobEffectInstance(
                    MobEffects.POISON,
                    100 + (poisonMarker.bonusDurationAmount() * ((enchantmentAndLevel.getSecond() + 1) / poisonMarker.enchantLevelIntervalForBonusDuration())), // 200, 300, 300 duration
                    (int)(poisonMarker.poisonLevelPerEnchantLevel() * enchantmentAndLevel.getSecond()), // 0, 1, 2, 3 level poison
                    false,
                    true,
                    true));

            return true;
        }

        return false;
    }
}
