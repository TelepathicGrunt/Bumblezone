package com.telepathicgrunt.the_bumblezone.items.essence;

import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class AbilityEssenceItem extends Item {

    private static final String ACTIVE_TAG = "isActive";
    private static final String COOLDOWN_TIME_TAG = "cooldownTime";
    private static final String FORCED_COOLDOWN_TAG = "forcedCooldown";
    private static final String LAST_ABILITY_CHARGE_TIMESTAMP_TAG = "lastChargeTime";
    private final int cooldownTickLength;

    public AbilityEssenceItem(Properties properties, int cooldownTickLength) {
        super(properties);
        this.cooldownTickLength = cooldownTickLength;
    }

    public int getCooldownTickLength() {
        return cooldownTickLength;
    }

    public static void setLastAbilityChargeTimestamp(ItemStack stack, int gametime) {
        stack.getOrCreateTag().putInt(LAST_ABILITY_CHARGE_TIMESTAMP_TAG, gametime);
    }

    public static int getLastAbilityChargeTimestamp(ItemStack stack) {
        return stack.getOrCreateTag().getInt(LAST_ABILITY_CHARGE_TIMESTAMP_TAG);
    }

    public static void setIsActive(ItemStack stack, boolean isActive) {
        stack.getOrCreateTag().putBoolean(ACTIVE_TAG, isActive);
    }

    public static boolean getIsActive(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean(ACTIVE_TAG);
    }

    public static void setForcedCooldown(ItemStack stack, boolean forcedCooldown) {
        stack.getOrCreateTag().putBoolean(FORCED_COOLDOWN_TAG, forcedCooldown);
    }

    public static boolean getForcedCooldown(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean(FORCED_COOLDOWN_TAG);
    }

    public static void setCooldownTime(ItemStack stack, int cooldownTime) {
        stack.getOrCreateTag().putInt(COOLDOWN_TIME_TAG, cooldownTime);
    }

    public static int getCooldownTime(ItemStack stack) {
        return stack.getOrCreateTag().getInt(COOLDOWN_TIME_TAG);
    }

    public void incrementCooldownTime(ItemStack stack) {
        if (getForcedCooldown(stack)) {
            int currentCooldownTime = getCooldownTime(stack);
            if (currentCooldownTime < cooldownTickLength) {
                setCooldownTime(stack, currentCooldownTime + 1);
            }
            else {
                setCooldownTime(stack, 0);
                setForcedCooldown(stack, false);
                rechargeAbilityEntirely(stack);
            }
        }
    }

    public void setDepleted(ItemStack stack, ServerPlayer serverPlayer, boolean vanillaItemCooldown) {
        setForcedCooldown(stack, true);
        setCooldownTime(stack, 0);
        setIsActive(stack, false);
        if (vanillaItemCooldown) {
            serverPlayer.getCooldowns().addCooldown(this, getCooldownTickLength());
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean bl) {
        if (stack.is(BzTags.ABILITY_ESSENCE_ITEMS) &&
            entity instanceof ServerPlayer serverPlayer &&
            EssenceOfTheBees.hasEssence(serverPlayer))
        {
            if (getForcedCooldown(stack)) {
                incrementCooldownTime(stack);
            }
            else {
                if (serverPlayer.getOffhandItem() == stack) {
                    if (!getIsActive(stack)) {
                        setIsActive(stack, true);
                    }
                    applyAbilityEffects(stack, level, serverPlayer);
                }
                else {
                    rechargeAbilitySlowly(stack, level, serverPlayer);
                    if (getIsActive(stack)) {
                        setIsActive(stack, false);
                    }
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> components, TooltipFlag tooltipFlag) {
        if (getIsActive(stack)) {
            components.add(Component.translatable("item.the_bumblezone.essence_active").withStyle(ChatFormatting.RED));
            components.add(Component.translatable("item.the_bumblezone.essence_usage", getAbilityUseRemaining(stack), getMaxAbilityUseAmount(stack)).withStyle(ChatFormatting.YELLOW));
        }
        else if (getForcedCooldown(stack)) {
            components.add(Component.translatable("item.the_bumblezone.essence_depleted").withStyle(ChatFormatting.DARK_RED));
            components.add(Component.translatable("item.the_bumblezone.essence_cooldown", (getCooldownTickLength() - getCooldownTime(stack)) / 20).withStyle(ChatFormatting.DARK_RED));
        }
        else {
            components.add(Component.translatable("item.the_bumblezone.essence_ready").withStyle(ChatFormatting.GREEN));
            components.add(Component.translatable("item.the_bumblezone.essence_usage", getAbilityUseRemaining(stack), getMaxAbilityUseAmount(stack)).withStyle(ChatFormatting.YELLOW));
        }
    }

    abstract void applyAbilityEffects(ItemStack stack, Level level, ServerPlayer serverPlayer);

    abstract void rechargeAbilitySlowly(ItemStack stack, Level level, ServerPlayer serverPlayer);

    abstract void rechargeAbilityEntirely(ItemStack stack);

    abstract int getAbilityUseRemaining(ItemStack stack);

    abstract int getMaxAbilityUseAmount(ItemStack stack);
}