package com.telepathicgrunt.the_bumblezone.enchantments;

import com.telepathicgrunt.the_bumblezone.entities.nonliving.ThrownStingerSpearEntity;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityAttackedEvent;
import com.telepathicgrunt.the_bumblezone.items.StingerSpearItem;
import com.telepathicgrunt.the_bumblezone.mixin.entities.ThrownTridentAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzEnchantments;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modules.NeurotoxinsMissedCounterModule;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHelper;
import com.telepathicgrunt.the_bumblezone.modules.registry.ModuleRegistry;
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

import java.util.Optional;

public class NeurotoxinsEnchantment extends Enchantment {

    public NeurotoxinsEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.TRIDENT, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinCost(int level) {
        return 14 * level;
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

    public static void entityHurtEvent(EntityAttackedEvent event) {
        if(event.entity().level.isClientSide()) {
            return;
        }

        ItemStack attackingItem = null;
        LivingEntity attacker = null;
        if(event.source().getEntity() instanceof LivingEntity livingEntity) {
            attacker = livingEntity;
            attackingItem = attacker.getMainHandItem();
        }
        if(event.source().isProjectile()) {
           Entity projectile = event.source().getDirectEntity();
           if(projectile instanceof ThrownTrident thrownTrident) {
               attackingItem = ((ThrownTridentAccessor)thrownTrident).getTridentItem();
           }
           else if (projectile instanceof ThrownStingerSpearEntity thrownStingerSpearEntity) {
               attackingItem = thrownStingerSpearEntity.getSpearItemStack();
           }
        }

        if(attackingItem != null && !attackingItem.isEmpty()) {
            applyNeurotoxins(attacker, event.entity(), attackingItem);
        }
    }

    public static void applyNeurotoxins(Entity attacker, Entity victim, ItemStack itemStack) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(BzEnchantments.NEUROTOXINS.get(), itemStack);

        if(level > 0 && victim instanceof LivingEntity livingEntity && livingEntity.getMobType() != MobType.UNDEAD) {
            if (livingEntity.hasEffect(BzEffects.PARALYZED.get())) {
                return;
            }

            float applyChance = 1.0f;
            NeurotoxinsMissedCounterModule capability = null;

            if(attacker != null) {
                Optional<NeurotoxinsMissedCounterModule> capOptional = ModuleHelper.getModule(attacker, ModuleRegistry.NEUROTOXINS_MISSED);
                if (capOptional.isPresent()) {
                    capability = capOptional.orElseThrow(RuntimeException::new);
                    float healthModifier = Math.max(100 - livingEntity.getHealth(), 10) / 100f;
                    applyChance = (healthModifier * level) * (capability.getMissedParalysis() + 1);
                }
            }

            if(livingEntity.getRandom().nextFloat() < applyChance) {
                livingEntity.addEffect(new MobEffectInstance(
                        BzEffects.PARALYZED.get(),
                        100 * level,
                        level,
                        false,
                        true,
                        true));

                if (itemStack.getItem() == BzItems.STINGER_SPEAR.get() && attacker instanceof ServerPlayer serverPlayer) {
                    BzCriterias.STINGER_SPEAR_PARALYZING_TRIGGER.trigger(serverPlayer);

                    if (livingEntity.getHealth() > 70) {
                        BzCriterias.STINGER_SPEAR_PARALYZE_BOSS_TRIGGER.trigger(serverPlayer);
                    }
                }

                if(capability != null) {
                    capability.setMissedParalysis(0);
                }
            }
            else {
                if(capability != null) {
                    capability.setMissedParalysis(capability.getMissedParalysis() + 1);
                }
            }
        }
    }
}
