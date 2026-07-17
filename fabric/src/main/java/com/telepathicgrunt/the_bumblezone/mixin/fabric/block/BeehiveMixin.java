package com.telepathicgrunt.the_bumblezone.mixin.fabric.block;

import com.llamalad7.mixinextras.sugar.Local;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(BeehiveBlock.class)
public class BeehiveMixin {
    @Inject(method = "angerNearbyBees(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V",
            at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 0, remap = false),
            cancellable = true)
    private void bumblezone$essenceBeehivePreventAnger2_fabric(Level level, BlockPos blockPos, CallbackInfo ci, @Local(ordinal = 0) List<Bee> beeList, @Local(ordinal = 1) List<Player> playerList) {
        BeeAggression.preventAngerOnEssencedPlayers(beeList, playerList);
        if (playerList.isEmpty()) {
            ci.cancel(); // Prevent crash if no players are around
        }
    }
}