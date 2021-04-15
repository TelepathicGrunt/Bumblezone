package com.telepathicgrunt.the_bumblezone.effects;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.modcompat.CarrierBeeRedirection;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

import java.util.List;


public class WrathOfTheHiveEffect extends Effect {
    private final static EntityPredicate SEE_THROUGH_WALLS = (new EntityPredicate()).setLineOfSiteRequired();
    private final static EntityPredicate LINE_OF_SIGHT = (new EntityPredicate());
    public static boolean ACTIVE_WRATH = false;

    public WrathOfTheHiveEffect(EffectType type, int potionColor) {
        super(type, potionColor);
    }

    /**
     * Returns true if the potion has an instant effect instead of a continuous one (eg Harming)
     */
    public boolean isInstant() {
        return true;
    }

    /**
     * checks if Potion effect is ready to be applied this tick.
     */
    public boolean isReady(int duration, int amplifier) {
        return duration >= 1;
    }

    /**
     * Makes the bees swarm at the entity
     */
    public void performEffect(LivingEntity entity, int amplifier) {
        //Maximum aggression
        if (amplifier >= 2) {
            unBEElievablyHighAggression(entity.world, entity);
            if(ModChecker.carrierBeesPresent){
                CarrierBeeRedirection.CBMobSpawn(entity);
            }
        }
        //Anything lower than 2 is medium aggression
        else {
            mediumAggression(entity.world, entity);
        }
    }

    /**
     * Bees are angry but not crazy angry
     */
    public static void mediumAggression(World world, LivingEntity livingEntity) {
        LINE_OF_SIGHT.setDistance(Bumblezone.BzBeeAggressionConfig.aggressionTriggerRadius.get());
        List<BeeEntity> beeList = world.getTargettableEntitiesWithinAABB(BeeEntity.class, LINE_OF_SIGHT, livingEntity, livingEntity.getBoundingBox().grow(Bumblezone.BzBeeAggressionConfig.aggressionTriggerRadius.get()));

        for (BeeEntity bee : beeList) {
            bee.setAttackTarget(livingEntity);

            EffectInstance effect = livingEntity.getActivePotionEffect(BzEffects.WRATH_OF_THE_HIVE.get());
            if(effect != null) {
                int leftoverDuration = effect.getDuration();

                // weaker potion effects for when attacking bears
                bee.addPotionEffect(new EffectInstance(Effects.SPEED, leftoverDuration, Math.max((Bumblezone.BzBeeAggressionConfig.speedBoostLevel.get() - 1), 1), false, false));
                bee.addPotionEffect(new EffectInstance(Effects.ABSORPTION, leftoverDuration, Math.max((Bumblezone.BzBeeAggressionConfig.absorptionBoostLevel.get() - 1) / 2, 1), false, false));
                bee.addPotionEffect(new EffectInstance(Effects.STRENGTH, leftoverDuration, Math.max((Bumblezone.BzBeeAggressionConfig.strengthBoostLevel.get() - 1) / 3, 1), false, true));
            }
        }
    }


    /**
     * Bees are REALLY angry!!! HIGH TAIL IT OUTTA THERE BRUH!!!
     */
    public static void unBEElievablyHighAggression(World world, LivingEntity livingEntity) {
        SEE_THROUGH_WALLS.setDistance(Bumblezone.BzBeeAggressionConfig.aggressionTriggerRadius.get());
        List<BeeEntity> beeList = world.getTargettableEntitiesWithinAABB(BeeEntity.class, SEE_THROUGH_WALLS, livingEntity, livingEntity.getBoundingBox().grow(Bumblezone.BzBeeAggressionConfig.aggressionTriggerRadius.get()));
        for (BeeEntity bee : beeList) {
            bee.setAttackTarget(livingEntity);
            EffectInstance effect = livingEntity.getActivePotionEffect(BzEffects.WRATH_OF_THE_HIVE.get());
            if(effect != null) {
                int leftoverDuration = effect.getDuration();

                bee.addPotionEffect(new EffectInstance(Effects.SPEED, leftoverDuration, Bumblezone.BzBeeAggressionConfig.speedBoostLevel.get() - 1, false, false));
                bee.addPotionEffect(new EffectInstance(Effects.ABSORPTION, leftoverDuration, Bumblezone.BzBeeAggressionConfig.absorptionBoostLevel.get() - 1, false, false));
                bee.addPotionEffect(new EffectInstance(Effects.STRENGTH, leftoverDuration, Bumblezone.BzBeeAggressionConfig.strengthBoostLevel.get() - 1, false, true));
            }
        }
    }

    /**
     * Calm the bees that are attacking the incoming entity
     */
    public static void calmTheBees(World world, LivingEntity livingEntity)
    {
        SEE_THROUGH_WALLS.setDistance(Bumblezone.BzBeeAggressionConfig.aggressionTriggerRadius.get()*0.5D);
        List<BeeEntity> beeList = world.getTargettableEntitiesWithinAABB(BeeEntity.class, SEE_THROUGH_WALLS, livingEntity, livingEntity.getBoundingBox().grow(Bumblezone.BzBeeAggressionConfig.aggressionTriggerRadius.get()*0.5D));
        for (BeeEntity bee : beeList)
        {
            if(bee.getAttackTarget() == livingEntity) {
                bee.setAttackTarget(null);
                bee.setAggroed(false);
                bee.setAngerTime(0);
                bee.removePotionEffect(Effects.STRENGTH);
                bee.removePotionEffect(Effects.SPEED);
                bee.removePotionEffect(Effects.ABSORPTION);
            }
        }
    }

    // Don't remove wrath effect from mobs that bees are to always be angry at (bears, non-bee insects)
    public void removeAttributesModifiersFromEntity(LivingEntity entity, AttributeModifierManager attributes, int amplifier) {
        if(BeeAggression.doesBeesHateEntity(entity)){
            //refresh the bee anger timer
            entity.addPotionEffect(new EffectInstance(
                    BzEffects.WRATH_OF_THE_HIVE.get(),
                    Bumblezone.BzBeeAggressionConfig.howLongWrathOfTheHiveLasts.get(),
                    1,
                    false,
                    true));
        }
        else{
            // remove the effect like normal
            super.removeAttributesModifiersFromEntity(entity, attributes, amplifier);
        }
    }
}
