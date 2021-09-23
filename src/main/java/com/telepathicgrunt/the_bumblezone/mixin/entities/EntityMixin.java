package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.tags.BzFluidTags;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.tags.ITag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract boolean updateFluidHeightAndDoFluidPushing(ITag<Fluid> fluidITag, double v);

    @Shadow
    public abstract boolean isEyeInFluid(ITag<Fluid> fluidITag);

    @Shadow
    public abstract void clearFire();

    @Shadow
    protected boolean wasTouchingWater;

    @Shadow
    protected boolean wasEyeInWater;

    @Shadow
    public float fallDistance;

    @Shadow
    public World level;

    @Shadow
    protected ITag<Fluid> fluidOnEyes;

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
                    target = "net/minecraft/entity/Entity.isEyeInFluid(Lnet/minecraft/tags/ITag;)Z",
                    shift = At.Shift.AFTER))
    private void thebumblezone_markEyesInFluid(CallbackInfo ci) {
        if(!this.wasEyeInWater) {
            this.wasEyeInWater = this.isEyeInFluid(BzFluidTags.BZ_HONEY_FLUID);
        }
    }

    @Inject(method = "updateFluidOnEyes()V",
            at = @At(value = "INVOKE_ASSIGN",
                    target = "net/minecraft/world/World.getFluidState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/fluid/FluidState;",
                    shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    private void thebumblezone_markEyesInFluid2(CallbackInfo ci, double eyeHeight, Entity entity, BlockPos blockpos, FluidState fluidstate) {
        if (fluidstate.is(BzFluidTags.BZ_HONEY_FLUID)) {
            double fluidHeight = (float)blockpos.getY() + fluidstate.getHeight(this.level, blockpos);
            if (fluidHeight > eyeHeight) {
                this.fluidOnEyes = BzFluidTags.BZ_HONEY_FLUID;
                ci.cancel();
            }
        }
    }
}