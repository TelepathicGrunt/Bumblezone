package com.telepathicgrunt.the_bumblezone.effects;

import com.telepathicgrunt.the_bumblezone.configs.BzBeeAggressionConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Bee;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.List;

public class HiddenEffect extends MobEffect {
    private final static TargetingConditions SEE_THROUGH_WALLS = (TargetingConditions.forCombat()).ignoreLineOfSight();

    public HiddenEffect(MobEffectCategory type, int potionColor) {
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
        SEE_THROUGH_WALLS.range(BzBeeAggressionConfigs.aggressionTriggerRadius.get()*0.5D);
        List<Bee> beeList = entity.level.getNearbyEntities(Bee.class, SEE_THROUGH_WALLS, entity, entity.getBoundingBox().inflate(BzBeeAggressionConfigs.aggressionTriggerRadius.get()*0.5D));

        for (Bee bee : beeList) {
            if(bee.getTarget() == entity) {
                bee.setTarget(null);
                bee.setPersistentAngerTarget(null);
                bee.setRemainingPersistentAngerTime(0);
            }
        }

        super.addAttributeModifiers(entity, attributes, amplifier);
    }

    public static void hideEntity(LivingEvent.LivingVisibilityEvent event) {
        MobEffectInstance hiddenEffect = event.getEntityLiving().getEffect(BzEffects.HIDDEN.get());
        if(hiddenEffect != null) {
            if(hiddenEffect.getAmplifier() >= 1) {
                event.modifyVisibility(0);
            }
            else {
                event.modifyVisibility(0.5);
            }
        }
    }
}
