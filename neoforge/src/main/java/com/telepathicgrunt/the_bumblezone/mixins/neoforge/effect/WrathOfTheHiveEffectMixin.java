package com.telepathicgrunt.the_bumblezone.mixins.neoforge.effect;

import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.common.EffectCure;
import net.neoforged.neoforge.common.EffectCures;
import net.neoforged.neoforge.common.extensions.IMobEffectExtension;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Set;

@Mixin(WrathOfTheHiveEffect.class)
public interface WrathOfTheHiveEffectMixin extends IMobEffectExtension {

    @Override
    default void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
        IMobEffectExtension.super.fillEffectCures(cures, effectInstance);
        cures.remove(EffectCures.MILK);
    }
}
