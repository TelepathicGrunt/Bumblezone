package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FogRenderer.class)
public class FogRendererClient {

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
        FluidState fluidstate = activeRenderInfo.getFluidInCamera();
        if(fluidstate.getType().equals(BzFluids.HONEY_FLUID.get()) || fluidstate.getType().equals(BzFluids.HONEY_FLUID_FLOWING.get())) {
            fogRed = 0.6F;
            fogGreen = 0.4F;
            fogBlue = 0.0F;
            biomeChangedTime = -1L;
        }
    }
}