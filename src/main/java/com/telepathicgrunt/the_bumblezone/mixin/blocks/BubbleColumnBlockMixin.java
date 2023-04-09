package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BubbleColumnBlock.class)
public class BubbleColumnBlockMixin {

    // allow intermixing of my and vanilla's bubble columns
    @ModifyReturnValue(method = "canSurvive(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z",
            at = @At(value = "RETURN"),
            require = 0)
    private boolean thebumblezone_allowVanillaBubbleColumnOnSugarWaterBubbleColumn(boolean canSurvive, BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        if(!canSurvive && levelReader.getBlockState(blockPos).is(BzFluids.SUGAR_WATER_BUBBLE_COLUMN_BLOCK)) {
            return true;
        }
        return canSurvive;
    }
}