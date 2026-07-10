package com.telepathicgrunt.the_bumblezone.mixin.fabric.entities;

import com.telepathicgrunt.the_bumblezone.events.entity.BzEntityTravelingToDimensionEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.TeleportTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "teleport",
            at = @At("HEAD"),
            cancellable = true)
    private void bumblezone$onChangeDimension(TeleportTransition dimensionTransition, CallbackInfoReturnable<Entity> cir) {
        if (BzEntityTravelingToDimensionEvent.EVENT.invoke(new BzEntityTravelingToDimensionEvent(dimensionTransition.newLevel().dimension(), (Entity)(Object)this))) {
            cir.setReturnValue(null);
        }
    }
}
