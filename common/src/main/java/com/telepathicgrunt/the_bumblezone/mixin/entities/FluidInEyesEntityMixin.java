package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.platform.BzEntityHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Entity.class)
public class FluidInEyesEntityMixin implements BzEntityHooks {

    @Unique
    private FluidState bz$eyeFluidState = null;

    @Inject(method = "updateFluidOnEyes", at = @At("HEAD"))
    public void bumblezone$onSetFluidInEyes(CallbackInfo ci) {
        bz$eyeFluidState = Fluids.EMPTY.defaultFluidState();
    }

    @Inject(
            method = "updateFluidOnEyes",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;getTags()Ljava/util/stream/Stream;"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void bumblezone$onSetFluidInEyes(CallbackInfo ci, double d, Entity entity, BlockPos blockPos, FluidState fluidState, double e) {
        bz$eyeFluidState = fluidState;
    }

    @Override
    public FluidState bz$getFluidOnEyes() {
        return bz$eyeFluidState;
    }
}
