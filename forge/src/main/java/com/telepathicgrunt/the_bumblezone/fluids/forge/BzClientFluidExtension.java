package com.telepathicgrunt.the_bumblezone.fluids.forge;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.fluids.base.ClientFluidProperties;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Optional;

public class BzClientFluidExtension implements IClientFluidTypeExtensions {

    private final BzFluidType type;
    private ClientFluidProperties properties;

    public BzClientFluidExtension(BzFluidType type) {
        this.type = type;
    }

    private void checkForProperties() {
        if (properties == null) {
            properties = ClientFluidProperties.get(type.properties().id());
            if (properties == null) {
                throw new IllegalStateException("Client Fluid properties for " + type.properties().id() + " is missing!");
            }
        }
    }

    @Override
    public ResourceLocation getStillTexture() {
        checkForProperties();
        return properties.still();
    }

    @Override
    public ResourceLocation getFlowingTexture() {
        checkForProperties();
        return properties.flowing();
    }

    @Nullable
    @Override
    public ResourceLocation getOverlayTexture() {
        checkForProperties();
        return properties.overlay();
    }

    @Override
    public void renderOverlay(Minecraft mc, PoseStack poseStack) {
        checkForProperties();
        properties.fluidOverlay(mc, poseStack);
    }

    @Override
    public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        checkForProperties();
        return properties.tintColor(state, getter, pos);
    }

    @Override
    public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
        checkForProperties();
        Optional<Vector3f> result = properties.modifyFogColor(camera, partialTick, level, renderDistance, darkenWorldAmount, fluidFogColor);
        return result.orElse(fluidFogColor);
    }

    @Override
    public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
        checkForProperties();
        properties.modifyFog(camera, mode, renderDistance, partialTick, nearDistance, farDistance, shape);
    }
}
