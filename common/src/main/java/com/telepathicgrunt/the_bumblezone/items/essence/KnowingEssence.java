package com.telepathicgrunt.the_bumblezone.items.essence;

import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

import java.security.PublicKey;

public class KnowingEssence extends AbilityEssenceItem {

    private static final int cooldownLengthInTicks = 18000;
    private static final int abilityUseAmount = 1200;
    private static final String ABILITY_USE_REMAINING_TAG = "abilityUseRemaining";

    public KnowingEssence(Properties properties) {
        super(properties, cooldownLengthInTicks);
    }

    public static void setAbilityUseRemaining(ItemStack stack, int abilityUseRemaining) {
        stack.getOrCreateTag().putInt(ABILITY_USE_REMAINING_TAG, abilityUseRemaining);
    }

    public void decrementAbilityUseRemaining(ItemStack stack, ServerPlayer serverPlayer) {
        int getRemainingUse = Math.max(getAbilityUseRemaining(stack) - 1, 0);
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
        if (((long)serverPlayer.tickCount + serverPlayer.getUUID().getLeastSignificantBits()) % 20L == 0) {
            if (!getForcedCooldown(stack)) {
                decrementAbilityUseRemaining(stack, serverPlayer);
            }
        }
    }

    public static boolean IsKnowingEssenceActive(Player player) {
        if (player != null) {
            ItemStack offHandItem = player.getOffhandItem();
            return offHandItem.is(BzItems.ESSENCE_PURPLE.get()) && !getForcedCooldown(offHandItem);
        }
        return false;
    }

    public static boolean IsValidEntityToGlow(Entity entity, Player player) {
        return GetTeamColor(entity, player) != -1;
    }

    public static int GetTeamColor(Entity entity, Player player) {
        // TODO: Add tags for extra highlighting or highlight override
        // TODO: Config to disable certain colors
        if (entity instanceof Monster) {
            return 16318464; // red
        }
        else if (entity instanceof TamableAnimal tamableAnimal && tamableAnimal.isOwnedBy(player)) {
            return 3931904; // green
        }
        else if (entity instanceof LivingEntity) {
            return 16746496; // orange
        }
        else if (entity instanceof ItemEntity itemEntity) {
            ItemStack itemStack = itemEntity.getItem();
            if (itemStack.getRarity() == Rarity.COMMON) {
                return 16776444; // white
            }
            else if (itemStack.getRarity() == Rarity.UNCOMMON) {
                return 16774656; // yellow
            }
            else if (itemStack.getRarity() == Rarity.RARE) {
                return 57564; // cyan
            }
            else if (itemStack.getRarity() == Rarity.EPIC) {
                return 13238501; // purple
            }
        }

        return -1; // Invalid
    }
}