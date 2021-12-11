package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.tags.BzFluidTags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract boolean updateFluidHeightAndDoFluidPushing(Tag<Fluid> fluidITag, double v);

    @Shadow
    public abstract boolean isEyeInFluid(Tag<Fluid> fluidITag);

    @Shadow
    public abstract void clearFire();

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getZ();

    @Shadow
    protected boolean wasTouchingWater;

    @Shadow
    protected boolean wasEyeInWater;

    @Shadow
    public float fallDistance;

    @Shadow
    public Level level;

    @Shadow
    protected Tag<Fluid> fluidOnEyes;

    // let honey fluid push entity
    @Inject(method = "updateInWaterStateAndDoWaterCurrentPushing()V",
            at = @At(value = "TAIL"))
    private void thebumblezone_fluidPushing(CallbackInfo ci) {
        if (this.updateFluidHeightAndDoFluidPushing(BzFluidTags.BZ_HONEY_FLUID, 0.014D)) {
            this.fallDistance = 0.0F;
            this.wasTouchingWater = true;
            this.clearFire();
        }
    }

    // make sure we set that we are in fluid
    @Inject(method = "updateFluidOnEyes()V",
            at = @At(value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/world/entity/Entity;isEyeInFluid(Lnet/minecraft/tags/Tag;)Z",
                    shift = At.Shift.AFTER))
    private void thebumblezone_markEyesInFluid(CallbackInfo ci) {
        if(!this.wasEyeInWater) {
            this.wasEyeInWater = this.isEyeInFluid(BzFluidTags.BZ_HONEY_FLUID);
        }
    }

    @Inject(method = "updateFluidOnEyes()V",
            at = @At(value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/world/level/Level;getFluidState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/FluidState;",
                    shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    private void thebumblezone_markEyesInFluid2(CallbackInfo ci, double eyeHeight) {
        // Have to get the fluid myself as the local capture here is uh broken. Dies on the vehicle entity variable
        BlockPos blockPos = new BlockPos(this.getX(), eyeHeight, this.getZ());
        FluidState fluidState = this.level.getFluidState(blockPos);
        if (fluidState.is(BzFluidTags.BZ_HONEY_FLUID)) {
            double fluidHeight = (float)blockPos.getY() + fluidState.getHeight(this.level, blockPos);
            if (fluidHeight > eyeHeight) {
                this.fluidOnEyes = BzFluidTags.BZ_HONEY_FLUID;
                ci.cancel();
            }
        }
    }
}