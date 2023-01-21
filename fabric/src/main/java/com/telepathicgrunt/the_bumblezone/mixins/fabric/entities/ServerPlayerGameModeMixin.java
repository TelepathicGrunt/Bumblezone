package com.telepathicgrunt.the_bumblezone.mixins.fabric.entities;

import com.telepathicgrunt.the_bumblezone.events.player.PlayerRightClickedBlockEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {

    @Inject(
            method = "useItemOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;",
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private void bumblezone$onUseItemOn(ServerPlayer serverPlayer, Level level, ItemStack itemStack, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        BlockState state = level.getBlockState(blockHitResult.getBlockPos());
        if (state.getBlock().isEnabled(level.enabledFeatures())) {
            InteractionResult result = PlayerRightClickedBlockEvent.EVENT.invoke(new PlayerRightClickedBlockEvent(serverPlayer, interactionHand, blockHitResult.getBlockPos(), blockHitResult));
            if (result != null) {
                cir.setReturnValue(result);
            }
        }
    }
}
