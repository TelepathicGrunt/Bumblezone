package com.telepathicgrunt.the_bumblezone.effects;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Bee;

import java.util.List;

public class HiddenEffect extends MobEffect {
    private final static TargetingConditions SEE_THROUGH_WALLS = (TargetingConditions.forCombat()).ignoreLineOfSight();

    public HiddenEffect(MobEffectCategory type, int potionColor) {
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
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration >= 1;
    }

    /**
     * Calm all attacking bees at this entity when first applied to the entity
     */
    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        SEE_THROUGH_WALLS.range(BzConfig.aggressionTriggerRadius*0.5D);
        List<Bee> beeList = entity.level.getNearbyEntities(Bee.class, SEE_THROUGH_WALLS, entity, entity.getBoundingBox().inflate(BzConfig.aggressionTriggerRadius*0.5D));
        for (Bee bee : beeList) {
            if(bee.getTarget() == entity) {
                bee.setTarget(null);
                bee.setPersistentAngerTarget(null);
                bee.setRemainingPersistentAngerTime(0);
            }
        }

        super.addAttributeModifiers(entity, attributes, amplifier);
    }

    public static double hideEntity(Entity entity, double currentVisibilty) {
        if(entity instanceof LivingEntity livingEntity) {
            MobEffectInstance hiddenEffect = livingEntity.getEffect(BzEffects.HIDDEN);
            if(hiddenEffect != null) {
                if(hiddenEffect.getAmplifier() >= 1) {
                    return 0;
                }
                else {
                    return 0.5;
                }
            }
        }
        return currentVisibilty;
    }
}
