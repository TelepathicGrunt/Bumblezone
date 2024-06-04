package com.telepathicgrunt.the_bumblezone.mixin.fabric.client;

import com.telepathicgrunt.the_bumblezone.client.dimension.BzDimensionSpecialEffects;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = FogRenderer.class, priority = 1200)
public class FogRendererMixin {

    @Inject(method = "setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;FZF)V",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V", ordinal = 0),
            require = 0, locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void bumblezone$reduceFogThickness(Camera camera,
                                                      FogRenderer.FogMode mode,
                                                      float fogEnd,
                                                      boolean thickFog,
                                                      float p_,
                                                      CallbackInfo ci,
                                                      FogType fogType,
                                                      Entity entity,
                                                      FogRenderer.FogData fogData)
    {
        BzDimensionSpecialEffects.fogThicknessAdjustments(
                GeneralUtilsClient.getClientPlayer(),
                fogEnd,
                thickFog,
                mode,
                fogType,
                (newFogStart) -> fogData.start = newFogStart,
                (newFogEnd) -> fogData.end = newFogEnd,
                (newFogShape) -> fogData.shape = newFogShape);
    }
}