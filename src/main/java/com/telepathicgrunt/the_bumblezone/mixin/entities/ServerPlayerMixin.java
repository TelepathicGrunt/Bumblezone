package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationBackend;
import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationHookup;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import com.telepathicgrunt.the_bumblezone.items.EssenceOfTheBees;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @Inject(method = "doTick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/critereon/PlayerTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;)V"))
    private void thebumblezone_checkIfInSpecialStructures(CallbackInfo ci) {
        ServerPlayer serverPlayer = (ServerPlayer)(Object)this;
        if (!EssenceOfTheBees.hasEssence(serverPlayer)) {
            BeeAggression.applyAngerIfInTaggedStructures(serverPlayer);
            BeeQueenEntity.applyMiningFatigueInStructures(serverPlayer);
        }
    }

    @Inject(method = "doTick()V",
            at = @At(value = "HEAD"))
    private void thebumblezone_playerTickHead(CallbackInfo ci) {
        ServerPlayer serverPlayer = (ServerPlayer)(Object)this;
        EntityTeleportationHookup.playerTick(serverPlayer);
    }

    // Handles storing of past non-bumblezone dimension the entity is leaving
    @Inject(method = "changeDimension(Lnet/minecraft/server/level/ServerLevel;)Lnet/minecraft/world/entity/Entity;",
            at = @At(value = "HEAD"))
    private void thebumblezone_onDimensionChange1(ServerLevel destination, CallbackInfoReturnable<Entity> cir) {
        EntityTeleportationBackend.entityChangingDimension(destination.dimension().location(), ((Entity)(Object)this));
    }

    // Handles storing of past non-bumblezone dimension the entity is leaving
    @Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDFF)V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/level/ServerPlayer;getLevel()Lnet/minecraft/server/level/ServerLevel;"))
    private void thebumblezone_onDimensionChange2(ServerLevel serverLevel, double d, double e, double f, float g, float h, CallbackInfo ci) {
        EntityTeleportationBackend.entityChangingDimension(serverLevel.dimension().location(), ((Entity)(Object)this));
    }
}