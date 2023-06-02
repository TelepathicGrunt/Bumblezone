package com.telepathicgrunt.the_bumblezone.items.essence;

import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class CalmingEssence extends AbilityEssenceItem {

    private static final int cooldownLengthInTicks = 12000;
    private static final int abilityUseAmount = 600;

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
    public void applyAbilityEffects(ItemStack stack, Level level, ServerPlayer serverPlayer) {
        if (((long)serverPlayer.tickCount + serverPlayer.getUUID().getLeastSignificantBits()) % 20L == 0) {

            if (!getForcedCooldown(stack)) {
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
                    depleteEssence(stack, serverPlayer);
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
            return offHandItem.is(BzItems.ESSENCE_BLUE.get()) && !getForcedCooldown(offHandItem);
        }
        return false;
    }

    public static boolean OnAttack(LivingEntity livingEntity, DamageSource damageSource) {
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
        return true;
    }
}