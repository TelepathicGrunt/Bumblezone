package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AmphibiousNodeEvaluator.class)
public class AmphibiousNodeEvaluatorMixin extends WalkNodeEvaluator {

    @WrapOperation(method = "getBlockPathType(Lnet/minecraft/world/level/BlockGetter;III)Lnet/minecraft/world/level/pathfinder/BlockPathTypes;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/pathfinder/AmphibiousNodeEvaluator;getBlockPathTypeRaw(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/pathfinder/BlockPathTypes;"),
            require = 0)
    private BlockPathTypes thebumblezone_bzStringCurtainBlockingBees(BlockGetter blockGetter, BlockPos blockPos, Operation<BlockPathTypes> original) {
        BlockPathTypes blockPathType = original.call(blockGetter, blockPos);
        if (blockPathType == BlockPathTypes.OPEN && this.mob != null) {
            if ((this.mob instanceof Bee || this.mob.getType().is(BzTags.STRING_CURTAIN_BLOCKS_PATHFINDING_FOR_NON_BEE_MOB)) &&
                !this.mob.getType().is(BzTags.STRING_CURTAIN_FORCE_ALLOW_PATHFINDING))
            {
                if (blockGetter.getBlockState(blockPos).is(BzTags.STRING_CURTAINS)) {
                    return BlockPathTypes.BLOCKED;
                }
            }
        }
        return blockPathType;
    }
}