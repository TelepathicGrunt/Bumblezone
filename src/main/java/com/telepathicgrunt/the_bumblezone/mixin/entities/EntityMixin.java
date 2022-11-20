package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.telepathicgrunt.the_bumblezone.items.StinglessBeeHelmet;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public class EntityMixin {

    @ModifyVariable(method = "positionRider(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity$MoveFunction;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getX()D"),
            require = 0)
    private double thebumblezone_beeRidingOffset(double yOffset, Entity entity) {
        return StinglessBeeHelmet.beeRidingOffset(yOffset, ((Entity)(Object)this), entity);
    }

    @ModifyReturnValue(method = "updateFluidHeightAndDoFluidPushing(Lnet/minecraft/tags/TagKey;D)Z",
            at = @At(value = "RETURN"),
            require = 0)
    private boolean thebumblezone_applyMissingWaterPhysicsForSugarWaterFluid(boolean appliedFluidPush) {
        if(!appliedFluidPush) {
            return ((Entity)(Object)this).isInFluidType(BzFluids.SUGAR_WATER_FLUID_TYPE.get());
        }
        return appliedFluidPush;
    }
}