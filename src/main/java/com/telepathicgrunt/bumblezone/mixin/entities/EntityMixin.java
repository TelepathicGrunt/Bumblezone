package com.telepathicgrunt.bumblezone.mixin.entities;

import com.telepathicgrunt.bumblezone.entities.EntityTeleportationBackend;
import com.telepathicgrunt.bumblezone.tags.BzFluidTags;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract boolean updateMovementInFluid(Tag<Fluid> fluidITag, double v);

    @Shadow
    public abstract boolean isSubmergedIn(Tag<Fluid> fluidITag);

    @Shadow
    public abstract boolean isSwimming();

    @Shadow
    public abstract boolean isSprinting();

    @Shadow
    public abstract boolean isSubmergedInWater();

    @Shadow
    public abstract boolean hasVehicle();

    @Shadow
    public abstract BlockPos getBlockPos();

    @Shadow
    public abstract void setSwimming(boolean isSwimming);

    @Shadow
    public abstract void extinguish();

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getZ();

    @Shadow
    protected boolean touchingWater;

    @Shadow
    protected boolean submergedInWater;

    @Shadow
    public float fallDistance;

    @Shadow
    protected Tag<Fluid> submergedFluidTag;

    @Shadow
    public World world;

    // Handles storing of past non-bumblezone dimension the entity is leaving
    @Inject(method = "moveToWorld(Lnet/minecraft/server/world/ServerWorld;)Lnet/minecraft/entity/Entity;",
            at = @At(value = "HEAD"))
    private void thebumblezone_onDimensionChange(ServerWorld destination, CallbackInfoReturnable<Entity> cir) {
        EntityTeleportationBackend.playerLeavingBz(world.getRegistryKey().getValue(), ((Entity)(Object)this));
    }


    // let honey fluid push entity
    @Inject(method = "checkWaterState()V",
            at = @At(value = "TAIL"))
    private void thebumblezone_fluidPushing(CallbackInfo ci) {
        if (this.updateMovementInFluid(BzFluidTags.BZ_HONEY_FLUID, 0.014D)) {
            this.fallDistance = 0.0F;
            this.touchingWater = true;
            this.extinguish();
        }
    }

    // make sure we set that we are in fluid
    @Inject(method = "updateSubmergedInWaterState()V",
            at = @At(value = "INVOKE_ASSIGN",
                    target = "net/minecraft/entity/Entity.isSubmergedIn(Lnet/minecraft/tag/Tag;)Z",
                    shift = At.Shift.AFTER))
    private void thebumblezone_markEyesInFluid(CallbackInfo ci) {
        if(!this.submergedInWater) {
            this.submergedInWater = this.isSubmergedIn(BzFluidTags.BZ_HONEY_FLUID);
        }
    }

    @Inject(method = "updateSubmergedInWaterState()V",
            at = @At(value = "INVOKE_ASSIGN",
                    target = "net/minecraft/world/World.getFluidState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/fluid/FluidState;",
                    shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    private void thebumblezone_markEyesInFluid2(CallbackInfo ci, double eyeHeight) {
        // Have to get the fluid myself as the local capture here is uh broken. Dies on the vehicle entity variable
        BlockPos blockPos = new BlockPos(this.getX(), eyeHeight, this.getZ());
        FluidState fluidState = this.world.getFluidState(blockPos);
        if (fluidState.isIn(BzFluidTags.BZ_HONEY_FLUID)) {
            double fluidHeight = (float)blockPos.getY() + fluidState.getHeight(this.world, blockPos);
            if (fluidHeight > eyeHeight) {
                this.submergedFluidTag = BzFluidTags.BZ_HONEY_FLUID;
                ci.cancel();
            }
        }
    }

    // let honey fluid push entity
    @Inject(method = "updateSwimming()V",
            at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.setSwimming(Z)V", ordinal = 1, shift = At.Shift.AFTER))
    private void thebumblezone_setSwimming(CallbackInfo ci) {
        // check if we were not set to swimming in water. If not, then check if we are swimming in honey fluid instead
        if(!this.isSwimming() && this.isSprinting() && this.isSubmergedInWater() && !this.hasVehicle()){
            this.setSwimming(this.world.getFluidState(this.getBlockPos()).isIn(BzFluidTags.BZ_HONEY_FLUID));
        }
    }
}