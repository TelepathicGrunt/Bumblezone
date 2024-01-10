package com.telepathicgrunt.the_bumblezone.mixin.neoforge.effect;

import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.common.EffectCure;
import net.neoforged.neoforge.common.EffectCures;
import net.neoforged.neoforge.common.extensions.IMobEffectExtension;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Set;

@Mixin(WrathOfTheHiveEffect.class)
public class WrathOfTheHiveEffectMixin extends MobEffect implements IMobEffectExtension {
    protected WrathOfTheHiveEffectMixin(MobEffectCategory arg, int i) {
        super(arg, i);
    }

    @Override
    public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
        super.fillEffectCures(cures, effectInstance);
        cures.remove(EffectCures.MILK);
    }
}
