package com.telepathicgrunt.the_bumblezone.effects;

import com.telepathicgrunt.the_bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.the_bumblezone.configs.BzBeeAggressionConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.bee.Bee;

import java.util.List;

public class HiddenEffect extends BzEffect {
    private final static TargetingConditions SEE_THROUGH_WALLS = (TargetingConditions.forCombat()).ignoreLineOfSight().ignoreInvisibilityTesting();

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
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity livingEntity, int amplifier) {
        super.applyEffectTick(serverLevel, livingEntity, amplifier);

        Registry<MobEffect> mobEffects = serverLevel.registryAccess().getOrThrow(Registries.MOB_EFFECT).value();
        Holder.Reference<MobEffect> hiddenEffectReference = mobEffects.get(BzEffects.HIDDEN.getId()).get();
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
        if (amplifier >= 1 && livingEntity.level() instanceof ServerLevel serverLevel) {
            SEE_THROUGH_WALLS.range(BzBeeAggressionConfigs.aggressionTriggerRadius * 0.5D);
            List<Bee> beeList = serverLevel.getNearbyEntities(
                    Bee.class,
                    SEE_THROUGH_WALLS,
                    livingEntity,
                    livingEntity.getBoundingBox().inflate(BzBeeAggressionConfigs.aggressionTriggerRadius * 0.5D));

            for (Bee bee : beeList) {
                if(bee.getTarget() == livingEntity) {
                    bee.setTarget(null);
                    bee.setPersistentAngerTarget(null);
                    bee.setTimeToRemainAngry(0);
                }
            }
        }

        super.onEffectStarted(livingEntity, amplifier);
    }

    public static double hideEntity(LivingEntity livingEntity) {
        Registry<MobEffect> mobEffects = livingEntity.level().registryAccess().getOrThrow(Registries.MOB_EFFECT).value();
        Holder.Reference<MobEffect> hiddenEffectReference = mobEffects.get(BzEffects.HIDDEN.getId()).get();
        if (livingEntity.hasEffect(hiddenEffectReference)) {
            return 0;
        }
        return 1; // Due to the `* original value`, this will not modify visibility
    }
}
