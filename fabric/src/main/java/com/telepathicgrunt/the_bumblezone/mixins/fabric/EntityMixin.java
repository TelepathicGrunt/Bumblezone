package com.telepathicgrunt.the_bumblezone.mixins.fabric;

import com.llamalad7.mixinextras.sugar.Local;
import com.telepathicgrunt.the_bumblezone.fabric.hooks.BzEntityHooks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin implements BzEntityHooks {

    @Unique
    private FluidState bz$eyeFluidState = null;

    @Inject(method = "updateFluidOnEyes", at = @At("HEAD"))
    public void bumblezone$onSetFluidInEyes(CallbackInfo ci) {
        bz$eyeFluidState = Fluids.EMPTY.defaultFluidState();
    }

    @Inject(method = "updateFluidOnEyes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;getTags()Ljava/util/stream/Stream;"))
    public void bumblezone$onSetFluidInEyes(CallbackInfo ci, @Local(ordinal = 0, name = "fluidstate") FluidState fluidState) {
        bz$eyeFluidState = fluidState;
    }

    @Override
    public FluidState bz$getFluidOnEyes() {
        return bz$eyeFluidState;
    }
}
