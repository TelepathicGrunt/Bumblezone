package com.telepathicgrunt.the_bumblezone.client.rendering.fluids;

import com.mojang.blaze3d.systems.RenderSystem;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.rendering.FluidClientOverlay;
import com.telepathicgrunt.the_bumblezone.fluids.base.ClientFluidProperties;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidProperties;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

public class HoneyFluidClientProperties {

    public static final ResourceLocation HONEY_FLUID_STILL_TEXTURE = new ResourceLocation(Bumblezone.MODID, "block/honey_fluid_still");
    public static final ResourceLocation HONEY_FLUID_FLOWING_TEXTURE = new ResourceLocation(Bumblezone.MODID, "block/honey_fluid_flow");

    public static ClientFluidProperties create(FluidProperties properties) {
        return new ClientFluidProperties(properties)
                .tintColor(0xFFFFFFFF)
                .still(HONEY_FLUID_STILL_TEXTURE)
                .flowing(HONEY_FLUID_FLOWING_TEXTURE)
                .overlay(HONEY_FLUID_FLOWING_TEXTURE)
                .screenOverlay(FluidClientOverlay::renderHoneyOverlay)
                .modifyFogColor((camera, level, pos, fluidState, fluid, fogColor) -> {
                    // Scale the brightness of fog but make sure it is never darker than the dimension's min brightness.
                    BlockPos blockpos = new BlockPos(camera.getEntity().getX(), camera.getEntity().getEyeY(), camera.getEntity().getZ());
                    float brightnessAtEyes = LightTexture.getBrightness(camera.getEntity().level.dimensionType(), camera.getEntity().level.getMaxLocalRawBrightness(blockpos));
                    float brightness = (float) Math.max(
                            Math.pow(FluidClientOverlay.getDimensionBrightnessAtEyes(camera.getEntity()), 2D),
                            brightnessAtEyes
                    );
                    float fogRed = 0.6F * brightness;
                    float fogGreen = 0.3F * brightness;
                    float fogBlue = 0.0F;
                    return new Vector3f(fogRed, fogGreen, fogBlue);
                })
                .modifyFog((camera, mode, renderDistance, partialTick, nearDistance, farDistance, shape) -> {
                    RenderSystem.setShaderFogStart(0.35f);
                    RenderSystem.setShaderFogEnd(4);
                });

    }
}
