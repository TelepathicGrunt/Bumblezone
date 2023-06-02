package com.telepathicgrunt.the_bumblezone.items.essence;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class RagingEssence extends AbilityEssenceItem {

    private static final int cooldownLengthInTicks = 12000;
    private static final int abilityUseAmount = 24;

    public RagingEssence(Properties properties) {
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
        if (((long)serverPlayer.tickCount + serverPlayer.getUUID().getLeastSignificantBits()) % 20L == 0) {

            if (!getForcedCooldown(stack))
            {
            }
        }
    }
}