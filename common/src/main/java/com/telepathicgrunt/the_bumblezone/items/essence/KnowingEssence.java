package com.telepathicgrunt.the_bumblezone.items.essence;

import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import com.telepathicgrunt.the_bumblezone.mixin.entities.FoxAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.packets.SyncHorseOwnerUUIDPacketFromServer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Supplier;

public class KnowingEssence extends AbilityEssenceItem {

    private static final Supplier<Integer> cooldownLengthInTicks = () -> BzGeneralConfigs.knowingEssenceCooldown;
    private static final Supplier<Integer> abilityUseAmount = () -> BzGeneralConfigs.knowingEssenceAbilityUse;

    public KnowingEssence(Properties properties) {
        super(properties, cooldownLengthInTicks, abilityUseAmount);
    }

    @Override
    void addDescriptionComponents(List<Component> components) {
        components.add(Component.translatable("item.the_bumblezone.essence_purple_description_1").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
        components.add(Component.translatable("item.the_bumblezone.essence_purple_description_2").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
    }

    public void decrementAbilityUseRemaining(ItemStack stack, ServerPlayer serverPlayer) {
        int getRemainingUse = Math.max(getAbilityUseRemaining(stack) - 1, 0);
        setAbilityUseRemaining(stack, getRemainingUse);
        if (getRemainingUse == 0) {
            setDepleted(stack, serverPlayer, false);
        }
    }

    @Override
    public void rechargeAbilitySlowly(ItemStack stack, Level level, ServerPlayer serverPlayer) {
        int abilityUseRemaining = getAbilityUseRemaining(stack);
        if (abilityUseRemaining < getMaxAbilityUseAmount()) {
            int lastChargeTime = getLastAbilityChargeTimestamp(stack);
            if (lastChargeTime == 0 || serverPlayer.tickCount < lastChargeTime) {
                setLastAbilityChargeTimestamp(stack, serverPlayer.tickCount);
            }
            else {
                int timeFromLastCharge = serverPlayer.tickCount - lastChargeTime;
                int chargeTimeIncrement = Math.max(getCooldownTickLength() / getMaxAbilityUseAmount(), 1);
                if (timeFromLastCharge % chargeTimeIncrement == 0) {
                    setAbilityUseRemaining(stack, abilityUseRemaining + 1);
                    setLastAbilityChargeTimestamp(stack, serverPlayer.tickCount);
                }
            }
        }
    }

    @Override
    public void rechargeAbilityEntirely(ItemStack stack) {
        setAbilityUseRemaining(stack, getMaxAbilityUseAmount());
    }

    @Override
    public void applyAbilityEffects(ItemStack stack, Level level, ServerPlayer serverPlayer) {
        if (getIsActive(stack)) {
            if (((long)serverPlayer.tickCount + serverPlayer.getUUID().getLeastSignificantBits()) % 5L == 0) {
                spawnParticles(serverPlayer.serverLevel(), serverPlayer.position(), serverPlayer.getRandom());
            }

            if (((long)serverPlayer.tickCount + serverPlayer.getUUID().getLeastSignificantBits()) % 20L == 0) {
                decrementAbilityUseRemaining(stack, serverPlayer);
            }
        }
    }

    public static boolean IsKnowingEssenceActive(Player player) {
        if (player != null) {
            ItemStack offHandItem = player.getOffhandItem();
            return offHandItem.is(BzItems.ESSENCE_PURPLE.get()) && getIsActive(offHandItem);
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
        else if (entity instanceof Enemy && !(entity instanceof HoneySlimeEntity)) {
            if (BzClientConfigs.knowingEssenceHighlightMonsters) {
                return RED;
            }
        }
        else if (entity instanceof OwnableEntity ownableEntity &&
                ownableEntity.getOwnerUUID() != null &&
                ownableEntity.getOwnerUUID().equals(player.getUUID()))
        {
            if (BzClientConfigs.knowingEssenceHighlightTamed) {
                return GREEN;
            }
        }
        else if (entity instanceof Fox fox &&
                ((FoxAccessor)fox).callTrusts(player.getUUID()))
        {
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

    public static void spawnParticles(ServerLevel world, Vec3 location, RandomSource random) {
        world.sendParticles(
                ParticleTypes.ENCHANT,
                location.x(),
                location.y() + 1,
                location.z(),
                1,
                random.nextGaussian() * 0.1D,
                (random.nextGaussian() * 0.1D) + 0.1,
                random.nextGaussian() * 0.1D,
                random.nextFloat() * 0.3 + 0.1f);
    }
}