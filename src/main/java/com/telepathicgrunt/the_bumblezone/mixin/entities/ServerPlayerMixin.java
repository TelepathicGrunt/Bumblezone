package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.tags.BzFluidTags;
import com.telepathicgrunt.the_bumblezone.world.structures.CellMazeStructure;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @Inject(method = "doTick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/critereon/LocationTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;)V"))
    private void thebumblezone_checkIfInCellMaze(CallbackInfo ci) {
        CellMazeStructure.applyAngerIfInMaze((ServerPlayer)(Object)this);
    }
}