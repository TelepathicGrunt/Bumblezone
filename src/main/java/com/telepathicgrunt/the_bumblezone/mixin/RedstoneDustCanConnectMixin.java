package com.telepathicgrunt.the_bumblezone.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import com.telepathicgrunt.the_bumblezone.blocks.BzBlocks;
import com.telepathicgrunt.the_bumblezone.blocks.StickyHoneyResidue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RedstoneWireBlock.class)
public class RedstoneDustCanConnectMixin {

    @Inject(method = "canConnectTo(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/Direction;)Z",
            at = @At(value = "TAIL"),
            cancellable = true)
    private static void canConnectVisually(BlockState state, IBlockReader world, BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() == BzBlocks.STICKY_HONEY_REDSTONE){
            boolean canConnect = false;

            if (state.get(StickyHoneyResidue.FACING_TO_PROPERTY_MAP.get(Direction.DOWN))) {
                for (Direction horizontal : Direction.Plane.HORIZONTAL) {
                    if(horizontal == direction) {
                        canConnect = true;
                    }
                }
            }

            if(canConnect)
                cir.setReturnValue(true);
        }
    }

}