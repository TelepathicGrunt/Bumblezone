package com.telepathicgrunt.the_bumblezone.enchantments;

import com.telepathicgrunt.the_bumblezone.items.StingerSpearItem;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.platform.BzEnchantment;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class PotentPoisonEnchantment extends BzEnchantment {

    public PotentPoisonEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.TRIDENT, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinCost(int level) {
        return 8 + level * 5;
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

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.is(BzTags.ENCHANTABLE_POTENT_POISON) || stack.is(Items.BOOK);
    }

    @Override
    public OptionalBoolean bz$canApplyAtEnchantingTable(ItemStack stack) {
        return OptionalBoolean.of(this.canEnchant(stack));
    }
}
