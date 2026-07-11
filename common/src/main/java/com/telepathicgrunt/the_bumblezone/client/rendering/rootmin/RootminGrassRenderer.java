package com.telepathicgrunt.the_bumblezone.client.rendering.rootmin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public class RootminGrassRenderer extends RenderLayer<RootminRenderState, RootminModel> {
    private static final Identifier GRASS = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/rootmin_grass.png");

    public RootminGrassRenderer(RootminRenderer renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, RootminRenderState state, float yRot, float xRot) {
        boolean isBodyVisible = !state.isInvisible;
        boolean forceTransparent = !isBodyVisible && !state.isInvisibleToPlayer;
        RenderType renderType = this.getRenderType(isBodyVisible, forceTransparent, state.appearsGlowing());
        if (renderType != null) {
            collector.submitModel(this.getParentModel(), state, poseStack, renderType, lightCoords, LivingEntityRenderer.getOverlayCoords(state, 0.0F), state.getGrassColor(), null, state.outlineColor, null);
        }
    }

    @Nullable
    private RenderType getRenderType(boolean isBodyVisible, boolean forceTransparent, boolean appearGlowing) {
        if (forceTransparent) {
            return RenderTypes.entityTranslucentCullItemTarget(GRASS);
        } else if (isBodyVisible) {
            return this.getParentModel().renderType(GRASS);
        } else {
            return appearGlowing ? RenderTypes.outline(GRASS) : null;
        }
    }
}