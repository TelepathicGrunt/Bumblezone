package com.telepathicgrunt.the_bumblezone.mixin.fabric.blocks;

import com.llamalad7.mixinextras.sugar.Local;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LiquidBlock.class, priority = 1200)
public class LiquidBlockMixin {

    @Inject(method = "shouldSpreadLiquid(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getFluidState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/FluidState;", ordinal = 0),
            require = 0,
            cancellable = true)
    private void thebumblezone_sugarWaterLavaInteraction1(Level level,
                                                         BlockPos blockPos,
                                                         BlockState blockState,
                                                         CallbackInfoReturnable<Boolean> cir,
                                                         @Local(ordinal = 1) BlockPos blockPos2)
    {
        if (level.getFluidState(blockPos2).is(BzTags.SUGAR_WATER_FLUID)) {
            Block block = level.getFluidState(blockPos).isSource() ? Blocks.OBSIDIAN : BzBlocks.SUGAR_INFUSED_COBBLESTONE.get();
            level.setBlockAndUpdate(blockPos, block.defaultBlockState());
            level.levelEvent(1501, blockPos, 0);
            cir.setReturnValue(false);
        }
    }
}