package com.telepathicgrunt.bumblezone.mixin.blocks;

import com.telepathicgrunt.bumblezone.client.rendering.FluidClientOverlay;
import com.telepathicgrunt.bumblezone.client.rendering.PileOfPollenRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(InGameOverlayRenderer.class)
public class BlockOverlayRendererMixin {

    @Inject(method = "renderOverlays",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameOverlayRenderer;getInWallBlockState(Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/block/BlockState;"),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true)
    private static void thebumblezone_blockOverlay(MinecraftClient minecraftClient, MatrixStack matrixStack, CallbackInfo ci, PlayerEntity playerEntity) {
        if (FluidClientOverlay.fluidOverlay(playerEntity, new BlockPos(playerEntity.getPos()), matrixStack))
            ci.cancel();
    }

    @Inject(method = "renderOverlays",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/gui/hud/InGameOverlayRenderer;getInWallBlockState(Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/block/BlockState;"),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true)
    private static void thebumblezone_blockOverlay2(MinecraftClient minecraftClient, MatrixStack matrixStack, CallbackInfo ci, PlayerEntity playerEntity, BlockState blockState) {
        if (PileOfPollenRenderer.pileOfPollenOverlay(playerEntity, new BlockPos(playerEntity.getPos()), matrixStack, blockState))
            ci.cancel();
    }
}