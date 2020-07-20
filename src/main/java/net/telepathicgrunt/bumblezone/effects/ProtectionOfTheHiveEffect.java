package net.telepathicgrunt.bumblezone.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;


public class ProtectionOfTheHiveEffect extends StatusEffect {
    public ProtectionOfTheHiveEffect(StatusEffectType type, int potionColor) {
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
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration >= 1;
    }

    /**
     * Makes the bees swarm at the attacking entity
     */
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
       if(entity.hurtTime > 0 && entity.getAttacker() instanceof LivingEntity){
           entity.getAttacker().addStatusEffect(new StatusEffectInstance(BzEffects.WRATH_OF_THE_HIVE, 40, amplifier, false, false));
       }
    }
}
