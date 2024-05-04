package com.telepathicgrunt.the_bumblezone.client.rendering.fluids;

import com.mojang.blaze3d.systems.RenderSystem;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.rendering.FluidClientOverlay;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluidBlock;
import com.telepathicgrunt.the_bumblezone.fluids.base.ClientFluidProperties;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidProperties;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

public class RoyalJellyClientProperties {
    public static final ResourceLocation ROYAL_JELLY_FLUID_STILL_TEXTURE = new ResourceLocation(Bumblezone.MODID, "block/royal_jelly_fluid_still");
    public static final ResourceLocation ROYAL_JELLY_FLUID_FLOWING_TEXTURE = new ResourceLocation(Bumblezone.MODID, "block/royal_jelly_fluid_flow");
    public static final ResourceLocation ROYAL_JELLY_FLUID_FLOWING_DIAGONAL_TEXTURE = new ResourceLocation(Bumblezone.MODID, "block/royal_jelly_fluid_flow_diagonal");

    public static ClientFluidProperties create(FluidProperties properties) {
        return new ClientFluidProperties(properties)
                .still(ROYAL_JELLY_FLUID_STILL_TEXTURE)
                .flowing(ROYAL_JELLY_FLUID_FLOWING_TEXTURE)
                .overlay(ROYAL_JELLY_FLUID_FLOWING_TEXTURE)
                .diagonal(ROYAL_JELLY_FLUID_FLOWING_DIAGONAL_TEXTURE)
                .tintColor(0xFFFFFFFF)
                .screenOverlay(FluidClientOverlay::renderHoneyOverlay)
                .modifyFogColor((camera, partialTick, level, renderDistance, darkenWorldAmount, fogColor) -> {
                    Entity entity = camera.getEntity();
                    BlockState state = level.getBlockState(entity != null ? BlockPos.containing(entity.getEyePosition(1)) : camera.getBlockPosition());
                    if (state.hasProperty(HoneyFluidBlock.BOTTOM_LEVEL)) {
                        double yEye = entity != null ? entity.getEyePosition(1).y() : camera.getPosition().y();
                        double yOffset = yEye - ((int)yEye);
                        if (state.getValue(HoneyFluidBlock.BOTTOM_LEVEL) / 8D > yOffset + 0.1) {
                            return fogColor;
                        }
                    }

                    // Scale the brightness of fog but make sure it is never darker than the dimension's min brightness.
                    BlockPos blockpos = BlockPos.containing(camera.getEntity().getX(), camera.getEntity().getEyeY(), camera.getEntity().getZ());
                    float brightnessAtEyes = LightTexture.getBrightness(camera.getEntity().level().dimensionType(), camera.getEntity().level().getMaxLocalRawBrightness(blockpos));
                    float brightness = (float) Math.max(
                            Math.pow(FluidClientOverlay.getDimensionBrightnessAtEyes(camera.getEntity()), 2D),
                            brightnessAtEyes
                    );
                    float fogRed = 0.5F * brightness;
                    float fogGreen = 0.0F;
                    float fogBlue = 0.55F * brightness;
                    return new Vector3f(fogRed, fogGreen, fogBlue);
                })
                .modifyFog((camera, mode, renderDistance, partialTick, nearDistance, farDistance, shape) -> {
                    Entity entity = camera.getEntity();
                    if (entity != null) {
                        BlockState state = entity.level().getBlockState(BlockPos.containing(entity.getEyePosition(1)));
                        if (state.hasProperty(HoneyFluidBlock.BOTTOM_LEVEL)) {
                            double yEye = entity.getEyePosition(1).y();
                            double yOffset = yEye - ((int)yEye);
                            if (state.getValue(HoneyFluidBlock.BOTTOM_LEVEL) / 8D > yOffset + 0.1) {
                                return;
                            }
                        }
                    }

                    RenderSystem.setShaderFogStart(0.35f);
                    RenderSystem.setShaderFogEnd(4);
                });
    }
}
