package com.telepathicgrunt.the_bumblezone.effects;

import com.telepathicgrunt.the_bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.the_bumblezone.configs.BzBeeAggressionConfigs;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityVisibilityEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Bee;

import java.util.List;

public class HiddenEffect extends BzEffect {
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


    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        super.applyEffectTick(livingEntity, amplifier);

        MobEffectInstance effect = livingEntity.getEffect(BzEffects.HIDDEN.get());
        if (effect != null && effect.getDuration() <= 1) {
            PileOfPollen.reapplyHiddenEffectIfInsidePollenPile(livingEntity);
        }
    }

    /**
     * Calm all attacking bees at this entity when first applied to the entity
     */
    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        if (amplifier >= 1) {
            SEE_THROUGH_WALLS.range(BzBeeAggressionConfigs.aggressionTriggerRadius * 0.5D);
            List<Bee> beeList = entity.level().getNearbyEntities(Bee.class, SEE_THROUGH_WALLS, entity, entity.getBoundingBox().inflate(BzBeeAggressionConfigs.aggressionTriggerRadius * 0.5D));

            for (Bee bee : beeList) {
                if(bee.getTarget() == entity) {
                    bee.setTarget(null);
                    bee.setPersistentAngerTarget(null);
                    bee.setRemainingPersistentAngerTime(0);
                }
            }
        }

        super.addAttributeModifiers(entity, attributes, amplifier);
    }

    public static void hideEntity(EntityVisibilityEvent event) {
        MobEffectInstance hiddenEffect = event.entity().getEffect(BzEffects.HIDDEN.get());
        if(hiddenEffect != null) {
            event.modify(0);
        }
    }
}
