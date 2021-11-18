package com.telepathicgrunt.the_bumblezone.effects;

import com.telepathicgrunt.the_bumblezone.capabilities.EntityFlyingSpeed;
import com.telepathicgrunt.the_bumblezone.capabilities.IFlyingSpeed;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;


public class BeenergizedEffect extends Effect {
    @CapabilityInject(IFlyingSpeed.class)
    public static Capability<IFlyingSpeed> ORIGINAL_FLYING_SPEED = null;

    public BeenergizedEffect(EffectType type, int potionColor) {
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
    public void addAttributeModifiers(LivingEntity entity, AttributeModifierManager attributes, int amplifier) {
        super.addAttributeModifiers(entity, attributes, amplifier);
        // Have to do this way as flyingSpeed field doesnt use the attribute for bees.
        if(entity.getAttributes().hasAttribute(Attributes.FLYING_SPEED)) {
            EntityFlyingSpeed cap = (EntityFlyingSpeed) entity.getCapability(ORIGINAL_FLYING_SPEED).orElseThrow(RuntimeException::new);
            cap.setOriginalFlyingSpeed(entity.flyingSpeed);
            // -0.6 because bee's base flying speed is 0.6f which is crazy high compared to 0.02f default
            entity.flyingSpeed = ((float)(entity.getAttributeValue(Attributes.FLYING_SPEED)) - 0.6f);
        }
    }

    /**
     * Remove effect when finished.
     */
    public void removeAttributeModifiers(LivingEntity entity, AttributeModifierManager attributes, int amplifier) {
        super.removeAttributeModifiers(entity, attributes, amplifier);
        if(entity.getAttributes().hasAttribute(Attributes.FLYING_SPEED)) {
            EntityFlyingSpeed cap = (EntityFlyingSpeed) entity.getCapability(ORIGINAL_FLYING_SPEED).orElseThrow(RuntimeException::new);
            entity.flyingSpeed = cap.getOriginalFlyingSpeed();
        }
    }
}
