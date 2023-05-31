package com.telepathicgrunt.the_bumblezone.items.essence;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.UUID;

public class RadianceEssence extends AbilityEssenceItem {

    private static final int cooldownLengthInTicks = 12000;
    private static final int abilityUseAmount = 4800;
    private static final String ABILITY_USE_REMAINING_TAG = "abilityUseRemaining";

    public RadianceEssence(Properties properties) {
        super(properties, cooldownLengthInTicks);
    }

    public static void setAbilityUseRemaining(ItemStack stack, int abilityUseRemaining) {
        stack.getOrCreateTag().putInt(ABILITY_USE_REMAINING_TAG, abilityUseRemaining);
    }

    public void decrementAbilityUseRemaining(ItemStack stack, ServerPlayer serverPlayer) {
        int getRemainingUse = getAbilityUseRemaining(stack) - 1;
        setAbilityUseRemaining(stack, getRemainingUse);
        if (getRemainingUse == 0) {
            setDepleted(stack, serverPlayer, false);
        }
    }

    @Override
    public int getAbilityUseRemaining(ItemStack stack) {
        if (!stack.getOrCreateTag().contains(ABILITY_USE_REMAINING_TAG)) {
            setAbilityUseRemaining(stack, getMaxAbilityUseAmount(stack));
            return getMaxAbilityUseAmount(stack);
        }

        return stack.getOrCreateTag().getInt(ABILITY_USE_REMAINING_TAG);
    }

    @Override
    int getMaxAbilityUseAmount(ItemStack stack) {
        return abilityUseAmount;
    }

    @Override
    void rechargeAbilitySlowly(ItemStack stack, Level level, ServerPlayer serverPlayer) {
        int abilityUseRemaining = getAbilityUseRemaining(stack);
        if (abilityUseRemaining < getMaxAbilityUseAmount(stack)) {
            int lastChargeTime = getLastAbilityChargeTimestamp(stack);
            if (lastChargeTime == 0 || serverPlayer.tickCount < lastChargeTime) {
                setLastAbilityChargeTimestamp(stack, serverPlayer.tickCount);
            }
            else {
                int timeFromLastCharge = serverPlayer.tickCount - lastChargeTime;
                int chargeTimeIncrement = getCooldownTickLength() / getMaxAbilityUseAmount(stack);
                if (timeFromLastCharge % chargeTimeIncrement == 0) {
                    setAbilityUseRemaining(stack, abilityUseRemaining + 1);
                    setLastAbilityChargeTimestamp(stack, serverPlayer.tickCount);
                }
            }
        }
    }

    @Override
    void rechargeAbilityEntirely(ItemStack stack) {
        setAbilityUseRemaining(stack, getMaxAbilityUseAmount(stack));
    }

    @Override
    void applyAbilityEffects(ItemStack stack, Level level, ServerPlayer serverPlayer) {
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
                decrementAbilityUseRemaining(stack, serverPlayer);
                if (getForcedCooldown(stack)) {
                    return;
                }

                serverPlayer.addEffect(new MobEffectInstance(
                        MobEffects.DAMAGE_RESISTANCE,
                        120,
                        1,
                        false,
                        false));
                decrementAbilityUseRemaining(stack, serverPlayer);
                if (getForcedCooldown(stack)) {
                    return;
                }

                serverPlayer.addEffect(new MobEffectInstance(
                        MobEffects.REGENERATION,
                        120,
                        0,
                        false,
                        false));
                decrementAbilityUseRemaining(stack, serverPlayer);
                if (getForcedCooldown(stack)) {
                    return;
                }

                serverPlayer.addEffect(new MobEffectInstance(
                        MobEffects.DIG_SPEED,
                        120,
                        1,
                        false,
                        false));
                decrementAbilityUseRemaining(stack, serverPlayer);
                if (getForcedCooldown(stack)) {
                    return;
                }

                serverPlayer.addEffect(new MobEffectInstance(
                        MobEffects.SATURATION,
                        120,
                        0,
                        false,
                        false));
                decrementAbilityUseRemaining(stack, serverPlayer);
                if (getForcedCooldown(stack)) {
                    return;
                }

                for (ItemStack armorItem : serverPlayer.getArmorSlots()) {
                    if (armorItem.isDamageableItem() && armorItem.isDamaged()) {
                        armorItem.setDamageValue(armorItem.getDamageValue() - 1);
                        decrementAbilityUseRemaining(stack, serverPlayer);

                        if (getForcedCooldown(stack)) {
                            return;
                        }
                    }
                }
            }
        }
    }
}