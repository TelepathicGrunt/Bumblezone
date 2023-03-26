package com.telepathicgrunt.the_bumblezone.mixin.blocks;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SmallDripleafBlock;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SmallDripleafBlock.class)
public class SmallDripleafBlockMixin {

    @ModifyExpressionValue(method = "mayPlaceOn(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/BlockGetter;getFluidState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/FluidState;"),
            require = 0)
    private static FluidState thebumblezone_waterlogWhenPlacedIntoSugarWater(FluidState fluid) {
        if(fluid.is(BzTags.SUGAR_WATER_FLUID) && Blocks.BIG_DRIPLEAF.defaultBlockState().is(BzTags.WATERLOGGABLE_BLOCKS_WHEN_PLACED_IN_FLUID)) {
            return Fluids.WATER.defaultFluidState();
        }
        return fluid;
    }
}
