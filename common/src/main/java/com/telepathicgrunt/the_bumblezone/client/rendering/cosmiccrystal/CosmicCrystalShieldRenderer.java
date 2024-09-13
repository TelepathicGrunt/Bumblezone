package com.telepathicgrunt.the_bumblezone.client.rendering.cosmiccrystal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.living.CosmicCrystalEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class CosmicCrystalShieldRenderer extends RenderLayer<CosmicCrystalEntity, CosmicCrystalModel> {
    private static final ResourceLocation SHIELD_LOCATION = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/cosmic_crystal_shield.png");
    private final CosmicCrystalModel model;

    public CosmicCrystalShieldRenderer(RenderLayerParent<CosmicCrystalEntity, CosmicCrystalModel> renderLayerParent, EntityModelSet entityModelSet) {
        super(renderLayerParent);
        this.model = new CosmicCrystalModel(entityModelSet.bakeLayer(CosmicCrystalModel.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CosmicCrystalEntity entity, float f, float g, float h, float j, float k, float l) {
        if (entity.getShield()) {
            float m = (float)entity.tickCount + h;
            EntityModel<CosmicCrystalEntity> entityModel = this.model();
            entityModel.prepareMobModel(entity, f, g, h);
            this.getParentModel().copyPropertiesTo(entityModel);
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.energySwirl(this.getTextureLocation(), this.xOffset(m) % 1.0F, m * 0.01F % 1.0F));
            entityModel.setupAnim(entity, f, g, j, k, l);
            poseStack.scale(1.05f, 1.05f, 1.05f);
            entityModel.renderToBuffer(poseStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, -8355712);
        }
    }

    protected float xOffset(float f) {
        return f * 0.02F;
    }

    protected ResourceLocation getTextureLocation() {
        return SHIELD_LOCATION;
    }

    protected EntityModel<CosmicCrystalEntity> model() {
        return this.model;
    }
}