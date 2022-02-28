package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.telepathicgrunt.the_bumblezone.client.rendering.FluidClientOverlay;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @Shadow
    private static float fogRed;

    @Shadow
    private static float fogGreen;

    @Shadow
    private static float fogBlue;

    @Shadow
    private static long biomeChangedTime = -1L;

    // make honey fluid have orange fog
    @Inject(method = "setupColor(Lnet/minecraft/client/Camera;FLnet/minecraft/client/multiplayer/ClientLevel;IF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel$ClientLevelData;getClearColorScale()D"))
    private static void thebumblezone_setupHoneyFogColor(Camera camera, float j, ClientLevel clientWorld, int l, float i1, CallbackInfo ci) {
        FluidState fluidstate = FluidClientOverlay.getNearbyHoneyFluid(camera);
        if(fluidstate.is(BzTags.BZ_HONEY_FLUID)) {
            // Scale the brightness of fog but make sure it is never darker than the dimension's min brightness.
            float brightness = (float) Math.max(
                    Math.pow(FluidClientOverlay.getDimensionBrightnessAtEyes(camera.getEntity()), 2D),
                    camera.getEntity().level.dimensionType().brightness(0)
            );
            fogRed = 0.6F * brightness;
            fogGreen = 0.3F * brightness;
            fogBlue = 0.0F;
            biomeChangedTime = -1L;
        }
    }

    @Inject(method = "setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;FZ)V",
            at = @At(value = "INVOKE", target = "com/mojang/blaze3d/systems/RenderSystem.setShaderFogEnd(F)V", ordinal = 1, shift = At.Shift.AFTER))
    private static void thebumblezone_renderHoneyFog(Camera camera, FogRenderer.FogMode fogType, float viewDistance, boolean thickFog, CallbackInfo ci) {
        FluidClientOverlay.renderHoneyFog(camera);
    }
}