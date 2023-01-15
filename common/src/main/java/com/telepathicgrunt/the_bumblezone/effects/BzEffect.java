package com.telepathicgrunt.the_bumblezone.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * This is a base class used in a mixin to add Forge rendering to the effect.
 */
public class BzEffect extends MobEffect {
    protected BzEffect(MobEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }
}
