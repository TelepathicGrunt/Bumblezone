package com.telepathicgrunt.the_bumblezone.mixin.entities;

import net.minecraft.world.entity.ExperienceOrb;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ExperienceOrb.class)
public interface ExperienceOrbInvoker {

    @Invoker("setValue")
    void bumblezone$setValue(int value);
}
