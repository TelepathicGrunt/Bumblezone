package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.client.rendering.FluidClientOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    // make honey fluid have overlay
    @Inject(method = "renderScreenEffect(Lnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isEyeInFluid(Lnet/minecraft/tags/Tag;)Z"),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true)
    private static void thebumblezone_renderHoneyOverlay(Minecraft minecraft, PoseStack matrixStack, CallbackInfo ci) {
        if(FluidClientOverlay.renderHoneyOverlay(minecraft.player, matrixStack)) {
            ci.cancel();
        }
    }
}