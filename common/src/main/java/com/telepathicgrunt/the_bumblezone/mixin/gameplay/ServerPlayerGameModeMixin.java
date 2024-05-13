package com.telepathicgrunt.the_bumblezone.mixin.gameplay;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {

    @WrapOperation(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z"),
            require = 0)
    private boolean bumblezone$allowSpecialAirDestroy7(BlockState blockState, Operation<Boolean> operation) {
        boolean isAir = operation.call(blockState);

        if (isAir && blockState.is(BzTags.AIR_LIKE)) {
            return false;
        }

        return isAir;
    }

    @WrapOperation(method = "handleBlockBreakAction(Lnet/minecraft/core/BlockPos;Lnet/minecraft/network/protocol/game/ServerboundPlayerActionPacket$Action;Lnet/minecraft/core/Direction;II)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z"),
            require = 0)
    private boolean bumblezone$allowSpecialAirDestroy8(BlockState blockState, Operation<Boolean> operation) {
        boolean isAir = operation.call(blockState);

        if (isAir && blockState.is(BzTags.AIR_LIKE)) {
            return false;
        }

        return isAir;
    }
}