package com.telepathicgrunt.bumblezone.mixin.client;

import com.telepathicgrunt.bumblezone.client.rendering.FluidClientOverlay;
import com.telepathicgrunt.bumblezone.tags.BzFluidTags;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {

    @Shadow
    private static float red;

    @Shadow
    private static float green;

    @Shadow
    private static float blue;

    @Shadow
    private static long lastWaterFogColorUpdateTime = -1L;

    // make honey fluid have orange fog
    @Inject(method = "render(Lnet/minecraft/client/render/Camera;FLnet/minecraft/client/world/ClientWorld;IF)V",
            at = @At(value = "INVOKE", target = "net/minecraft/client/world/ClientWorld$Properties.getHorizonShadingRatio()D"))
    private static void thebumblezone_setupHoneyFogColor(Camera camera, float j, ClientWorld clientWorld, int l, float i1, CallbackInfo ci) {
        FluidState fluidstate = clientWorld.getFluidState(camera.getBlockPos());
        if(fluidstate.isIn(BzFluidTags.BZ_HONEY_FLUID)) {
            float brightness = (float) Math.pow(camera.getFocusedEntity().getBrightnessAtEyes(), 2D);
            red = 0.6F * brightness;
            green = 0.3F * brightness;
            blue = 0.0F;
            lastWaterFogColorUpdateTime = -1L;
        }
    }

    @Inject(method = "applyFog(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/BackgroundRenderer$FogType;FZ)V",
            at = @At(value = "INVOKE", target = "com/mojang/blaze3d/systems/RenderSystem.setShaderFogEnd(F)V", ordinal = 1, shift = At.Shift.AFTER))
    private static void thebumblezone_renderHoneyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo ci) {
        FluidState fluidstate = camera.getFocusedEntity().world.getFluidState(camera.getBlockPos());
        if(fluidstate.isIn(BzFluidTags.BZ_HONEY_FLUID)) {
            FluidClientOverlay.renderHoneyFog();
        }
    }
}