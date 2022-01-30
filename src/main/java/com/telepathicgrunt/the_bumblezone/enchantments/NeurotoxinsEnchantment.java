package com.telepathicgrunt.the_bumblezone.enchantments;

import com.telepathicgrunt.the_bumblezone.items.StingerSpearItem;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class NeurotoxinsEnchantment extends Enchantment {

    public NeurotoxinsEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.TRIDENT, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinCost(int level) {
        return 10 * level;
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
        return stack.getItem() instanceof StingerSpearItem;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return this.canEnchant(stack);
    }

    @Override
    public void doPostAttack(LivingEntity attacker, Entity victim, int level) {
        if(victim instanceof LivingEntity livingEntity && livingEntity.getHealth() < 80) {
            livingEntity.addEffect(new MobEffectInstance(
                    BzEffects.PARALYZED.get(),
                    100 * level,
                    level,
                    false,
                    true,
                    true));
        }
    }
}
