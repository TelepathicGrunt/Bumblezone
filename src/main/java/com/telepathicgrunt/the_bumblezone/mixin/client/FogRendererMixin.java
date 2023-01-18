package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.rendering.FluidClientOverlay;
import com.telepathicgrunt.the_bumblezone.configs.BzConfig;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
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

    @ModifyVariable(method = "setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;FZF)V",
            at = @At(value = "HEAD"),
            ordinal = 0,
            argsOnly = true,
            require = 0)
    private static float thebumblezone_reduceFogThickness(float original, Camera cam, FogRenderer.FogMode mode, float farPlaneDistance2, boolean thickFog, float p__) {
        if (mode == FogRenderer.FogMode.FOG_TERRAIN && thickFog && Minecraft.getInstance().player.getLevel().dimension().location().equals(Bumblezone.MOD_DIMENSION_ID)) {
            float finalReducer = (float) (BzConfig.fogReducer / Math.max(farPlaneDistance2 / 382f, 1.0f));
            return Math.min(farPlaneDistance2 * 0.95f, original * finalReducer);
        }
        return original;
    }
}