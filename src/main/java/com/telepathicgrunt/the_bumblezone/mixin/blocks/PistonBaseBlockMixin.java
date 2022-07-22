package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import com.telepathicgrunt.the_bumblezone.blocks.RoyalJellyBlock;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonBaseBlock.class)
public class PistonBaseBlockMixin {

    // allow royal jelly block to be pullable only
    @Inject(method = "isPushable(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;ZLnet/minecraft/core/Direction;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getPistonPushReaction()Lnet/minecraft/world/level/material/PushReaction;"),
            cancellable = true)
    private static void thebumblezone_pullableOnlyBlocks1(BlockState blockState,
                                                             Level level,
                                                             BlockPos blockPos,
                                                             Direction pushDirection,
                                                             boolean unknown,
                                                             Direction pistonDirection,
                                                             CallbackInfoReturnable<Boolean> cir)
    {
        if (blockState.is(BzBlocks.ROYAL_JELLY_BLOCK.get())) {
            cir.setReturnValue(RoyalJellyBlock.isValidMoveDirection(pushDirection, pistonDirection));
        }
    }
}