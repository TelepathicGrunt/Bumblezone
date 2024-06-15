package com.telepathicgrunt.the_bumblezone.client.rendering.fluids;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamresourceful.resourcefullib.client.fluid.data.ClientFluidProperties;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluidBlock;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.function.Function;

public class HoneyFluidClientProperties {

    public static final ResourceLocation HONEY_FLUID_STILL_TEXTURE = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "block/honey_fluid/still");
    public static final ResourceLocation HONEY_FLUID_FLOWING_TEXTURE = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "block/honey_fluid/flow");
    public static final ResourceLocation HONEY_FLUID_FLOWING_DIAGONAL_TEXTURE = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "block/honey_fluid/flow_diagonal");

    public static ClientFluidProperties create() {
        return new ClientFluidProperties() {
            @Override
            public ResourceLocation still(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
                return HONEY_FLUID_STILL_TEXTURE;
            }

            @Override
            public ResourceLocation flowing(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
                return HONEY_FLUID_FLOWING_TEXTURE;
            }

            public ResourceLocation flowingDiagonal(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
                return HONEY_FLUID_FLOWING_DIAGONAL_TEXTURE;
            }

            @Override
            public ResourceLocation overlay(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
                return HONEY_FLUID_FLOWING_TEXTURE;
            }

            @Override
            public ResourceLocation screenOverlay() {
                return null;
            }

            @Override
            public void renderOverlay(Minecraft minecraft, PoseStack stack) {
                FluidClientOverlay.renderHoneyOverlay(minecraft.player, stack);
            }

            @Override
            public int tintColor(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
                return 0xFFFFFFFF;
            }

            @Override
            public boolean renderFluid(BlockPos pos, BlockAndTintGetter level, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState, Function<ResourceLocation, TextureAtlasSprite> sprites) {
                TextureAtlasSprite[] textureAtlasSprites = new TextureAtlasSprite[] {
                        sprites.apply(this.still(level, pos, fluidState)),
                        sprites.apply(this.flowing(level, pos, fluidState)),
                        sprites.apply(this.overlay(level, pos, fluidState)),
                        sprites.apply(this.flowingDiagonal(level, pos, fluidState)),
                };
                HoneyFluidRendering.renderSpecialHoneyFluid(pos, level, vertexConsumer, blockState, fluidState, textureAtlasSprites);
                return true;
            }

            @Override
            public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                Entity entity = camera.getEntity();
                BlockState state = level.getBlockState(entity != null ? BlockPos.containing(entity.getEyePosition(1)) : camera.getBlockPosition());
                if (state.hasProperty(HoneyFluidBlock.BOTTOM_LEVEL)) {
                    double yEye = Math.abs(entity != null ? entity.getEyePosition(1).y() : camera.getPosition().y());
                    double yOffset = yEye - ((int)yEye);
                    if (state.getValue(HoneyFluidBlock.BOTTOM_LEVEL) / 8D > yOffset + 0.1) {
                        return fluidFogColor;
                    }
                }

                // Scale the brightness of fog but make sure it is never darker than the dimension's min brightness.
                BlockPos blockpos = BlockPos.containing(camera.getEntity().getX(), camera.getEntity().getEyeY(), camera.getEntity().getZ());
                float brightnessAtEyes = LightTexture.getBrightness(camera.getEntity().level().dimensionType(), camera.getEntity().level().getMaxLocalRawBrightness(blockpos));
                float brightness = (float) Math.max(
                        Math.pow(FluidClientOverlay.getDimensionBrightnessAtEyes(camera.getEntity()), 2D),
                        brightnessAtEyes
                );
                float fogRed = 0.6F * brightness;
                float fogGreen = 0.3F * brightness;
                float fogBlue = 0.0F;
                return new Vector3f(fogRed, fogGreen, fogBlue);
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
                Entity entity = camera.getEntity();
                if (entity != null) {
                    BlockState state = entity.level().getBlockState(BlockPos.containing(entity.getEyePosition(1)));
                    if (state.hasProperty(HoneyFluidBlock.BOTTOM_LEVEL)) {
                        double yEye = Math.abs(entity.getEyePosition(1).y());
                        double yOffset = yEye - ((int)yEye);
                        if (state.getValue(HoneyFluidBlock.BOTTOM_LEVEL) / 8D > yOffset + 0.1) {
                            return;
                        }
                    }
                }

                RenderSystem.setShaderFogStart(0.35f);
                RenderSystem.setShaderFogEnd(4);
            }
        };
    }
}
