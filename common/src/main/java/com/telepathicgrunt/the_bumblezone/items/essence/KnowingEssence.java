package com.telepathicgrunt.the_bumblezone.items.essence;

import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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

    private static final int RED = 16318464;
    private static final int ORANGE = 16746496;
    private static final int YELLOW = 16774656;
    private static final int GREEN = 3931904;
    private static final int CYAN = 57564;
    private static final int PURPLE = 13238501;
    private static final int WHITE = 16776444;
    private static final int NO_HIGHLIGHT = -1;

    public static int GetTeamColor(Entity entity, Player player) {
        EntityType<?> entityType = entity.getType();
        if (entityType.is(BzTags.ENTITY_PREVENT_HIGHLIGHTING)) {
            return NO_HIGHLIGHT;
        }
        else if (entityType.is(BzTags.ENTITY_FORCED_WHITE_HIGHLIGHT)) {
            return WHITE;
        }
        else if (entityType.is(BzTags.ENTITY_FORCED_PURPLE_HIGHLIGHT)) {
            return PURPLE;
        }
        else if (entityType.is(BzTags.ENTITY_FORCED_CYAN_HIGHLIGHT)) {
            return CYAN;
        }
        else if (entityType.is(BzTags.ENTITY_FORCED_GREEN_HIGHLIGHT)) {
            return GREEN;
        }
        else if (entityType.is(BzTags.ENTITY_FORCED_YELLOW_HIGHLIGHT)) {
            return YELLOW;
        }
        else if (entityType.is(BzTags.ENTITY_FORCED_ORANGE_HIGHLIGHT)) {
            return ORANGE;
        }
        else if (entityType.is(BzTags.ENTITY_FORCED_RED_HIGHLIGHT)) {
            return RED;
        }

        if (entityType.is(BzTags.BOSSES)) {
            if (BzClientConfigs.knowingEssenceHighlightBosses) {
                return PURPLE;
            }
        }
        else if (entity instanceof Monster) {
            if (BzClientConfigs.knowingEssenceHighlightMonsters) {
                return RED;
            }
        }
        else if (entity instanceof TamableAnimal tamableAnimal && tamableAnimal.isOwnedBy(player)) {
            if (BzClientConfigs.knowingEssenceHighlightTamed) {
                return GREEN;
            }
        }
        else if (entity instanceof LivingEntity) {
            if (BzClientConfigs.knowingEssenceHighlightLivingEntities) {
                return ORANGE;
            }
        }
        else if (entity instanceof ItemEntity itemEntity) {
            ItemStack itemStack = itemEntity.getItem();
            if (itemStack.getRarity() == Rarity.COMMON) {
                if (BzClientConfigs.knowingEssenceHighlightCommonItems) {
                    return WHITE;
                }
            }
            else if (itemStack.getRarity() == Rarity.UNCOMMON) {
                if (BzClientConfigs.knowingEssenceHighlightUncommonItems) {
                    return YELLOW;
                }
            }
            else if (itemStack.getRarity() == Rarity.RARE) {
                if (BzClientConfigs.knowingEssenceHighlightRareItems) {
                    return CYAN;
                }
            }
            else if (itemStack.getRarity() == Rarity.EPIC) {
                if (BzClientConfigs.knowingEssenceHighlightEpicItems) {
                    return PURPLE;
                }
            }
        }

        return NO_HIGHLIGHT;
    }
}