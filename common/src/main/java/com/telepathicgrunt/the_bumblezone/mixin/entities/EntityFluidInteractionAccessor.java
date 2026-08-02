package com.telepathicgrunt.the_bumblezone.mixin.entities;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityFluidInteraction;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(EntityFluidInteraction.class)
public interface EntityFluidInteractionAccessor {
    @Accessor("trackerByFluid")
    Map<TagKey<Fluid>, EntityFluidInteraction.Tracker> bumblezone$getTrackerByFluid();
}
