package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FishingHook.class)
public abstract class FishingBobberEntityMixin extends Entity {

    public FishingBobberEntityMixin(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }

    @WrapOperation(method = "tick()V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z",
                    ordinal = 0),
            require = 0)
    private boolean thebumblezone_bobberFloat(FluidState fluidstate, TagKey<Fluid> tagKey, Operation<Boolean> original) {
        if(fluidstate.is(BzTags.SPECIAL_HONEY_LIKE)) {
            Vec3 vector3d = this.getDeltaMovement();
            this.setDeltaMovement(vector3d.x * 0.5D, 0, vector3d.z * 0.5D);
        }
        return original.call(fluidstate, tagKey);
    }
}