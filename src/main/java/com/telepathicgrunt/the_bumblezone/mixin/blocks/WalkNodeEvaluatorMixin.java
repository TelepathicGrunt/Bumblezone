package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import com.telepathicgrunt.the_bumblezone.blocks.SuperCandleWick;
import com.telepathicgrunt.the_bumblezone.enchantments.CombCutterEnchantment;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

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