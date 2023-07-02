package com.telepathicgrunt.the_bumblezone.items.essence;

import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityHurtEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Supplier;

public class CalmingEssence extends AbilityEssenceItem {

    private static final Supplier<Integer> cooldownLengthInTicks = () -> BzGeneralConfigs.calmingEssenceCooldown;
    private static final Supplier<Integer> abilityUseAmount = () -> BzGeneralConfigs.calmingEssenceAbilityUse;

    public CalmingEssence(Properties properties) {
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
    void addDescriptionComponents(List<Component> components) {
        components.add(Component.translatable("item.the_bumblezone.essence_blue_description_1").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.ITALIC));
        components.add(Component.translatable("item.the_bumblezone.essence_blue_description_2").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.ITALIC));
        components.add(Component.translatable("item.the_bumblezone.essence_blue_description_3").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.ITALIC));
    }

    @Override
    public void applyAbilityEffects(ItemStack stack, Level level, ServerPlayer serverPlayer) {
        if (getIsActive(stack)) {
            if (((long)serverPlayer.tickCount + serverPlayer.getUUID().getLeastSignificantBits()) % 12L == 0) {
                spawnParticles(serverPlayer.serverLevel(), serverPlayer.position(), serverPlayer.getRandom());
            }

            if (((long)serverPlayer.tickCount + serverPlayer.getUUID().getLeastSignificantBits()) % 20L == 0) {
                serverPlayer.getStats().setValue(serverPlayer, Stats.CUSTOM.get(Stats.TIME_SINCE_REST), 0);

                for (Entity entity : level.getEntities(serverPlayer, serverPlayer.getBoundingBox().inflate(60))) {
                    if (entity.getType().is(BzTags.ALLOW_ANGER_THROUGH)) {
                        continue;
                    }

                    if (entity instanceof Mob mob && mob.getTarget() == serverPlayer) {
                        mob.setTarget(null);
                    }

                    if (entity instanceof NeutralMob neutralMob && neutralMob.getPersistentAngerTarget() == serverPlayer.getUUID()) {
                        neutralMob.stopBeingAngry();
                    }

                    if (entity instanceof Warden warden && warden.getAngerManagement().getActiveEntity().orElse(null) == serverPlayer) {
                        warden.clearAnger(serverPlayer);
                    }
                }

                if (serverPlayer.isSprinting()) {
                    decrementAbilityUseRemaining(stack, serverPlayer, 60);
                }
                else {
                    decrementAbilityUseRemaining(stack, serverPlayer, 1);
                }
            }
        }
    }

    private void depleteEssence(ItemStack stack, ServerPlayer serverPlayer) {
        setAbilityUseRemaining(stack, 0);
        setDepleted(stack, serverPlayer, false);
    }

    public static boolean IsCalmingEssenceActive(Player player) {
        if (player != null) {
            ItemStack offHandItem = player.getOffhandItem();
            return offHandItem.is(BzItems.ESSENCE_BLUE.get()) && getIsActive(offHandItem);
        }
        return false;
    }

    public static void OnAttack(EntityHurtEvent event) {
        DamageSource damageSource = event.source();
        LivingEntity livingEntity = event.entity();
        if (livingEntity instanceof Mob && damageSource.getEntity() instanceof ServerPlayer serverPlayer) {
            ItemStack offHandItem = serverPlayer.getOffhandItem();
            if (offHandItem.getItem() instanceof CalmingEssence calmingEssence && IsCalmingEssenceActive(serverPlayer)) {
                calmingEssence.depleteEssence(offHandItem, serverPlayer);
            }
        }
        else if (livingEntity instanceof ServerPlayer serverPlayer && damageSource.getEntity() instanceof Mob) {
            ItemStack offHandItem = serverPlayer.getOffhandItem();
            if (offHandItem.getItem() instanceof CalmingEssence calmingEssence && IsCalmingEssenceActive(serverPlayer)) {
                calmingEssence.depleteEssence(offHandItem, serverPlayer);
            }
        }
    }

    public static void spawnParticles(ServerLevel world, Vec3 location, RandomSource random) {
        world.sendParticles(
                BzParticles.SPARKLE_PARTICLE.get(),
                location.x(),
                location.y() + 1,
                location.z(),
                1,
                random.nextGaussian() * 0.2D,
                (random.nextGaussian() * 0.25D) + 0.1,
                random.nextGaussian() * 0.2D,
                0);
    }
}