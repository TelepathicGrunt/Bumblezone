package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.telepathicgrunt.the_bumblezone.client.rendering.FluidRender;
import com.telepathicgrunt.the_bumblezone.tags.BzFluidTags;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FogRenderer.class)
public class FogRendererClientMixin {

    @Shadow
    private static float fogRed;

    @Shadow
    private static float fogGreen;

    @Shadow
    private static float fogBlue;

    @Shadow
    private static long biomeChangedTime = -1L;

    // make honey fluid have orange fog
    @Inject(method = "setupColor(Lnet/minecraft/client/renderer/ActiveRenderInfo;FLnet/minecraft/client/world/ClientWorld;IF)V",
            at = @At(value = "INVOKE", target = "net/minecraft/client/world/ClientWorld$ClientWorldInfo.getClearColorScale()D"))
    private static void thebumblezone_setupHoneyFogColor(ActiveRenderInfo activeRenderInfo, float j, ClientWorld clientWorld, int l, float i1, CallbackInfo ci) {
        FluidState fluidstate = FluidRender.getNearbyHoneyFluid(activeRenderInfo);
        if(fluidstate.is(BzFluidTags.BZ_HONEY_FLUID)) {
            // Scale the brightness of fog but make sure it is never darker than the dimension's min brightness.
            float brightness = (float) Math.max(
                    Math.pow(FluidRender.getDimensionBrightnessAtEyes(activeRenderInfo.getEntity()), 2D),
                    activeRenderInfo.getEntity().level.dimensionType().brightness(0)
            );
            fogRed = 0.6F * brightness;
            fogGreen = 0.3F * brightness;
            fogBlue = 0.0F;
            biomeChangedTime = -1L;
        }
    }
}