package com.telepathicgrunt.the_bumblezone.effects;

import com.telepathicgrunt.the_bumblezone.configs.BzBeeAggressionConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;


public class ProtectionOfTheHiveEffect extends Effect {
    private final static EntityPredicate SEE_THROUGH_WALLS = (new EntityPredicate()).allowUnseeable();

    public ProtectionOfTheHiveEffect(EffectType type, int potionColor) {
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
    public void addAttributeModifiers(LivingEntity entity, AttributeModifierManager attributes, int amplifier) {

        SEE_THROUGH_WALLS.range(BzBeeAggressionConfigs.aggressionTriggerRadius.get()*0.5D);
        List<BeeEntity> beeList = entity.level.getNearbyEntities(BeeEntity.class, SEE_THROUGH_WALLS, entity, entity.getBoundingBox().inflate(BzBeeAggressionConfigs.aggressionTriggerRadius.get()*0.5D));

        for (BeeEntity bee : beeList)
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
       if(entity.hurtTime > 0 && entity.getLastHurtByMob() != null) {
           resetBeeAngry(entity.level, entity.getLastHurtByMob());

           if(!(entity.getLastHurtByMob() instanceof BeeEntity)) {
               if(entity instanceof ServerPlayerEntity) {
                   entity.getLastHurtByMob().addEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE.get(), BzBeeAggressionConfigs.howLongWrathOfTheHiveLasts.get(), amplifier, true, true, true));
                   BzCriterias.PROTECTION_OF_THE_HIVE_DEFENSE_TRIGGER.trigger((ServerPlayerEntity) entity, entity.getLastHurtByMob());
               }
           }
        }
    }

    /**
     * Changes the entity Bees are angry at.
     */
    public static void resetBeeAngry(World world, LivingEntity livingEntity)
    {
        LivingEntity entity = livingEntity;
        UUID uuid = entity.getUUID();

        SEE_THROUGH_WALLS.range(BzBeeAggressionConfigs.aggressionTriggerRadius.get()*0.5D);
        List<BeeEntity> beeList = world.getNearbyEntities(BeeEntity.class, SEE_THROUGH_WALLS, entity, entity.getBoundingBox().inflate(BzBeeAggressionConfigs.aggressionTriggerRadius.get()*0.5D));

        if(livingEntity instanceof BeeEntity){
            entity = null;
            uuid = null;
        }

        for (BeeEntity bee : beeList)
        {
            bee.setTarget(entity);
            bee.setPersistentAngerTarget(uuid);
            if(entity == null){
                bee.setRemainingPersistentAngerTime(0);
            }
        }
    }
}
