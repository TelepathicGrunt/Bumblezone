package com.telepathicgrunt.the_bumblezone.mixin.effects;

import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MobEffectInstance.class)
public interface MobEffectInstanceAccessor {
    @Invoker("tickDownDuration")
    int callTickDownDuration();
}
