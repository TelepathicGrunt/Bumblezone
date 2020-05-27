package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.Direction;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.blocks.StickyHoneyResidue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RedstoneWireBlock.class)
public class RedstoneDustCanConnectMixin {

    @Inject(method = "Lnet/minecraft/block/RedstoneWireBlock;connectsTo(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;)Z",
            at = @At(value = "TAIL"),
            cancellable = true)
    private static void canConnectVisually(BlockState state, Direction dir, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() == BzBlocks.STICKY_HONEY_REDSTONE){
            boolean canConnect = false;

            if (state.get(StickyHoneyResidue.FACING_TO_PROPERTY_MAP.get(Direction.DOWN))) {
                for (Direction horizontal : Direction.Type.HORIZONTAL) {
                    if(horizontal == dir) {
                        canConnect = true;
                    }
                }
            }

            if(canConnect)
                cir.setReturnValue(true);
        }
    }

}