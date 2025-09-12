package com.telepathicgrunt.the_bumblezone.enchantments;

import com.telepathicgrunt.the_bumblezone.events.entity.EntityAttackedEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzEnchantments;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.platform.BzEnchantment;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Comparator;
import java.util.List;

public class HiveLifelineEnchantment extends BzEnchantment {

    private final static TargetingConditions SEE_THROUGH_WALLS = (TargetingConditions.forCombat())
            .ignoreLineOfSight()
            .ignoreInvisibilityTesting();

    public HiveLifelineEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR, new EquipmentSlot[]{ EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET });
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public int getMinCost(int level) {
        return 50 + level * 5;
    }

    @Override
    public int getMaxCost(int level) {
        return 50 + level * 10;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.is(BzTags.ENCHANTABLE_HIVE_LIFELINE) || stack.is(Items.BOOK);
    }

    @Override
    public OptionalBoolean bz$canApplyAtEnchantingTable(ItemStack stack) {
        return OptionalBoolean.of(this.canEnchant(stack));
    }

    public static boolean entityHurtEvent(EntityAttackedEvent event) {
        if (!(event.entity() instanceof ServerPlayer player) || !(player.level() instanceof ServerLevel serverLevel)) {
            return false;
        }

        Entity attacker = event.source().getEntity();
        if (attacker == null || attacker.getType().is(BzTags.HIVE_LIFELINE_CANNOT_TRIGGER_LIFELINE)) {
            return false;
        }

        ItemStack currentHiveLifelineArmor = null;
        int hiveLifelineLevel = 0;
        for (ItemStack armorItem : player.getArmorSlots()) {
            if (!player.getCooldowns().isOnCooldown(armorItem.getItem())) {
                Integer enchantmentResult = EnchantmentHelper.getEnchantments(armorItem).get(BzEnchantments.HIVE_LIFELINE.get());
                if (enchantmentResult != null && enchantmentResult > 0) {
                    currentHiveLifelineArmor = armorItem;
                    hiveLifelineLevel = enchantmentResult;
                    break;
                }
            }
        }

        if (currentHiveLifelineArmor == null) {
            return false;
        }

        int baseRange = 16;
        int additionalRange = 6 * (hiveLifelineLevel - 1);

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
            int armorCooldownTicks = 80;
            player.getCooldowns().addCooldown(currentHiveLifelineArmor.getItem(), armorCooldownTicks);

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

            currentHiveLifelineArmor.hurtAndBreak(1, player, item -> {});

            return true;
        }

        return false;
    }
}
