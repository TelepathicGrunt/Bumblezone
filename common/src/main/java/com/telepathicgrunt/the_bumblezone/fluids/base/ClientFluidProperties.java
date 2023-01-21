package com.telepathicgrunt.the_bumblezone.fluids.base;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class ClientFluidProperties {

    private static final Map<ResourceLocation, ClientFluidProperties> PROPERTIES = new HashMap<>();

    private ModifyFogColor modifyFogColor = null;
    private ModifyFog modifyFog = null;
    private FluidOverlay fluidOverlay = null;
    private TriFunction<FluidState, BlockAndTintGetter, BlockPos, Integer> tintColor = (state, level, pos) -> -1;
    private ResourceLocation still;
    private ResourceLocation flowing;
    private ResourceLocation overlay;

    public ClientFluidProperties(FluidProperties properties) {
        this(properties.id());
    }

    public ClientFluidProperties(ResourceLocation id) {
        PROPERTIES.put(id, this);
    }

    public ClientFluidProperties modifyFogColor(ModifyFogColor modifyFogColor) {
        this.modifyFogColor = modifyFogColor;
        return this;
    }

    public ClientFluidProperties modifyFog(ModifyFog modifyFog) {
        this.modifyFog = modifyFog;
        return this;
    }


    public ClientFluidProperties tintColor(int tintColor) {
        this.tintColor = (state, level, pos) -> tintColor;
        return this;
    }
    public ClientFluidProperties tintColor(TriFunction<FluidState, BlockAndTintGetter, BlockPos, Integer> tintColor) {
        this.tintColor = tintColor;
        return this;
    }

    public ClientFluidProperties still(ResourceLocation still) {
        this.still = still;
        return this;
    }

    public ClientFluidProperties flowing(ResourceLocation flowing) {
        this.flowing = flowing;
        return this;
    }

    public ClientFluidProperties overlay(ResourceLocation overlay) {
        this.overlay = overlay;
        return this;
    }

    public ClientFluidProperties screenOverlay(ResourceLocation screenOverlay) {
        this.fluidOverlay = (mc, stack) -> {
            if (mc.player == null) return;
            bz$renderFluid(mc.player, stack, screenOverlay);
        };
        return this;
    }

    public ClientFluidProperties screenOverlay(BiConsumer<Player, PoseStack> fluidOverlay) {
        this.fluidOverlay = (mc, stack) -> {
            if (mc.player == null) return;
            fluidOverlay.accept(mc.player, stack);
        };
        return this;
    }

    public static ClientFluidProperties get(ResourceLocation id) {
        return PROPERTIES.get(id);
    }

    public ResourceLocation still() {
        return still;
    }

    public ResourceLocation flowing() {
        return flowing;
    }

    public ResourceLocation overlay() {
        return overlay;
    }

    public void fluidOverlay(Minecraft mc, PoseStack stack) {
        if (fluidOverlay != null) fluidOverlay.render(mc, stack);
    }

    public int tintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return tintColor.apply(state, getter, pos);
    }

    public Optional<Vector3f> modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
        if (modifyFogColor != null) {
            return Optional.of(modifyFogColor.modify(camera, partialTick, level, renderDistance, darkenWorldAmount, fluidFogColor));
        }
        return Optional.empty();
    }

    public void modifyFog(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
        if (modifyFog != null) {
            modifyFog.modify(camera, mode, renderDistance, partialTick, nearDistance, farDistance, shape);
        }
    }

    @FunctionalInterface
    public interface ModifyFogColor {
        Vector3f modify(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor);
    }

    @FunctionalInterface
    public interface ModifyFog {
        void modify(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape);
    }

    @FunctionalInterface
    public interface FluidOverlay {
        void render(Minecraft mc, PoseStack poseStack);
    }


    private static void bz$renderFluid(@NotNull Player player, PoseStack pPoseStack, ResourceLocation texture) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableTexture();
        RenderSystem.setShaderTexture(0, texture);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        BlockPos blockpos = new BlockPos(player.getX(), player.getEyeY(), player.getZ());
        float f = LightTexture.getBrightness(player.level.dimensionType(), player.level.getMaxLocalRawBrightness(blockpos));
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(f, f, f, 0.1F);
        float f7 = -player.getYRot() / 64.0F;
        float f8 = player.getXRot() / 64.0F;
        Matrix4f matrix4f = pPoseStack.last().pose();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).uv(4.0F + f7, 4.0F + f8).endVertex();
        bufferbuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).uv(0.0F + f7, 4.0F + f8).endVertex();
        bufferbuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).uv(0.0F + f7, 0.0F + f8).endVertex();
        bufferbuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).uv(4.0F + f7, 0.0F + f8).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.disableBlend();
    }
}
