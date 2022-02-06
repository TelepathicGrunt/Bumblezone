package com.telepathicgrunt.the_bumblezone.enchantments;

import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
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
        return 3;
    }

    @Override
    public void doPostAttack(LivingEntity attacker, Entity victim, int level) {
        if(victim instanceof LivingEntity livingEntity && livingEntity.getMobType() != MobType.UNDEAD) {
            livingEntity.addEffect(new MobEffectInstance(
                    MobEffects.POISON,
                    100 + 100 * (level - ((level - 1) / 2)), // 200, 300, 300 duration
                    ((level - 1) / 2), // 0, 0, 1 level poison
                    true,
                    true,
                    true));
        }
    }
}
