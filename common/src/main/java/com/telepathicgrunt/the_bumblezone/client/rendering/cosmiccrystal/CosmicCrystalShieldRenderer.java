package com.telepathicgrunt.the_bumblezone.client.rendering.cosmiccrystal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.CommonColors;

public class CosmicCrystalShieldRenderer extends RenderLayer<CosmicCrystalRenderState, CosmicCrystalModel> {
    private static final Identifier SHIELD_LOCATION = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/cosmic_crystal_shield.png");

    public CosmicCrystalShieldRenderer(RenderLayerParent<CosmicCrystalRenderState, CosmicCrystalModel> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, CosmicCrystalRenderState state, float yRot, float xRot) {
        if (state.shielded) {
            poseStack.scale(1.05f, 1.05f, 1.05f);
            collector.submitModel(this.getParentModel(), state, poseStack, RenderTypes.energySwirl(SHIELD_LOCATION, state.ageInTicks * 0.02F % 1.0F, state.ageInTicks * 0.01F % 1.0F), state.lightCoords, OverlayTexture.NO_OVERLAY, CommonColors.GRAY, null, state.outlineColor, null);
        }
    }
}