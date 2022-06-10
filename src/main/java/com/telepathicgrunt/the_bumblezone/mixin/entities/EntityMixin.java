package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.items.StinglessBeeHelmet;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Set;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract boolean updateFluidHeightAndDoFluidPushing(TagKey<Fluid> fluidITag, double v);

    @Shadow
    public abstract boolean isEyeInFluid(TagKey<Fluid> fluidITag);

    @Shadow
    public abstract boolean isSwimming();

    @Shadow
    public abstract boolean isSprinting();

    @Shadow
    public abstract boolean isUnderWater();

    @Shadow
    public abstract boolean isPassenger();

    @Shadow
    public abstract BlockPos blockPosition();

    @Shadow
    public abstract void setSwimming(boolean isSwimming);

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

    @Mutable
    @Final
    @Shadow
    private Set<TagKey<Fluid>> fluidOnEyes;

    // let honey fluid push entity
    @Inject(method = "updateInWaterStateAndDoWaterCurrentPushing()V",
            at = @At(value = "TAIL"))
    private void thebumblezone_fluidPushing(CallbackInfo ci) {
        if (this.updateFluidHeightAndDoFluidPushing(BzTags.BZ_HONEY_FLUID, 0.014D)) {
            this.fallDistance = 0.0F;
            this.wasTouchingWater = true;
            this.clearFire();
        }
    }

    // make sure we set that we are in fluid
    @Inject(method = "updateFluidOnEyes()V",
            at = @At(value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/world/entity/Entity;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z",
                    shift = At.Shift.AFTER))
    private void thebumblezone_markEyesInFluid(CallbackInfo ci) {
        if(!this.wasEyeInWater) {
            this.wasEyeInWater = this.isEyeInFluid(BzTags.BZ_HONEY_FLUID);
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
        if (fluidState.is(BzTags.BZ_HONEY_FLUID)) {
            double fluidHeight = (float)blockPos.getY() + fluidState.getHeight(this.level, blockPos);
            if (fluidHeight > eyeHeight) {
                this.fluidOnEyes.add(BzTags.BZ_HONEY_FLUID);
                ci.cancel();
            }
        }
    }

    // let honey fluid be swimmable
    @Inject(method = "updateSwimming()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setSwimming(Z)V", ordinal = 1, shift = At.Shift.AFTER))
    private void thebumblezone_setSwimming(CallbackInfo ci) {
        // check if we were not set to swimming in water. If not, then check if we are swimming in honey fluid instead
        if(!this.isSwimming() && this.isSprinting() && this.isUnderWater() && !this.isPassenger()) {
            this.setSwimming(this.level.getFluidState(this.blockPosition()).is(BzTags.BZ_HONEY_FLUID));
        }
    }

    @ModifyVariable(method = "positionRider(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity$MoveFunction;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getX()D"))
    private double thebumblezone_beeRidingOffset(double yOffset, Entity entity) {
        return StinglessBeeHelmet.beeRidingOffset(yOffset, ((Entity)(Object)this), entity);
    }
}