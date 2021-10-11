package com.telepathicgrunt.bumblezone.effects;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.modinit.BzEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;


public class ProtectionOfTheHiveEffect extends MobEffect {
    private final static TargetingConditions SEE_THROUGH_WALLS = (TargetingConditions.forCombat()).ignoreLineOfSight();

    public ProtectionOfTheHiveEffect(MobEffectCategory type, int potionColor) {
        super(type, potionColor);
    }

    /**
     * Returns true if the potion has an instant effect instead of a continuous one (eg Harming)
     */
    public boolean isInstantenous() {
        return false;
    }

    /**
     * checks if Potion effect is ready to be applied this tick.
     */
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration >= 1;
    }

    /**
     * Calm all attacking bees when first applied to the entity
     */
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {

        SEE_THROUGH_WALLS.range(Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.aggressionTriggerRadius*0.5D);
        List<Bee> beeList = entity.level.getNearbyEntities(Bee.class, SEE_THROUGH_WALLS, entity, entity.getBoundingBox().inflate(Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.aggressionTriggerRadius*0.5D));

        for (Bee bee : beeList)
        {
            if(bee.getTarget() == entity){
                bee.setTarget(null);
                bee.setPersistentAngerTarget(null);
                bee.setRemainingPersistentAngerTime(0);
            }
        }

        super.addAttributeModifiers(entity, attributes, amplifier);
    }

    /**
     * Makes the bees swarm at the attacking entity
     */
    public void applyEffectTick(LivingEntity entity, int amplifier) {
       if(entity.hurtTime > 0 && entity.getLastHurtByMob() != null){
           resetBeeAngry(entity.level, entity.getLastHurtByMob());

           if(!(entity.getLastHurtByMob() instanceof Bee))
                entity.getLastHurtByMob().addEffect(new MobEffectInstance(BzEffects.WRATH_OF_THE_HIVE, Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.howLongWrathOfTheHiveLasts, amplifier, true, true, true));
       }
    }

    /**
     * Changes the entity Bees are angry at.
     */
    public static void resetBeeAngry(Level world, LivingEntity livingEntity)
    {
        LivingEntity entity = livingEntity;
        UUID uuid = entity.getUUID();

        SEE_THROUGH_WALLS.range(Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.aggressionTriggerRadius*0.5D);
        List<Bee> beeList = world.getNearbyEntities(Bee.class, SEE_THROUGH_WALLS, entity, entity.getBoundingBox().inflate(Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.aggressionTriggerRadius*0.5D));

        if(livingEntity instanceof Bee){
            entity = null;
            uuid = null;
        }

        for (Bee bee : beeList)
        {
            bee.setTarget(entity);
            bee.setPersistentAngerTarget(uuid);
            if(entity == null){
                bee.setRemainingPersistentAngerTime(0);
            }
        }
    }
}
