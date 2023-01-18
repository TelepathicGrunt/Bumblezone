package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.rendering.FluidClientOverlay;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzSkyProperty;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

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
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/multiplayer/ClientLevel$ClientLevelData;getClearColorScale()F"))
    private static void thebumblezone_setupHoneyFogColor(Camera camera, float j, ClientLevel clientWorld, int l, float i1, CallbackInfo ci) {
        FluidState fluidstate = FluidClientOverlay.getNearbyHoneyFluid(camera);
        if(fluidstate.is(BzTags.BZ_HONEY_FLUID)) {
            // Scale the brightness of fog but make sure it is never darker than the dimension's min brightness.
            BlockPos blockpos = new BlockPos(camera.getEntity().getX(), camera.getEntity().getEyeY(), camera.getEntity().getZ());
            float brightnessAtEyes = LightTexture.getBrightness(camera.getEntity().level.dimensionType(), camera.getEntity().level.getMaxLocalRawBrightness(blockpos));
            float brightness = (float) Math.max(
                    Math.pow(FluidClientOverlay.getDimensionBrightnessAtEyes(camera.getEntity()), 2D),
                    brightnessAtEyes
            );
            fogRed = 0.6F * brightness;
            fogGreen = 0.3F * brightness;
            fogBlue = 0.0F;
            biomeChangedTime = -1L;
        }
        else if(fluidstate.is(BzTags.ROYAL_JELLY_FLUID)) {
            // Scale the brightness of fog but make sure it is never darker than the dimension's min brightness.
            BlockPos blockpos = new BlockPos(camera.getEntity().getX(), camera.getEntity().getEyeY(), camera.getEntity().getZ());
            float brightnessAtEyes = LightTexture.getBrightness(camera.getEntity().level.dimensionType(), camera.getEntity().level.getMaxLocalRawBrightness(blockpos));
            float brightness = (float) Math.max(
                    Math.pow(FluidClientOverlay.getDimensionBrightnessAtEyes(camera.getEntity()), 2D),
                    brightnessAtEyes
            );
            fogBlue = 0.6F * brightness;
            fogRed = 0.3F * brightness;
            fogGreen = 0.0F;
            biomeChangedTime = -1L;
        }
    }

    @Inject(method = "setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;FZF)V", at = @At(value = "TAIL"))
    private static void thebumblezone_renderHoneyFog(Camera camera, FogRenderer.FogMode fogMode, float f, boolean bl, float g, CallbackInfo ci) {
        FluidClientOverlay.renderHoneyFog(camera);
    }

    @Inject(method = "setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;FZF)V",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V", ordinal = 0),
            require = 0, locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void thebumblezone_reduceFogThickness(Camera camera,
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