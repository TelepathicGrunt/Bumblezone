package com.telepathicgrunt.the_bumblezone.items.essence;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class RadianceEssence extends AbilityEssenceItem {

    private static final int cooldownLengthInTicks = 12000;
    private static final int abilityUseAmount = 4800;

    public RadianceEssence(Properties properties) {
        super(properties, cooldownLengthInTicks, abilityUseAmount);
    }

    public void decrementAbilityUseRemaining(ItemStack stack, ServerPlayer serverPlayer, int decreaseAmount) {
        int getRemainingUse = Math.max(getAbilityUseRemaining(stack) - decreaseAmount, 0);
        setAbilityUseRemaining(stack, getRemainingUse);
        if (getRemainingUse == 0) {
            setDepleted(stack, serverPlayer, false);
        }
    }

    @Override
    public void applyAbilityEffects(ItemStack stack, Level level, ServerPlayer serverPlayer) {
        if (((long)serverPlayer.tickCount + serverPlayer.getUUID().getLeastSignificantBits()) % 25L == 0) {

            if (!getForcedCooldown(stack) &&
                level.getBrightness(LightLayer.SKY, serverPlayer.blockPosition()) >= 13)
            {
                serverPlayer.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SPEED,
                        120,
                        0,
                        false,
                        false));
                decrementAbilityUseRemaining(stack, serverPlayer, serverPlayer.isSprinting() ? 3 : 1);
                if (getForcedCooldown(stack)) {
                    return;
                }

                serverPlayer.addEffect(new MobEffectInstance(
                        MobEffects.DAMAGE_RESISTANCE,
                        120,
                        1,
                        false,
                        false));
                decrementAbilityUseRemaining(stack, serverPlayer, 1);
                if (getForcedCooldown(stack)) {
                    return;
                }

                serverPlayer.addEffect(new MobEffectInstance(
                        MobEffects.REGENERATION,
                        120,
                        0,
                        false,
                        false));
                decrementAbilityUseRemaining(stack, serverPlayer, serverPlayer.getHealth() < serverPlayer.getMaxHealth() ? 5 : 1);
                if (getForcedCooldown(stack)) {
                    return;
                }

                serverPlayer.addEffect(new MobEffectInstance(
                        MobEffects.DIG_SPEED,
                        120,
                        1,
                        false,
                        false));
                decrementAbilityUseRemaining(stack, serverPlayer, 1);
                if (getForcedCooldown(stack)) {
                    return;
                }

                serverPlayer.addEffect(new MobEffectInstance(
                        MobEffects.SATURATION,
                        120,
                        0,
                        false,
                        false));
                decrementAbilityUseRemaining(stack, serverPlayer, serverPlayer.getFoodData().needsFood() ? 3 : 1);
                if (getForcedCooldown(stack)) {
                    return;
                }

                for (ItemStack armorItem : serverPlayer.getArmorSlots()) {
                    if (armorItem.isDamageableItem() && armorItem.isDamaged()) {
                        armorItem.setDamageValue(armorItem.getDamageValue() - 1);
                        decrementAbilityUseRemaining(stack, serverPlayer, 10);

                        if (getForcedCooldown(stack)) {
                            return;
                        }
                    }
                }
            }
        }
    }
}