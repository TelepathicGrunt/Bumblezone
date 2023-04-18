package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.telepathicgrunt.the_bumblezone.blocks.StringCurtain;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AmphibiousNodeEvaluator.class)
public class AmphibiousNodeEvaluatorMixin extends WalkNodeEvaluator {

    @WrapOperation(method = "getBlockPathType(Lnet/minecraft/world/level/BlockGetter;III)Lnet/minecraft/world/level/pathfinder/BlockPathTypes;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/pathfinder/AmphibiousNodeEvaluator;getBlockPathTypeRaw(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/pathfinder/BlockPathTypes;"),
            require = 0)
    private BlockPathTypes bumblezone$bzStringCurtainBlockingBees(BlockGetter blockGetter, BlockPos blockPos, Operation<BlockPathTypes> original) {
        BlockPathTypes blockPathType = original.call(blockGetter, blockPos);
        BlockPathTypes blocked = StringCurtain.getCurtainBlockPathType(this.mob, blockGetter, blockPos, blockPathType);
        if (blocked != null) return blocked;
        return blockPathType;
    }
}