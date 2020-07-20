package net.telepathicgrunt.bumblezone.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.Bumblezone;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class ProtectionOfTheHiveEffect extends StatusEffect {
    private final static TargetPredicate SEE_THROUGH_WALLS = (new TargetPredicate()).includeHidden();

    public ProtectionOfTheHiveEffect(StatusEffectType type, int potionColor) {
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
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration >= 1;
    }

    /**
     * Calm all attacking bees when first applied to the entity
     */
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {

        SEE_THROUGH_WALLS.setBaseMaxDistance(Bumblezone.BZ_CONFIG.aggressionTriggerRadius*0.5D);
        List<BeeEntity> beeList = entity.world.getTargets(BeeEntity.class, SEE_THROUGH_WALLS, entity, entity.getBoundingBox().expand(Bumblezone.BZ_CONFIG.aggressionTriggerRadius*0.5D));

        for (BeeEntity bee : beeList)
        {
            if(bee.getTarget() == entity){
                bee.setTarget(null);
                bee.setAngryAt(null);
                bee.setAngerTime(0);
            }
        }

        super.onApplied(entity, attributes, amplifier);
    }

    /**
     * Makes the bees swarm at the attacking entity
     */
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
       if(entity.hurtTime > 0 && entity.getAttacker() != null){
           resetBeeAngry(entity.world, entity.getAttacker());

           if(!(entity.getAttacker() instanceof BeeEntity))
                entity.getAttacker().addStatusEffect(new StatusEffectInstance(BzEffects.WRATH_OF_THE_HIVE, Bumblezone.BZ_CONFIG.howLongWrathOfTheHiveLasts, amplifier, true, true, true));
       }
    }

    /**
     * Changes the entity Bees are angry at.
     */
    public static void resetBeeAngry(World world, LivingEntity livingEntity)
    {
        LivingEntity entity = livingEntity;
        UUID uuid = entity.getUuid();

        SEE_THROUGH_WALLS.setBaseMaxDistance(Bumblezone.BZ_CONFIG.aggressionTriggerRadius*0.5D);
        List<BeeEntity> beeList = world.getTargets(BeeEntity.class, SEE_THROUGH_WALLS, entity, entity.getBoundingBox().expand(Bumblezone.BZ_CONFIG.aggressionTriggerRadius*0.5D));

        if(livingEntity instanceof BeeEntity){
            entity = null;
            uuid = null;
        }

        for (BeeEntity bee : beeList)
        {
            bee.setTarget(entity);
            bee.setAngryAt(uuid);
            if(entity == null){
                bee.setAngerTime(0);
            }
        }
    }
}
