package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.client;

import com.telepathicgrunt.the_bumblezone.events.player.PlayerRightClickedBlockEvent;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @Inject(
            method = "performUseItemOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private void bumblezone$onPerformUseItemOn(LocalPlayer player, InteractionHand hand, BlockHitResult blockResult, CallbackInfoReturnable<InteractionResult> cir) {
        InteractionResult result = PlayerRightClickedBlockEvent.EVENT.invoke(new PlayerRightClickedBlockEvent(player, hand, blockResult.getBlockPos(), blockResult));
        if (result != null) {
            cir.setReturnValue(result);
        }
    }
}
