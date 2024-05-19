package com.telepathicgrunt.the_bumblezone.mixin.fabric.entity;

import com.telepathicgrunt.the_bumblezone.platform.BlockExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WalkNodeEvaluator.class)
public class WalkNodeEvaluatorMixin {

    @Inject(method = "getPathTypeStatic(Lnet/minecraft/world/level/pathfinder/PathfindingContext;Lnet/minecraft/core/BlockPos$MutableBlockPos;)Lnet/minecraft/world/level/pathfinder/PathType;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/pathfinder/PathfindingContext;getPathTypeFromState(III)Lnet/minecraft/world/level/pathfinder/PathType;"),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    private static void bumblezone$getBlockPathTypeRaw(PathfindingContext pathfindingContext, BlockPos.MutableBlockPos mutableBlockPos, CallbackInfoReturnable<PathType> cir, int x, int y, int z) {
        BlockState blockState = pathfindingContext.getBlockState(mutableBlockPos);
        if (blockState.getBlock() instanceof BlockExtension extension) {
            PathType blockpathtypes = extension.bz$getBlockPathType(blockState, pathfindingContext.level(), mutableBlockPos, null);
            if (blockpathtypes != null) {
                cir.setReturnValue(blockpathtypes);
            }
        }
    }
}