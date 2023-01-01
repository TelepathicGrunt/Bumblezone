package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.telepathicgrunt.the_bumblezone.blocks.SuperCandleWick;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WalkNodeEvaluator.class)
public class WalkNodeEvaluatorMixin {

    @ModifyReturnValue(method = "isBurningBlock(Lnet/minecraft/world/level/block/state/BlockState;)Z",
            at = @At(value = "RETURN"))
    private static boolean thebumblezone_candleWickHazard(boolean isBurningBlock, BlockState state) {
        if(!isBurningBlock) {
            if (state.is(BzTags.CANDLES) && SuperCandleWick.getBlockPathType(state) == BlockPathTypes.DAMAGE_FIRE) {
                return true;
            }
        }
        return isBurningBlock;
    }

    @WrapOperation(method = "getBlockPathTypeRaw(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/pathfinder/BlockPathTypes;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z", ordinal =  1))
    private static boolean thebumblezone_bzFluidHazard(FluidState fluidState, TagKey<Fluid> waterTagKey, Operation<Boolean> original) {
        boolean originalWaterCheck = original.call(fluidState, waterTagKey);
        if (!originalWaterCheck) {
            if (fluidState.is(BzTags.BZ_HONEY_FLUID) || fluidState.is(BzTags.ROYAL_JELLY_FLUID) || fluidState.is(BzTags.SUGAR_WATER_FLUID)) {
                return true;
            }
        }
        return originalWaterCheck;
    }
}