package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.telepathicgrunt.the_bumblezone.blocks.StringCurtain;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = AmphibiousNodeEvaluator.class, priority = 1200)
public class AmphibiousNodeEvaluatorMixin extends WalkNodeEvaluator {

    @WrapOperation(method = "getPathType(Lnet/minecraft/world/level/pathfinder/PathfindingContext;III)Lnet/minecraft/world/level/pathfinder/PathType;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/pathfinder/PathfindingContext;getPathTypeFromState(III)Lnet/minecraft/world/level/pathfinder/PathType;"),
            require = 0)
    private PathType bumblezone$bzStringCurtainBlockingBees(PathfindingContext instance, int x, int y, int z, Operation<PathType> original) {
        PathType blockPathType = original.call(instance, x, y, z);
        PathType blocked = StringCurtain.getCurtainBlockPathType(this.mob, instance, new BlockPos(x, y, z), blockPathType);
        if (blocked != null) return blocked;
        return blockPathType;
    }
}