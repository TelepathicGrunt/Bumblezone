package com.telepathicgrunt.the_bumblezone.mixins.fabric.entities;

import com.telepathicgrunt.the_bumblezone.events.entity.EntityTravelingToDimensionEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(
            method = "changeDimension",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bumblezone$onChangeDimension(ServerLevel serverLevel, CallbackInfoReturnable<Entity> cir) {
        if (EntityTravelingToDimensionEvent.EVENT.invoke(new EntityTravelingToDimensionEvent(serverLevel.dimension(), (Entity)(Object)this))) {
            cir.setReturnValue(null);
        }
    }
}
