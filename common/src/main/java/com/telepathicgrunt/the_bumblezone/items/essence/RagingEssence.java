package com.telepathicgrunt.the_bumblezone.items.essence;

import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.events.entity.BzEntityDeathEvent;
import com.telepathicgrunt.the_bumblezone.items.datacomponents.AbilityEssenceActivityData;
import com.telepathicgrunt.the_bumblezone.items.datacomponents.RagingEssenceCurrentTargetData;
import com.telepathicgrunt.the_bumblezone.items.datacomponents.RagingEssenceStateData;
import com.telepathicgrunt.the_bumblezone.items.datacomponents.RagingEssenceTimerData;
import com.telepathicgrunt.the_bumblezone.modinit.BzDataComponents;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class RagingEssence extends AbilityEssenceItem {

    private static final Supplier<Integer> cooldownLengthInTicks = () -> BzGeneralConfigs.ragingEssenceCooldown;
    private static final Supplier<Integer> abilityUseAmount = () -> BzGeneralConfigs.ragingEssenceAbilityUse;

    private static final int radius = 24;
    private static final int trackingRange = radius * radius * 4;
    private static final int maxEmpoweredTimeLimit = 375;
    private static final int maxCurrentTargets = 4;

    public RagingEssence(Properties properties) {
        super(properties, cooldownLengthInTicks, abilityUseAmount);
    }

    @Override
    public void verifyComponentsAfterLoad(ItemStack itemStack) {
        if (itemStack.get(BzDataComponents.RAGING_ESSENCE_STATE_DATA.get()) == null) {
            itemStack.set(BzDataComponents.RAGING_ESSENCE_STATE_DATA.get(), new RagingEssenceStateData());
        }
        if (itemStack.get(BzDataComponents.RAGING_ESSENCE_CURRENT_TARGET_DATA.get()) == null) {
            itemStack.set(BzDataComponents.RAGING_ESSENCE_CURRENT_TARGET_DATA.get(), new RagingEssenceCurrentTargetData());
        }
        if (itemStack.get(BzDataComponents.RAGING_ESSENCE_TIMER_DATA.get()) == null) {
            itemStack.set(BzDataComponents.RAGING_ESSENCE_TIMER_DATA.get(), new RagingEssenceTimerData());
        }
        super.verifyComponentsAfterLoad(itemStack);
    }

    @Override
    public int getColor() {
        return 0xFF2B2B;
    }

    @Override
    void addDescriptionComponents(List<Component> components) {
        components.add(Component.translatable("item.the_bumblezone.essence_raging_description_1").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.ITALIC));
        components.add(Component.translatable("item.the_bumblezone.essence_raging_description_2").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.ITALIC));
    }

    public static void setRageState(ItemStack itemStack, short rageState) {
        itemStack.set(BzDataComponents.RAGING_ESSENCE_STATE_DATA.get(), new RagingEssenceStateData(rageState));
    }

    public static short getRageState(ItemStack itemStack) {
        return itemStack.get(BzDataComponents.RAGING_ESSENCE_STATE_DATA.get()).rageStateLevel();
    }

    public static void setEmpoweredTimestamp(ItemStack itemStack, long empoweredTimestamp) {
        itemStack.set(BzDataComponents.RAGING_ESSENCE_TIMER_DATA.get(), new RagingEssenceTimerData(empoweredTimestamp));
    }

    public static long getEmpoweredTimestamp(ItemStack itemStack) {
        return itemStack.get(BzDataComponents.RAGING_ESSENCE_TIMER_DATA.get()).empoweredTimestamp();
    }

    public static void setCurrentTargets(ItemStack itemStack, List<UUID> targetsToKill) {
        itemStack.set(BzDataComponents.RAGING_ESSENCE_CURRENT_TARGET_DATA.get(), new RagingEssenceCurrentTargetData(targetsToKill));
    }

    public static List<UUID> getCurrentTargets(ItemStack itemStack) {
        return new ArrayList<>(itemStack.get(BzDataComponents.RAGING_ESSENCE_CURRENT_TARGET_DATA.get()).currentTargets());
    }

    @Override
    public void applyAbilityEffects(ItemStack itemStack, Level level, ServerPlayer serverPlayer) {
        AbilityEssenceActivityData abilityEssenceActivityData = itemStack.get(BzDataComponents.ABILITY_ESSENCE_ACTIVITY_DATA.get());
        if (abilityEssenceActivityData.isActive() && BzGeneralConfigs.ragingEssenceStrengthLevels.length > 0) {

            // prevent crash if rage state is a too large value due to config editing while rage is active.
            int rageState = getRageState(itemStack);
            if (rageState > BzGeneralConfigs.ragingEssenceStrengthLevels.length) {
                setRageState(itemStack, (short) BzGeneralConfigs.ragingEssenceStrengthLevels.length);
                rageState = (short) BzGeneralConfigs.ragingEssenceStrengthLevels.length;
            }

            if (rageState > 0 && ((long)serverPlayer.tickCount + serverPlayer.getUUID().getLeastSignificantBits()) % 5L == 0) {
                spawnParticles(serverPlayer.serverLevel(), serverPlayer.position(), serverPlayer.getRandom(), rageState);

                // timer between kills
                if (level.getGameTime() - getEmpoweredTimestamp(itemStack) > maxEmpoweredTimeLimit) {
                    // reset as empowered phase is done
                    resetRage(itemStack, serverPlayer);
//                    Component message = Component.literal("DEBUG: Rage state is " + 0);
//                    serverPlayer.displayClientMessage(message, true);

                    // drain power
                    if (decrementAbilityUseRemaining(itemStack, serverPlayer, 1)) {
                        return;
                    }
                }
            }

            if (((long)serverPlayer.tickCount + serverPlayer.getUUID().getLeastSignificantBits()) % 20L == 0) {

                List<UUID> currentTargetsToKill = getCurrentTargets(itemStack);

                // Setup targets to kill if present
                if (currentTargetsToKill.size() < maxCurrentTargets && rageState + currentTargetsToKill.size() <= BzGeneralConfigs.ragingEssenceStrengthLevels.length) {
                    List<Entity> entities = level.getEntities(serverPlayer, new AABB(
                                serverPlayer.getX() - radius,
                                serverPlayer.getY() - (radius / 4D),
                                serverPlayer.getZ() - radius,
                                serverPlayer.getX() + radius,
                                serverPlayer.getY() + (radius / 4D),
                                serverPlayer.getZ() + radius
                            ),
                            (e) -> !currentTargetsToKill.contains(e.getUUID()) && isTargetable(e, serverPlayer.getUUID())
                    );

                    if (!entities.isEmpty() && currentTargetsToKill.size() < maxCurrentTargets) {

                        // Lower health entities to kill first
                        entities.sort((e1, e2) -> (int) (((LivingEntity)e1).getHealth() - ((LivingEntity)e2).getHealth()));
                        int rageLeft = BzGeneralConfigs.ragingEssenceStrengthLevels.length - rageState;
                        int entitiesToAdd = Math.min(rageLeft + 1, maxCurrentTargets) - currentTargetsToKill.size();
                        for (int i = entitiesToAdd; i >= 1 && !entities.isEmpty(); i--) {
                            if (i == 1) {
                                currentTargetsToKill.add(entities.remove(entities.size() - 1).getUUID());
                            }
                            else {
                                currentTargetsToKill.add(entities.remove(0).getUUID());
                            }
                        }

                        setCurrentTargets(itemStack, currentTargetsToKill);
                        return;
                    }
                }

                // Validate targets
                if (!currentTargetsToKill.isEmpty()) {

                    for (UUID uuid : currentTargetsToKill) {
                        Entity entity = serverPlayer.serverLevel().getEntity(uuid);

                        // An entity is out of range. Break chain.
                        if (entity == null || entity.distanceToSqr(serverPlayer.position()) > trackingRange * trackingRange) {
                            resetRage(itemStack, serverPlayer);
                            return;
                        }
                    }
                }

                // damage boost and particles
                if (rageState > 0) {
                    List<Holder<MobEffect>> radianceEffects = BuiltInRegistries.MOB_EFFECT.getTag(BzTags.RAGING_RAGE_EFFECTS)
                            .stream()
                            .flatMap(HolderSet.ListBacked::stream)
                            .filter(Holder::isBound)
                            .toList();

                    for (Holder<MobEffect> effectHolder : radianceEffects) {
                        if (effectHolder.value() == MobEffects.DAMAGE_BOOST) {
                            serverPlayer.addEffect(new MobEffectInstance(
                                MobEffects.DAMAGE_BOOST,
                                (int) ((level.getGameTime() - getEmpoweredTimestamp(itemStack)) * 4),
                                BzGeneralConfigs.ragingEssenceStrengthLevels[rageState - 1] - 1,
                                false,
                                false));
                        }
                        else {
                            serverPlayer.addEffect(new MobEffectInstance(
                                    effectHolder,
                                    (int) ((level.getGameTime() - getEmpoweredTimestamp(itemStack)) * 4),
                                    rageState,
                                    false,
                                    false));
                        }
                    }
                }
            }
        }
    }

    public static void OnEntityDeath(BzEntityDeathEvent event) {
        DamageSource damageSource = event.source();
        LivingEntity livingEntity = event.entity();
        if (damageSource.getEntity() instanceof ServerPlayer player) {
            ItemStack itemStack = player.getOffhandItem();
            AbilityEssenceActivityData abilityEssenceActivityData = itemStack.get(BzDataComponents.ABILITY_ESSENCE_ACTIVITY_DATA.get());
            if (livingEntity.isDeadOrDying() &&
                itemStack.getItem() instanceof RagingEssence ragingEssence &&
                abilityEssenceActivityData.isActive() &&
                !player.getCooldowns().isOnCooldown(itemStack.getItem()))
            {
                List<UUID> currentTargetsToKill = RagingEssence.getCurrentTargets(itemStack);
                int rageState = RagingEssence.getRageState(itemStack);
                if (rageState == BzGeneralConfigs.ragingEssenceStrengthLevels.length ||
                    currentTargetsToKill.contains(livingEntity.getUUID()))
                {
                    if (rageState >= BzGeneralConfigs.ragingEssenceStrengthLevels.length) {
                        resetRage(itemStack, player);
                    }
                    else {
                        RagingEssence.setRageState(itemStack, (short) (rageState + 1));
                        setEmpoweredTimestamp(itemStack, livingEntity.level().getGameTime());

                        currentTargetsToKill.remove(livingEntity.getUUID());
                        RagingEssence.setCurrentTargets(itemStack, currentTargetsToKill);
                        ragingEssence.decrementAbilityUseRemaining(itemStack, player, 1);

//                        Component message = Component.literal("DEBUG: Rage state is " + rageState);
//                        player.displayClientMessage(message, true);
                    }
                }
            }
        }
    }

    private static boolean isTargetable(Entity entity, UUID playerUUID) {
        return entity instanceof LivingEntity livingEntity &&
                !entity.isInvulnerable() &&
                livingEntity.attackable() &&
                !livingEntity.isDeadOrDying() &&
                (entity instanceof Enemy ||
                (entity instanceof NeutralMob neutralMob && neutralMob.getPersistentAngerTarget() == playerUUID));
    }

    private static void resetRage(ItemStack stack, ServerPlayer serverPlayer) {
        setCurrentTargets(stack, new ArrayList<>());
        setRageState(stack, (short)0);

        List<Holder<MobEffect>> radianceEffects = BuiltInRegistries.MOB_EFFECT.getTag(BzTags.RAGING_RAGE_EFFECTS)
                .stream()
                .flatMap(HolderSet.ListBacked::stream)
                .filter(Holder::isBound)
                .toList();

        for (Holder<MobEffect> effectHolder : radianceEffects) {
            serverPlayer.removeEffect(effectHolder);
        }

        serverPlayer.serverLevel().sendParticles(
                ParticleTypes.POOF,
                serverPlayer.position().x(),
                serverPlayer.position().y() + 1,
                serverPlayer.position().z(),
                50,
                serverPlayer.getRandom().nextGaussian() * 0.3D,
                (serverPlayer.getRandom().nextGaussian() * 0.25D) + 0.1,
                serverPlayer.getRandom().nextGaussian() * 0.3D,
                0.06f);
    }

    public static void spawnParticles(ServerLevel world, Vec3 location, RandomSource random, int rageState) {

        if (rageState == BzGeneralConfigs.ragingEssenceStrengthLevels.length) {
            rageState *= 2;
        }

        world.sendParticles(
                ParticleTypes.FLAME,
                location.x(),
                location.y() + 1,
                location.z(),
                rageState,
                random.nextGaussian() * 0.2D,
                (random.nextGaussian() * 0.25D) + 0.1,
                random.nextGaussian() * 0.2D,
                0.02f);

        world.sendParticles(
                ParticleTypes.CRIT,
                location.x(),
                location.y() + 1,
                location.z(),
                rageState,
                random.nextGaussian() * 0.2D,
                (random.nextGaussian() * 0.25D) + 0.1,
                random.nextGaussian() * 0.2D,
                0.1f);
    }

    public static boolean IsRagingEssenceActive(Player player) {
        if (player != null) {
            ItemStack offHandItem = player.getOffhandItem();

            return offHandItem.is(BzItems.ESSENCE_RAGING.get()) &&
                    offHandItem.get(BzDataComponents.ABILITY_ESSENCE_ACTIVITY_DATA.get()).isActive() &&
                    !player.getCooldowns().isOnCooldown(offHandItem.getItem());
        }
        return false;
    }

    public static boolean IsValidEntityToGlow(Entity entity, Player player) {
        return GetTeamColor(entity, player) != -1;
    }

    public static int GetTeamColor(Entity entity, Player player) {
        ItemStack stack = player.getOffhandItem();

        int rageState = RagingEssence.getRageState(stack);
        List<UUID> currentTargetsToKill = RagingEssence.getCurrentTargets(stack);

        if ((rageState == BzGeneralConfigs.ragingEssenceStrengthLevels.length && isTargetable(entity, player.getUUID())) ||
            currentTargetsToKill.contains(entity.getUUID()))
        {

            if (rageState == BzGeneralConfigs.ragingEssenceStrengthLevels.length) {
                float percentage = Math.abs(((player.tickCount % 40) - 20) / 20f);
                int red = 0xFF;
                int green = Mth.lerpInt(percentage, 0x00, 0x80);
                return ((red & 0xFF) << 16) | ((green & 0xFF) << 8);
            }
            else {
                float percentage = (float)rageState / BzGeneralConfigs.ragingEssenceStrengthLevels.length;
                return (Mth.lerpInt(percentage, 0x55, 0xFF) << 16);
            }
        }

        return -1; // No highlight
    }
}