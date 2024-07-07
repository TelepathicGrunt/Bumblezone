package com.telepathicgrunt.the_bumblezone.effects;

import com.telepathicgrunt.the_bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.the_bumblezone.configs.BzBeeAggressionConfigs;
import com.telepathicgrunt.the_bumblezone.events.entity.BzEntityVisibilityEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
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
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration >= 1;
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        super.applyEffectTick(livingEntity, amplifier);

        Registry<MobEffect> mobEffects = livingEntity.level().registryAccess().registryOrThrow(Registries.MOB_EFFECT);
        Holder.Reference<MobEffect> hiddenEffectReference = mobEffects.getHolder(BzEffects.HIDDEN.getId()).get();
        MobEffectInstance effect = livingEntity.getEffect(hiddenEffectReference);
        if (effect != null && effect.getDuration() <= 1) {
            PileOfPollen.reapplyHiddenEffectIfInsidePollenPile(livingEntity);
        }
        return true;
    }

    /**
     * Calm all attacking bees at this entity when first applied to the entity
     */
    @Override
    public void onEffectStarted(LivingEntity livingEntity, int amplifier) {
        if (amplifier >= 1) {
            SEE_THROUGH_WALLS.range(BzBeeAggressionConfigs.aggressionTriggerRadius * 0.5D);
            List<Bee> beeList = livingEntity.level().getNearbyEntities(Bee.class, SEE_THROUGH_WALLS, livingEntity, livingEntity.getBoundingBox().inflate(BzBeeAggressionConfigs.aggressionTriggerRadius * 0.5D));

            for (Bee bee : beeList) {
                if(bee.getTarget() == livingEntity) {
                    bee.setTarget(null);
                    bee.setPersistentAngerTarget(null);
                    bee.setRemainingPersistentAngerTime(0);
                }
            }
        }

        super.onEffectStarted(livingEntity, amplifier);
    }

    public static void hideEntity(BzEntityVisibilityEvent event) {
        Registry<MobEffect> mobEffects = event.entity().level().registryAccess().registryOrThrow(Registries.MOB_EFFECT);
        Holder.Reference<MobEffect> hiddenEffectReference = mobEffects.getHolder(BzEffects.HIDDEN.getId()).get();
        MobEffectInstance hiddenEffect = event.entity().getEffect(hiddenEffectReference);
        if(hiddenEffect != null) {
            event.modify(0);
        }
    }
}
