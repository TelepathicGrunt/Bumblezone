package com.telepathicgrunt.the_bumblezone.enchantments;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class PotentPoisonEnchantment extends Enchantment {

    public PotentPoisonEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.TRIDENT, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinCost(int level) {
        return 6 + level * 5;
    }

    @Override
    public int getMaxCost(int level) {
        return super.getMinCost(level) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.getItem() instanceof TridentItem;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return this.canEnchant(stack);
    }

    @Override
    public void doPostAttack(LivingEntity attacker, Entity victim, int level) {
        if(victim instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(
                    MobEffects.POISON,
                    200,
                    level - 1, // 0-2 level poison
                    true,
                    true,
                    true));
        }
    }
}
