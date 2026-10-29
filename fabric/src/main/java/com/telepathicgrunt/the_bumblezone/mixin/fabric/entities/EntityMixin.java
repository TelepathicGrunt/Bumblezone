package com.telepathicgrunt.the_bumblezone.mixin.fabric.entities;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.telepathicgrunt.the_bumblezone.events.entity.BzEntityTravelingToDimensionEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityFluidInteraction;
import net.minecraft.world.level.portal.TeleportTransition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    protected boolean wasTouchingWater;

    @Final
    @Shadow
    private EntityFluidInteraction fluidInteraction;

    @Shadow
    public abstract void resetFallDistance();

    @Inject(method = "teleport",
            at = @At("HEAD"),
            cancellable = true)
    private void bumblezone$onChangeDimension(TeleportTransition dimensionTransition, CallbackInfoReturnable<Entity> cir) {
        if (BzEntityTravelingToDimensionEvent.EVENT.invoke(new BzEntityTravelingToDimensionEvent(dimensionTransition.newLevel().dimension(), (Entity)(Object)this))) {
            cir.setReturnValue(null);
        }
    }

    @ModifyReturnValue(method = "updateFluidInteraction()Z",
            at = @At(value = "RETURN"))
    private boolean bumblezone$fluidDetectionAndWaterLike(boolean original) {
        boolean inHoney = fluidInteraction.getFluidHeight(BzTags.SPECIAL_HONEY_LIKE) > 0.0;
        if (inHoney) {
            this.resetFallDistance();
            this.wasTouchingWater = true;
            if (((Entity)(Object)this).isPushedByFluid()) {
                this.fluidInteraction.applyCurrentTo(BzTags.SPECIAL_HONEY_LIKE, ((Entity)(Object)this), 0.014);
            }
        }
        return original || inHoney;
    }
}
