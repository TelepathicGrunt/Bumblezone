package com.telepathicgrunt.the_bumblezone.enchantments;

import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityAttackedEvent;
import com.telepathicgrunt.the_bumblezone.mixin.entities.AbstractArrowAccessor;
import com.telepathicgrunt.the_bumblezone.mixin.entities.MobAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzEnchantments;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.modules.LivingEntityDataModule;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHelper;
import com.telepathicgrunt.the_bumblezone.modules.registry.ModuleRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Optional;

public class NeurotoxinsEnchantment extends Enchantment {

    public NeurotoxinsEnchantment() {
        super(Enchantment.definition(
                BzTags.NEUROTOXIN_ENCHANTABLE,
                1,
                BzGeneralConfigs.neurotoxinMaxLevel,
                Enchantment.dynamicCost(14, 3),
                Enchantment.constantCost(50),
                6,
                EquipmentSlot.MAINHAND)
        );
    }

    public static void entityHurtEvent(EntityAttackedEvent event) {
        if(event.entity().level().isClientSide()) {
            return;
        }

        ItemStack attackingItem = null;
        LivingEntity attacker = null;
        if(event.source().getEntity() instanceof LivingEntity livingEntity) {
            attacker = livingEntity;
            attackingItem = attacker.getMainHandItem();
        }

        if(event.source().is(DamageTypeTags.IS_PROJECTILE)) {
           Entity projectile = event.source().getDirectEntity();
           if(projectile instanceof AbstractArrow abstractArrow) {
               attackingItem = ((AbstractArrowAccessor)abstractArrow).callGetPickupItem();
           }
        }

        if(attackingItem != null && !attackingItem.isEmpty()) {
            applyNeurotoxins(attacker, event.entity(), attackingItem);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static void applyNeurotoxins(Entity attacker, Entity victim, ItemStack itemStack) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(BzEnchantments.NEUROTOXINS.get(), itemStack);
        level = Math.min(level, BzGeneralConfigs.neurotoxinMaxLevel);

        if(level > 0 && victim instanceof LivingEntity livingEntity && !livingEntity.getType().is(EntityTypeTags.UNDEAD)) {
            if (livingEntity.hasEffect(BzEffects.PARALYZED.holder())) {
                return;
            }

            float applyChance = 1.0f;
            LivingEntityDataModule capability = null;

            if(attacker != null) {
                Optional<LivingEntityDataModule> capOptional = ModuleHelper.getModule(attacker, ModuleRegistry.LIVING_ENTITY_DATA);
                if (capOptional.isPresent()) {
                    capability = capOptional.orElseThrow(RuntimeException::new);
                    float healthModifier = Math.max(100 - livingEntity.getHealth(), 10) / 100f;
                    applyChance = (healthModifier * level) * (capability.getMissedParalysis() + 1);
                }
            }

            if(livingEntity.getRandom().nextFloat() < applyChance) {
                livingEntity.addEffect(new MobEffectInstance(
                        BzEffects.PARALYZED.holder(),
                        Math.min(100 * level, BzGeneralConfigs.paralyzedMaxTickDuration),
                        level,
                        false,
                        true,
                        true));

                if (attacker instanceof LivingEntity livingAttacker && victim instanceof Mob mob) {
                    mob.setLastHurtByMob(livingAttacker);
                    ((MobAccessor)mob).getTargetSelector().tick();
                }

                if (itemStack.is(BzItems.STINGER_SPEAR.get()) && attacker instanceof ServerPlayer serverPlayer) {
                    BzCriterias.STINGER_SPEAR_PARALYZING_TRIGGER.get().trigger(serverPlayer);

                    if (livingEntity.getHealth() > 70) {
                        BzCriterias.STINGER_SPEAR_PARALYZE_BOSS_TRIGGER.get().trigger(serverPlayer);
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
