package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.telepathicgrunt.the_bumblezone.blocks.IncenseCandleBase;
import com.telepathicgrunt.the_bumblezone.blocks.SuperCandleBase;
import com.telepathicgrunt.the_bumblezone.blocks.SuperCandleWick;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WalkNodeEvaluator.class)
public class WalkNodeEvaluatorMixin {

    @ModifyReturnValue(method = "isBurningBlock(Lnet/minecraft/world/level/block/state/BlockState;)Z",
            at = @At(value = "RETURN"))
    private static boolean thebumblezone_candleWickHazard(boolean isBurningBlock, BlockState state) {
        if(!isBurningBlock) {
            if (state.is(BzTags.CANDLES) && SuperCandleWick.getBlockPathType(state) == BlockPathTypes.DAMAGE_FIRE) {
                return true;
            }
        }
        return isBurningBlock;
    }
}