package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.tags.BzFluidTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingHook.class)
public abstract class FishingBobberEntityMixin extends Entity {

    public FishingBobberEntityMixin(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "tick()V",
            at = @At(value = "INVOKE", ordinal = 0, shift = At.Shift.BY, by = -6,
                    target = "Lnet/minecraft/world/entity/projectile/FishingHook;getDeltaMovement()Lnet/minecraft/world/phys/Vec3;"))
    private void thebumblezone_bobberFloat(CallbackInfo ci) {
        BlockPos blockpos = this.blockPosition();
        FluidState fluidstate = this.level.getFluidState(blockpos);
        if (fluidstate.is(BzFluidTags.BZ_HONEY_FLUID)) {
            Vec3 vector3d = this.getDeltaMovement();
            this.setDeltaMovement(vector3d.x * 0.5D, 0, vector3d.z * 0.5D);
        }
    }
}