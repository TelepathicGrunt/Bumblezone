package com.telepathicgrunt.the_bumblezone.client.rendering.fluids;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamresourceful.resourcefullib.client.fluid.data.ClientFluidProperties;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluidBlock;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.Lightmap;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidRenderer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;

public class RoyalJellyClientProperties {
    public static final Identifier ROYAL_JELLY_FLUID_STILL_TEXTURE = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "block/royal_jelly_fluid/still");
    public static final Identifier ROYAL_JELLY_FLUID_FLOWING_TEXTURE = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "block/royal_jelly_fluid/flow");
    public static final Identifier ROYAL_JELLY_FLUID_FLOWING_DIAGONAL_TEXTURE = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "block/royal_jelly_fluid/flow_diagonal");

    public static ClientFluidProperties create() {
        return new BzCachingClientFluidProperties() {

            @Override
            public Material still() {
                return new Material(ROYAL_JELLY_FLUID_STILL_TEXTURE);
            }

            @Override
            public Material flowing() {
                return new Material(ROYAL_JELLY_FLUID_FLOWING_TEXTURE);
            }

            @Override
            public Material flowingDiagonal() {
                return new Material(ROYAL_JELLY_FLUID_FLOWING_DIAGONAL_TEXTURE);
            }

            @Override
            public Material overlay() {
                return new Material(ROYAL_JELLY_FLUID_FLOWING_TEXTURE);
            }

            @Override
            public Identifier screenOverlay() {
                return null;
            }

            @Override
            public void renderOverlay(Minecraft minecraft, PoseStack stack, MultiBufferSource source) {
                FluidClientOverlay.renderHoneyOverlay(minecraft.player, stack, source);
            }

            @Override
            public int tintColor(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, @Nullable FluidState state) {
                return 0xFFFFFFFF;
            }

            @Override
            public boolean renderFluid(BlockPos pos, BlockAndTintGetter level, FluidRenderer.Output output, BlockState blockState, FluidState fluidState) {
                VertexConsumer vertexConsumer = output.getBuilder(ChunkSectionLayer.TRANSLUCENT);
                HoneyFluidRendering.renderSpecialHoneyFluid(pos, level, vertexConsumer, blockState, fluidState, this);
                return true;
            }

            @Override
            public Vector4f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector4f fluidFogColor) {
                Entity entity = camera.entity();
                BlockState state = level.getBlockState(entity != null ? BlockPos.containing(entity.getEyePosition(1)) : camera.blockPosition());
                if (state.hasProperty(HoneyFluidBlock.BOTTOM_LEVEL)) {
                    double yEye = Math.abs(entity != null ? entity.getEyePosition(1).y() : camera.position().y());
                    double yOffset = yEye - ((int)yEye);
                    if (state.getValue(HoneyFluidBlock.BOTTOM_LEVEL) / 8D > yOffset + 0.1) {
                        return fluidFogColor;
                    }
                }

                // Scale the brightness of fog but make sure it is never darker than the dimension's min brightness.
                BlockPos blockpos = BlockPos.containing(entity.getX(), entity.getEyeY(), entity.getZ());
                float brightnessAtEyes = Lightmap.getBrightness(entity.level().dimensionType(), entity.level().getMaxLocalRawBrightness(blockpos));
                float brightness = (float) Math.max(
                        Math.pow(FluidClientOverlay.getDimensionBrightnessAtEyes(entity), 2D),
                        brightnessAtEyes
                );
                float fogRed = 0.5F * brightness;
                float fogGreen = 0.0F;
                float fogBlue = 0.55F * brightness;
                return new Vector4f(fogRed, fogGreen, fogBlue, 1.0F);
            }

            @Override
            public FogData modifyFogRender(Camera camera, float renderDistance, float partialTick, FogData data) {
                Entity entity = camera.entity();
                if (entity != null) {
                    BlockState state = entity.level().getBlockState(BlockPos.containing(entity.getEyePosition(1)));
                    if (state.hasProperty(HoneyFluidBlock.BOTTOM_LEVEL)) {
                        double yEye = Math.abs(entity.getEyePosition(1).y());
                        double yOffset = yEye - ((int)yEye);
                        if (state.getValue(HoneyFluidBlock.BOTTOM_LEVEL) / 8D > yOffset + 0.1) {
                            return data;
                        }
                    }
                }

                data.environmentalStart = 0.35f;
                data.renderDistanceEnd = 4f;
                return data;
            }
        };
    }
}
