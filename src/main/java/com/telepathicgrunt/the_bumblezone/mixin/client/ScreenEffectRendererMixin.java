package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.client.rendering.FluidClientOverlay;
import com.telepathicgrunt.the_bumblezone.client.rendering.pileofpollen.PileOfPollenRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    @Inject(method = "renderScreenEffect(Lnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/ScreenEffectRenderer;getViewBlockingState(Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/level/block/state/BlockState;"),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true)
    private static void thebumblezone_pollonBlockOverlay(Minecraft minecraftClient, PoseStack matrixStack, CallbackInfo ci, Player playerEntity, BlockState blockState) {
        if (PileOfPollenRenderer.pileOfPollenOverlay(playerEntity, matrixStack, blockState)) {
            ci.cancel();
        }
    }

    // make honey fluid have overlay
    @Inject(method = "renderScreenEffect(Lnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            require = 0)
    private static void thebumblezone_renderHoneyOverlay(Minecraft minecraft, PoseStack matrixStack, CallbackInfo ci) {
        FluidClientOverlay.sugarWaterFluidOverlay(minecraft.player, matrixStack);
        FluidClientOverlay.renderHoneyOverlay(minecraft.player, matrixStack);
    }
}