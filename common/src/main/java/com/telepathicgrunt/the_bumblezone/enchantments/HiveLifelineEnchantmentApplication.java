package com.telepathicgrunt.the_bumblezone.enchantments;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.enchantments.datacomponents.HiveLifelineMarker;
import com.telepathicgrunt.the_bumblezone.events.entity.BzEntityAttackedEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzEnchantments;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HiveLifelineEnchantmentApplication {

    private final static TargetingConditions SEE_THROUGH_WALLS = (TargetingConditions.forCombat())
            .ignoreLineOfSight()
            .ignoreInvisibilityTesting();

    public static Pair<HiveLifelineMarker, Integer> getHiveLifelineEnchantLevel(ItemStack stack) {
        return EnchantmentHelper.getHighestLevel(stack, BzEnchantments.HIVE_LIFELINE_MARKER.get());
    }

    public static boolean entityHurtEvent(BzEntityAttackedEvent event) {
        if (!(event.entity() instanceof ServerPlayer player) || !(player.level() instanceof ServerLevel serverLevel)) {
            return false;
        }

        Entity attacker = event.source().getEntity();
        if (attacker == null || attacker.getType().is(BzTags.HIVE_LIFELINE_CANNOT_TRIGGER_LIFELINE)) {
            return false;
        }

        ItemStack currentHiveLifelineArmor = null;
        Pair<HiveLifelineMarker, Integer> hiveLifelineMarker = null;
        for (ItemStack armorItem : player.getArmorSlots()) {
            if (!player.getCooldowns().isOnCooldown(armorItem.getItem())) {
                Pair<HiveLifelineMarker, Integer> enchantmentResult = getHiveLifelineEnchantLevel(armorItem);
                if (enchantmentResult != null) {
                    currentHiveLifelineArmor = armorItem;
                    hiveLifelineMarker = enchantmentResult;
                    break;
                }
            }
        }

        if (currentHiveLifelineArmor == null) {
            return false;
        }

        int baseRange = hiveLifelineMarker.getFirst().lifelineEntityRange();
        int additionalRange = hiveLifelineMarker.getFirst().lifelineEntityRangeIncreasePerAdditionalLevel() * (hiveLifelineMarker.getSecond() - 1);

        List<LivingEntity> entitiesNearby = serverLevel.getNearbyEntities(
                LivingEntity.class,
                SEE_THROUGH_WALLS,
                player,
                player.getBoundingBox().inflate(baseRange + additionalRange));

        // Damage closest lifeline entity first if possible
        entitiesNearby.sort(Comparator.comparingDouble(entity -> entity.position().distanceTo(player.position())));

        for (LivingEntity nearbyEntity : entitiesNearby) {
            if (!nearbyEntity.getType().is(BzTags.HIVE_LIFELINE_TAKES_PLAYER_DAMAGE)) {
                continue;
            }

            if (nearbyEntity.isInvulnerable() || nearbyEntity.isDeadOrDying()) {
                continue;
            }

            if (nearbyEntity instanceof Mob mob && mob.isNoAi()) {
                continue;
            }

            nearbyEntity.hurt(event.source(), event.amount());
            player.getCooldowns().addCooldown(currentHiveLifelineArmor.getItem(), hiveLifelineMarker.getFirst().armorCooldownTicks());

            serverLevel.sendParticles(
                    BzParticles.SPARKLE_PARTICLE.get(),
                    player.position().x(),
                    player.position().y() + 1,
                    player.position().z(),
                    50,
                    player.getRandom().nextGaussian() * 0.1D,
                    (player.getRandom().nextGaussian() * 0.1D) + 0.1,
                    player.getRandom().nextGaussian() * 0.1D,
                    player.getRandom().nextFloat() * 0.1 + 0.1f);

            serverLevel.sendParticles(
                    BzParticles.SPARKLE_PARTICLE.get(),
                    nearbyEntity.position().x(),
                    nearbyEntity.position().y() + 1,
                    nearbyEntity.position().z(),
                    50,
                    nearbyEntity.getRandom().nextGaussian() * 0.1D,
                    (nearbyEntity.getRandom().nextGaussian() * 0.1D) + 0.1,
                    nearbyEntity.getRandom().nextGaussian() * 0.1D,
                    nearbyEntity.getRandom().nextFloat() * 0.1 + 0.1f);

            serverLevel.sendParticles(
                    ParticleTypes.ANGRY_VILLAGER,
                    nearbyEntity.position().x(),
                    nearbyEntity.position().y() + 1,
                    nearbyEntity.position().z(),
                    3,
                    nearbyEntity.getRandom().nextGaussian() * 0.4D,
                    (nearbyEntity.getRandom().nextGaussian() * 0.4D) + 0.1,
                    nearbyEntity.getRandom().nextGaussian() * 0.4D,
                    nearbyEntity.getRandom().nextFloat() * 0.1 + 0.1f);

            currentHiveLifelineArmor.hurtAndBreak(1, serverLevel, player, item -> {});

            return true;
        }

        return false;
    }
}
