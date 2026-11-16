package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import dev.architectury.injectables.annotations.PlatformOnly;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PistonStructureResolver.class)
public class PistonStructureResolverMixin {

    @Mutable
    @Final
    @Shadow
    private boolean extending;

    // allow royal jelly block to be pullable only
    @Inject(method = "resolve()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/piston/PistonBaseBlock;isPushable(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;ZLnet/minecraft/core/Direction;)Z", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true)
    private void bumblezone$pullableOnlyBlocks2(CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 0) BlockState nextState)
    {
        if (this.extending &&
            nextState.is(BzBlocks.ROYAL_JELLY_BLOCK.get()))
        {
            cir.setReturnValue(false);
        }
    }

    @WrapOperation(method = "resolve()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/piston/PistonBaseBlock;isPushable(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;ZLnet/minecraft/core/Direction;)Z"))
    private boolean bumblezone$pullableOnlyBlocks3(BlockState state, Level level, BlockPos pos, Direction direction, boolean allowDestroyable, Direction connectionDirection, Operation<Boolean> original)
    {
        if (this.extending &&
            state.is(BzBlocks.ROYAL_JELLY_BLOCK.get()))
        {
            return false;
        }
        return original.call(state, level, pos, direction, allowDestroyable, connectionDirection);
    }

    @WrapOperation(method = "addBlockLine(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/piston/PistonBaseBlock;isPushable(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;ZLnet/minecraft/core/Direction;)Z"))
    private boolean bumblezone$pullableOnlyBlocks4(BlockState state, Level level, BlockPos pos, Direction direction, boolean allowDestroyable, Direction connectionDirection, Operation<Boolean> original)
    {
        if (this.extending &&
            state.is(BzBlocks.ROYAL_JELLY_BLOCK.get()))
        {
            return false;
        }
        return original.call(state, level, pos, direction, allowDestroyable, connectionDirection);
    }
}