package com.telepathicgrunt.the_bumblezone.effects;

import com.telepathicgrunt.the_bumblezone.capabilities.BzCapabilities;
import com.telepathicgrunt.the_bumblezone.capabilities.EntityFlyingSpeed;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class BeenergizedEffect extends MobEffect {

    public BeenergizedEffect(MobEffectCategory type, int potionColor) {
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
     * Make entity have faster flying speeds at first apply
     */
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        super.addAttributeModifiers(entity, attributes, amplifier);
        // Have to do this way as flyingSpeed field doesnt use the attribute for bees.
        if(entity.getAttributes().hasAttribute(Attributes.FLYING_SPEED)) {
            EntityFlyingSpeed capability = entity.getCapability(BzCapabilities.ORIGINAL_FLYING_SPEED_CAPABILITY).orElseThrow(RuntimeException::new);
            capability.setOriginalFlyingSpeed(entity.flyingSpeed);
            // -0.6 because bee's base flying speed is 0.6f which is crazy high compared to 0.02f default
            entity.flyingSpeed = ((float)(entity.getAttributeValue(Attributes.FLYING_SPEED)) - 0.6f);
        }
    }

    /**
     * Remove effect when finished.
     */
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        super.removeAttributeModifiers(entity, attributes, amplifier);
        if(entity.getAttributes().hasAttribute(Attributes.FLYING_SPEED)) {
            EntityFlyingSpeed capability = entity.getCapability(BzCapabilities.ORIGINAL_FLYING_SPEED_CAPABILITY).orElseThrow(RuntimeException::new);
            entity.flyingSpeed = capability.getOriginalFlyingSpeed();
        }
    }
}
