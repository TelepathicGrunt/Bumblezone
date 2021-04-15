package com.telepathicgrunt.the_bumblezone.effects;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;


public class ProtectionOfTheHiveEffect extends Effect {
    private final static EntityPredicate SEE_THROUGH_WALLS = (new EntityPredicate()).setLineOfSiteRequired();

    public ProtectionOfTheHiveEffect(EffectType type, int potionColor) {
        super(type, potionColor);
    }

    /**
     * Returns true if the potion has an instant effect instead of a continuous one (eg Harming)
     */
    public boolean isInstant() {
        return false;
    }

    /**
     * checks if Potion effect is ready to be applied this tick.
     */
    public boolean isReady(int duration, int amplifier) {
        return duration >= 1;
    }

    /**
     * Calm all attacking bees when first applied to the entity
     */
    public void applyAttributesModifiersToEntity(LivingEntity entity, AttributeModifierManager attributes, int amplifier) {

        SEE_THROUGH_WALLS.setDistance(Bumblezone.BzBeeAggressionConfig.aggressionTriggerRadius.get()*0.5D);
        List<BeeEntity> beeList = entity.world.getTargettableEntitiesWithinAABB(BeeEntity.class, SEE_THROUGH_WALLS, entity, entity.getBoundingBox().grow(Bumblezone.BzBeeAggressionConfig.aggressionTriggerRadius.get()*0.5D));

        for (BeeEntity bee : beeList)
        {
            if(bee.getAttackTarget() == entity){
                bee.setAttackTarget(null);
                bee.setAngerTarget(null);
                bee.setAngerTime(0);
            }
        }

        super.applyAttributesModifiersToEntity(entity, attributes, amplifier);
    }

    /**
     * Makes the bees swarm at the attacking entity
     */
    public void performEffect(LivingEntity entity, int amplifier) {
       if(entity.hurtTime > 0 && entity.getRevengeTarget() != null){
           resetBeeAngry(entity.world, entity.getRevengeTarget());

           if(!(entity.getRevengeTarget() instanceof BeeEntity))
                entity.getRevengeTarget().addPotionEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE.get(), Bumblezone.BzBeeAggressionConfig.howLongWrathOfTheHiveLasts.get(), amplifier, true, true, true));
       }
    }

    /**
     * Changes the entity Bees are angry at.
     */
    public static void resetBeeAngry(World world, LivingEntity livingEntity)
    {
        LivingEntity entity = livingEntity;
        UUID uuid = entity.getUniqueID();

        SEE_THROUGH_WALLS.setDistance(Bumblezone.BzBeeAggressionConfig.aggressionTriggerRadius.get()*0.5D);
        List<BeeEntity> beeList = world.getTargettableEntitiesWithinAABB(BeeEntity.class, SEE_THROUGH_WALLS, entity, entity.getBoundingBox().grow(Bumblezone.BzBeeAggressionConfig.aggressionTriggerRadius.get()*0.5D));

        if(livingEntity instanceof BeeEntity){
            entity = null;
            uuid = null;
        }

        for (BeeEntity bee : beeList)
        {
            bee.setAttackTarget(entity);
            bee.setAngerTarget(uuid);
            if(entity == null){
                bee.setAngerTime(0);
            }
        }
    }
}
