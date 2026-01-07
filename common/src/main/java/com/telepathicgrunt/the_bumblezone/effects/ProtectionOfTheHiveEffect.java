package com.telepathicgrunt.the_bumblezone.effects;

import com.telepathicgrunt.the_bumblezone.configs.BzBeeAggressionConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;


public class ProtectionOfTheHiveEffect extends MobEffect {
    private final static TargetingConditions SEE_THROUGH_WALLS = (TargetingConditions.forCombat()).ignoreLineOfSight().ignoreInvisibilityTesting();

    public ProtectionOfTheHiveEffect(MobEffectCategory type, int potionColor) {
        super(type, potionColor);
    }

    /**
     * Returns true if the potion has an instant effect instead of a continuous one (eg Harming)
     */
    @Override
    public boolean isInstantenous() {
        return false;
    }

    /**
     * checks if Potion effect is ready to be applied this tick.
     */
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration >= 1;
    }

    /**
     * Calm all attacking bees when first applied to the entity
     */
    @Override
    public void onEffectStarted(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.level() instanceof ServerLevel serverLevel) {
            SEE_THROUGH_WALLS.range(BzBeeAggressionConfigs.aggressionTriggerRadius * 0.5D);
            List<Bee> beeList = serverLevel.getNearbyEntities(Bee.class, SEE_THROUGH_WALLS, livingEntity, livingEntity.getBoundingBox().inflate(BzBeeAggressionConfigs.aggressionTriggerRadius * 0.5D));

            for (Bee bee : beeList) {
                if(bee.getTarget() == livingEntity && !bee.isNoAi()) {
                    bee.setTarget(null);
                    bee.setPersistentAngerTarget(null);
                    bee.setTimeToRemainAngry(0);
                }
            }
        }

        super.onEffectStarted(livingEntity, amplifier);
    }

    /**
     * Makes the bees swarm at the attacking entity
     */
    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity entity, int amplifier) {
       if (entity.hurtTime > 0 && entity.getLastHurtByMob() != null) {
           if (entity.getLastHurtByMob() instanceof Player player && player.isCreative()) {
               return true;
           }

           if (!(entity.getLastHurtByMob() instanceof Bee)) {
               resetBeeAngry(serverLevel, entity.getLastHurtByMob());
               entity.getLastHurtByMob().addEffect(new MobEffectInstance(BzEffects.WRATH_OF_THE_HIVE.holder(), BzBeeAggressionConfigs.howLongWrathOfTheHiveLasts, amplifier, true, true, true));
               if (entity instanceof ServerPlayer serverPlayer) {
                   BzCriterias.PROTECTION_OF_THE_HIVE_DEFENSE_TRIGGER.get().trigger(serverPlayer, serverPlayer.getLastHurtByMob());
               }
           }
       }

        return true;
    }

    /**
     * Changes the entity Bees are angry at.
     */
    public static void resetBeeAngry(ServerLevel level, LivingEntity livingEntity) {
        LivingEntity entity = livingEntity;
        UUID uuid = entity.getUUID();

        SEE_THROUGH_WALLS.range(BzBeeAggressionConfigs.aggressionTriggerRadius * 0.5D);
        List<Bee> beeList = level.getNearbyEntities(Bee.class, SEE_THROUGH_WALLS, entity, entity.getBoundingBox().inflate(BzBeeAggressionConfigs.aggressionTriggerRadius * 0.5D));

        if(livingEntity instanceof Bee) {
            entity = null;
            uuid = null;
        }

        for (Bee bee : beeList) {
            if (bee.isNoAi()) {
                continue;
            }

            bee.setTarget(entity);
            bee.setPersistentAngerTarget(EntityReference.of(uuid));
            if(entity == null) {
                bee.setTimeToRemainAngry(0);
            }
        }
    }
}
