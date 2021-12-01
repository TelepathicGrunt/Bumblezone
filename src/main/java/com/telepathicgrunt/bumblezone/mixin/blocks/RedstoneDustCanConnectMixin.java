package com.telepathicgrunt.bumblezone.mixin.blocks;

import com.telepathicgrunt.bumblezone.blocks.StickyHoneyResidue;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RedStoneWireBlock.class)
public class RedstoneDustCanConnectMixin {

    @Inject(method = "shouldConnectTo(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z",
            at = @At(value = "TAIL"),
            cancellable = true)
    private static void thebumblezone_canConnectVisually(BlockState state, Direction dir, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() == BzBlocks.STICKY_HONEY_REDSTONE) {
            boolean canConnect = false;

            if (state.getValue(StickyHoneyResidue.FACING_TO_PROPERTY_MAP.get(Direction.DOWN))) {
                for (Direction horizontal : Direction.Plane.HORIZONTAL) {
                    if (horizontal == dir) {
                        canConnect = true;
                        break;
                    }
                }
            }

            if(canConnect)
                cir.setReturnValue(true);
        }
    }

}