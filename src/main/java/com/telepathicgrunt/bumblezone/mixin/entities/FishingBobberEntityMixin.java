package com.telepathicgrunt.bumblezone.mixin.entities;

import com.telepathicgrunt.bumblezone.tags.BzFluidTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin extends Entity {

    public FishingBobberEntityMixin(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick()V",
            at = @At(value = "INVOKE", ordinal = 0, shift = At.Shift.BY, by = -6,
                    target = "net/minecraft/entity/projectile/FishingBobberEntity.getVelocity()Lnet/minecraft/util/math/Vec3d;"))
    private void thebumblezone_bobberFloat(CallbackInfo ci) {
        BlockPos blockpos = this.getBlockPos();
        FluidState fluidstate = this.world.getFluidState(blockpos);
        if (fluidstate.isIn(BzFluidTags.BZ_HONEY_FLUID)) {
            Vec3d vector3d = this.getVelocity();
            this.setVelocity(vector3d.x * 0.5D, 0, vector3d.z * 0.5D);
        }
    }
}