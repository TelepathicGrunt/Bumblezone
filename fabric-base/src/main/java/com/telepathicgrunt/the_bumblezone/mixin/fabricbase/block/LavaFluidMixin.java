package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.block;

import com.llamalad7.mixinextras.sugar.Local;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.LavaFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = LavaFluid.class, priority = 1200)
public class LavaFluidMixin {

    @ModifyArg(method = "spreadTo(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/world/level/material/FluidState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", ordinal = 0),
            require = 0,
            index = 1)
    private BlockState thebumblezone_sugarWaterLavaInteraction2(BlockState blockState,
                                                                @Local(ordinal = 1) FluidState fluidState2)
    {
        if (fluidState2.is(BzTags.SUGAR_WATER_FLUID)) {
            return BzBlocks.SUGAR_INFUSED_STONE.get().defaultBlockState();
        }
        return blockState;
    }
}