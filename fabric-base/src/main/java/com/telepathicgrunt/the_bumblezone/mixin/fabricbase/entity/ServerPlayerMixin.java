package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.entity;

import com.telepathicgrunt.the_bumblezone.events.entity.EntityTravelingToDimensionEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {


    @Inject(
            method = "changeDimension",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bumblezone$onTravelToDimension(ServerLevel serverLevel, CallbackInfoReturnable<Entity> cir) {
        if (EntityTravelingToDimensionEvent.EVENT.invoke(new EntityTravelingToDimensionEvent(serverLevel.dimension(), (ServerPlayer)(Object)this))) {
            cir.setReturnValue(null);
        }
    }

    @Inject(
            method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;getLevel()Lnet/minecraft/server/level/ServerLevel;"
            ),
            cancellable = true
    )
    private void bumblezone$onTeleportTo(ServerLevel serverLevel, double d, double e, double f, float g, float h, CallbackInfo ci) {
        if (EntityTravelingToDimensionEvent.EVENT.invoke(new EntityTravelingToDimensionEvent(serverLevel.dimension(), (ServerPlayer)(Object)this))) {
            ci.cancel();
        }
    }
}
