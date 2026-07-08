package com.telepathicgrunt.the_bumblezone.mixin.entities;

import net.minecraft.world.entity.EntityFluidInteraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityFluidInteraction.Tracker.class)
public interface TrackerAccessor {
    @Accessor("eyesInside")
    boolean bumblezone$isEyesInside();
}
