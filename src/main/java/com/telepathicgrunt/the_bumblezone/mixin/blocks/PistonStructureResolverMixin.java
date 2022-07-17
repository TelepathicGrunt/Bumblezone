package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PistonStructureResolver.class)
public class PistonStructureResolverMixin {

    @Mutable
    @Final
    @Shadow
    private Direction pushDirection;

    // allow royal jelly block to be pullable only
    @Inject(method = "addBlockLine(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void thebumblezone_pullableOnlyBlocks2(BlockPos blockPos,
                                                      Direction direction,
                                                      CallbackInfoReturnable<Boolean> cir,
                                                      BlockState blockState)
    {
        if (blockState.is(BzBlocks.ROYAL_JELLY_BLOCK)) {
            this.pushDirection = this.pushDirection.getOpposite();
        }
    }

    // allow royal jelly block to be pullable only
    @Inject(method = "addBlockLine(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z",
            at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 0, remap = false),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void thebumblezone_pullableOnlyBlocks3(BlockPos blockPos,
                                                      Direction direction,
                                                      CallbackInfoReturnable<Boolean> cir,
                                                      BlockState blockState)
    {
        if (blockState.is(BzBlocks.ROYAL_JELLY_BLOCK)) {
            this.pushDirection = this.pushDirection.getOpposite();
        }
    }
}