package com.telepathicgrunt.bumblezone.mixin.blocks;

import com.telepathicgrunt.bumblezone.client.rendering.FluidClientOverlay;
import com.telepathicgrunt.bumblezone.client.rendering.PileOfPollenRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(InGameOverlayRenderer.class)
public class BlockOverlayRendererMixin {

    @Inject(method = "renderOverlays",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/gui/hud/InGameOverlayRenderer;getInWallBlockState(Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/block/BlockState;"),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true)
    private static void thebumblezone_blockOverlay(MinecraftClient minecraftClient, MatrixStack matrixStack, CallbackInfo ci, PlayerEntity playerEntity, BlockState blockState) {
        if (FluidClientOverlay.sugarWaterFluidOverlay(playerEntity, matrixStack))
            ci.cancel();
        else if (PileOfPollenRenderer.pileOfPollenOverlay(playerEntity, matrixStack, blockState))
            ci.cancel();
    }

    // make honey fluid have overlay
    @Inject(method = "renderOverlays(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/util/math/MatrixStack;)V",
            at = @At(value = "INVOKE", target = "net/minecraft/client/network/ClientPlayerEntity.isSubmergedIn(Lnet/minecraft/tag/Tag;)Z"),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true)
    private static void thebumblezone_renderHoneyOverlay(MinecraftClient minecraft, MatrixStack matrixStack, CallbackInfo ci) {
        if(FluidClientOverlay.renderHoneyOverlay(minecraft.player, matrixStack)) {
            ci.cancel();
        }
    }
}