package com.telepathicgrunt.the_bumblezone.enchantments;

import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityAttackedEvent;
import com.telepathicgrunt.the_bumblezone.mixin.entities.AbstractArrowAccessor;
import com.telepathicgrunt.the_bumblezone.mixin.entities.MobAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzEnchantments;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modules.LivingEntityDataModule;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHelper;
import com.telepathicgrunt.the_bumblezone.modules.registry.ModuleRegistry;
import com.telepathicgrunt.the_bumblezone.utils.EnchantmentUtils;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class NeurotoxinsEnchantmentApplication {

    public static int getNeurotoxinEnchantLevel(ItemStack stack, Level level) {
        Holder<Enchantment> neurotoxin = EnchantmentUtils.getEnchantmentHolder(BzEnchantments.NEUROTOXINS, level);
        return Math.min(EnchantmentHelper.getItemEnchantmentLevel(neurotoxin, stack), BzGeneralConfigs.neurotoxinMaxLevel);
    }

    public static void entityHurtEvent(EntityAttackedEvent event) {
        if(event.entity() == null || event.entity().level().isClientSide() || event.entity().getType().is(EntityTypeTags.UNDEAD)) {
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
    public static void applyNeurotoxins(Entity attacker, LivingEntity victim, ItemStack itemStack) {
        int neurotoxinEnchantLevel = getNeurotoxinEnchantLevel(itemStack, victim.level());

        if(neurotoxinEnchantLevel > 0) {
            if (victim.hasEffect(BzEffects.PARALYZED.holder())) {
                return;
            }

            float applyChance = 1.0f;
            LivingEntityDataModule capability = null;

            if(attacker != null) {
                Optional<LivingEntityDataModule> capOptional = ModuleHelper.getModule(attacker, ModuleRegistry.LIVING_ENTITY_DATA);
                if (capOptional.isPresent()) {
                    capability = capOptional.orElseThrow(RuntimeException::new);
                    float healthModifier = Math.max(100 - victim.getHealth(), 10) / 100f;
                    applyChance = (healthModifier * neurotoxinEnchantLevel) * (capability.getMissedParalysis() + 1);
                }
            }

            if(victim.getRandom().nextFloat() < applyChance) {
                victim.addEffect(new MobEffectInstance(
                        BzEffects.PARALYZED.holder(),
                        Math.min(100 * neurotoxinEnchantLevel, BzGeneralConfigs.paralyzedMaxTickDuration),
                        neurotoxinEnchantLevel,
                        false,
                        true,
                        true));

                if (attacker instanceof LivingEntity livingAttacker) {
                    victim.setLastHurtByMob(livingAttacker);
                    ((MobAccessor)victim).getTargetSelector().tick();
                }

                if (itemStack.is(BzItems.STINGER_SPEAR.get()) && attacker instanceof ServerPlayer serverPlayer) {
                    BzCriterias.STINGER_SPEAR_PARALYZING_TRIGGER.get().trigger(serverPlayer);

                    if (victim.getHealth() > 70) {
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
