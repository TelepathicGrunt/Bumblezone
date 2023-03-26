package com.telepathicgrunt.the_bumblezone.mixin.blocks;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BigDripleafBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BigDripleafBlock.class)
public class BigDripleafBlockMixin {

    @ModifyReturnValue(method = "canReplace(Lnet/minecraft/world/level/block/state/BlockState;)Z",
            at = @At(value = "RETURN"),
            require = 0)
    private static boolean thebumblezone_allowPlacingIntoSugarWater(boolean canReplace, BlockState blockState) {
        if(!canReplace && blockState.is(BzFluids.SUGAR_WATER_BLOCK) && Blocks.BIG_DRIPLEAF.defaultBlockState().is(BzTags.WATERLOGGABLE_BLOCKS_WHEN_PLACED_IN_FLUID)) {
            return true;
        }
        return canReplace;
    }

    @ModifyExpressionValue(method = "place(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/material/FluidState;Lnet/minecraft/core/Direction;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;isSourceOfType(Lnet/minecraft/world/level/material/Fluid;)Z"),
            require = 0)
    private static boolean thebumblezone_waterlogWhenPlacedIntoSugarWater(boolean isWater, LevelAccessor levelAccessor, BlockPos blockPos, FluidState fluidState) {
        if(fluidState.is(BzTags.SUGAR_WATER_FLUID) && Blocks.BIG_DRIPLEAF.defaultBlockState().is(BzTags.WATERLOGGABLE_BLOCKS_WHEN_PLACED_IN_FLUID)) {
            return true;
        }
        return isWater;
    }
}
