package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.telepathicgrunt.the_bumblezone.client.rendering.FluidRender;
import com.telepathicgrunt.the_bumblezone.tags.BzFluidTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OverlayRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OverlayRenderer.class)
public class OverlayRendererClientMixin {

    // make honey fluid have overlay
    @Inject(method = "renderScreenEffect(Lnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/matrix/MatrixStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/player/ClientPlayerEntity;isEyeInFluid(Lnet/minecraft/tags/ITag;)Z"))
    private static void thebumblezone_renderHoneyOverlay(Minecraft minecraft, MatrixStack matrixStack, CallbackInfo ci) {
        if(minecraft.player.isEyeInFluid(BzFluidTags.BZ_HONEY_FLUID)) {
            if (!net.minecraftforge.event.ForgeEventFactory.renderWaterOverlay(minecraft.player, matrixStack))
                FluidRender.renderHoneyOverlay(minecraft, matrixStack);
        }
    }
}