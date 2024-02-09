package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @WrapOperation(method = "destroyBlock(Lnet/minecraft/core/BlockPos;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z"),
            require = 0)
    private boolean bumblezone$allowSpecialAirDestroy1(BlockState blockState, Operation<Boolean> operation) {
        boolean isAir = operation.call(blockState);

        if (isAir && blockState.is(BzTags.AIR_LIKE)) {
            return false;
        }

        return isAir;
    }

    @WrapOperation(method = "method_41930(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;I)Lnet/minecraft/network/protocol/Packet;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z"),
            require = 0)
    private boolean bumblezone$allowSpecialAirDestroy2(BlockState blockState, Operation<Boolean> operation) {
        boolean isAir = operation.call(blockState);

        if (isAir && blockState.is(BzTags.AIR_LIKE)) {
            return false;
        }

        return isAir;
    }

    @WrapOperation(method = "continueDestroyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z"),
            require = 0)
    private boolean bumblezone$allowSpecialAirDestroy3(BlockState blockState, Operation<Boolean> operation) {
        boolean isAir = operation.call(blockState);

        if (isAir && blockState.is(BzTags.AIR_LIKE)) {
            return false;
        }

        return isAir;
    }
}