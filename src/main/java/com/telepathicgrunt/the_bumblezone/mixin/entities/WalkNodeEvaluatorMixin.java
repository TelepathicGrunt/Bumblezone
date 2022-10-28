package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.blocks.SuperCandleWick;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WalkNodeEvaluator.class)
public class WalkNodeEvaluatorMixin {

    @Inject(method = "isBurningBlock(Lnet/minecraft/world/level/block/state/BlockState;)Z",
            at = @At(value = "HEAD"), cancellable = true)
    private static void thebumblezone_candleWickHazard(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.is(BzBlocks.SUPER_CANDLE_WICK.get()) && state.getValue(SuperCandleWick.LIT)) {
            cir.setReturnValue(true);
        }
    }
}