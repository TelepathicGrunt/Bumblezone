package com.telepathicgrunt.the_bumblezone.enchantments;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.capabilities.BzCapabilities;
import com.telepathicgrunt.the_bumblezone.capabilities.NeurotoxinsMissCounter;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.ThrownStingerSpearEntity;
import com.telepathicgrunt.the_bumblezone.items.StingerSpearItem;
import com.telepathicgrunt.the_bumblezone.mixin.entities.ThrownTridentAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzEnchantments;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

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

    public static void entityHurtEvent(LivingAttackEvent event) {
        if(event.getEntity().level.isClientSide()) {
            return;
        }

        ItemStack attackingItem = null;
        LivingEntity attacker = null;
        if(event.getSource().getEntity() instanceof LivingEntity livingEntity) {
            attacker = livingEntity;
            attackingItem = attacker.getMainHandItem();
        }
        if(event.getSource().isProjectile()) {
           Entity projectile = event.getSource().getDirectEntity();
           if(projectile instanceof ThrownTrident thrownTrident) {
               attackingItem = ((ThrownTridentAccessor)thrownTrident).getTridentItem();
           }
           else if (projectile instanceof ThrownStingerSpearEntity thrownStingerSpearEntity) {
               attackingItem = thrownStingerSpearEntity.getSpearItemStack();
           }
        }

        if(attackingItem != null && !attackingItem.isEmpty()) {
            applyNeurotoxins(attacker, event.getEntityLiving(), attackingItem);
        }
    }

    public static void applyNeurotoxins(Entity attacker, Entity victim, ItemStack itemStack) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(BzEnchantments.NEUROTOXINS, itemStack);

        if(level > 0 && victim instanceof LivingEntity livingEntity && livingEntity.getMobType() != MobType.UNDEAD) {
            float applyChance = 1.0f;
            boolean isAttackerNull = attacker == null;
            int missedCounter = 0;

            if(!isAttackerNull) {
                missedCounter = Bumblezone.NEUROTOXINS_MISSED_COUNTER_COMPONENT.get(attacker).getMissedCounter();
                float healthModifier = Math.max(100 - livingEntity.getHealth(), 10) / 100f;
                applyChance = (healthModifier * level) * (missedCounter + 1);
            }

            if(victim.level.random.nextFloat() < applyChance) {
                livingEntity.addEffect(new MobEffectInstance(
                        BzEffects.PARALYZED,
                        100 * level,
                        level,
                        false,
                        true,
                        true));

                if (itemStack.getItem() == BzItems.STINGER_SPEAR && attacker instanceof ServerPlayer serverPlayer) {
                    BzCriterias.STINGER_SPEAR_PARALYZING_TRIGGER.trigger(serverPlayer);

                    if (livingEntity.getHealth() > 70) {
                        BzCriterias.STINGER_SPEAR_PARALYZE_BOSS_TRIGGER.trigger(serverPlayer);
                    }
                }

                if(!isAttackerNull) {
                    Bumblezone.NEUROTOXINS_MISSED_COUNTER_COMPONENT.get(attacker).setMissedCounter(0);
                }
            }
            else {
                if(!isAttackerNull) {
                    Bumblezone.NEUROTOXINS_MISSED_COUNTER_COMPONENT.get(attacker).setMissedCounter(missedCounter + 1);
                }
            }
        }
    }
}
