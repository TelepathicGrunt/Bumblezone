package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.entity;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor("fluidOnEyes")
    Set<TagKey<Fluid>> getFluidOnEyes();
}