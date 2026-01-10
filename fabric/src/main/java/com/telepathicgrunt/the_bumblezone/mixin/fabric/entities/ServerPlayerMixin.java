package com.telepathicgrunt.the_bumblezone.mixin.fabric.entities;

import com.telepathicgrunt.the_bumblezone.events.entity.BzEntityTravelingToDimensionEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.level.portal.TeleportTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {


    @Inject(method = "teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/server/level/ServerPlayer;",
            at = @At("HEAD"),
            cancellable = true)
    private void bumblezone$onTravelToDimension(TeleportTransition teleportTransition, CallbackInfoReturnable<Entity> cir) {
        if (!teleportTransition.newLevel().dimension().equals(((ServerPlayer)(Object)this).level().dimension()) &&
            BzEntityTravelingToDimensionEvent.EVENT.invoke(new BzEntityTravelingToDimensionEvent(teleportTransition.newLevel().dimension(), (ServerPlayer)(Object)this)))
        {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDLjava/util/Set;FFZ)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDLjava/util/Set;FFZ)Z"),
            cancellable = true)
    private void bumblezone$onTeleportTo(ServerLevel serverLevel, double d, double e, double f, Set<Relative> set, float g, float h, boolean bl, CallbackInfoReturnable<Boolean> cir) {
        if (BzEntityTravelingToDimensionEvent.EVENT.invoke(new BzEntityTravelingToDimensionEvent(serverLevel.dimension(), (ServerPlayer)(Object)this))) {
            cir.setReturnValue(true);
        }
    }
}
