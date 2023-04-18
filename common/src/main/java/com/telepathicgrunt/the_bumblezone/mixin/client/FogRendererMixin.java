package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzSkyProperty;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FogRenderer.class)
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

        if (mode == FogRenderer.FogMode.FOG_TERRAIN && thickFog && Minecraft.getInstance().player.getLevel().dimension().location().equals(Bumblezone.MOD_DIMENSION_ID)) {
            BzSkyProperty.fogThicknessAdjustments(fogEnd, fogData);
        }
    }
}