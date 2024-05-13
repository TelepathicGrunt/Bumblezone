package com.telepathicgrunt.the_bumblezone.enchantments;

import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;

public class PotentPoisonEnchantment extends Enchantment {

    public PotentPoisonEnchantment() {
        super(Enchantment.definition(
                BzTags.POTENT_POISON_ENCHANTABLE,
                4,
                3,
                Enchantment.dynamicCost(8, 5),
                Enchantment.constantCost(50),
                3,
                EquipmentSlot.MAINHAND)
        );
    }

    @Override
    public void doPostAttack(LivingEntity attacker, Entity victim, int level) {
        if(victim instanceof LivingEntity livingEntity && !livingEntity.getType().is(EntityTypeTags.UNDEAD)) {
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
