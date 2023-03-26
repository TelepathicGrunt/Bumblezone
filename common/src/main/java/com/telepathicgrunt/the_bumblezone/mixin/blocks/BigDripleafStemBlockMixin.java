package com.telepathicgrunt.the_bumblezone.mixin.blocks;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BigDripleafStemBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BigDripleafStemBlock.class)
public class BigDripleafStemBlockMixin {

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