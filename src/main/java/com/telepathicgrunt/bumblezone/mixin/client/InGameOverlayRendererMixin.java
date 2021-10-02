package com.telepathicgrunt.bumblezone.mixin.client;

import com.telepathicgrunt.bumblezone.client.rendering.FluidClientOverlay;
import com.telepathicgrunt.bumblezone.tags.BzFluidTags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {

    // make honey fluid have overlay
    @Inject(method = "renderOverlays(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/util/math/MatrixStack;)V",
            at = @At(value = "INVOKE", target = "net/minecraft/client/network/ClientPlayerEntity.isSubmergedIn(Lnet/minecraft/tag/Tag;)Z"))
    private static void thebumblezone_renderHoneyOverlay(MinecraftClient minecraft, MatrixStack matrixStack, CallbackInfo ci) {
        if(minecraft.player.isSubmergedIn(BzFluidTags.BZ_HONEY_FLUID)) {
            FluidClientOverlay.renderHoneyOverlay(minecraft, matrixStack);
        }
    }
}