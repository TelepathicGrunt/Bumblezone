package com.telepathicgrunt.the_bumblezone.client.rendering.rootmin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class RootminRenderer extends MobRenderer<RootminEntity, RootminModel> {
    private static final ResourceLocation SKIN = new ResourceLocation(Bumblezone.MODID, "textures/entity/rootmin.png");

    public RootminRenderer(EntityRendererProvider.Context context) {
        super(context, new RootminModel(context.bakeLayer(RootminModel.LAYER_LOCATION)), 0.7F);
        this.addLayer(new FlowerBlockLayer(this, context.getBlockRenderDispatcher()));
    }

    @Override
    public void render(RootminEntity rootminEntity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        super.render(rootminEntity, entityYaw, partialTicks, stack, buffer, packedLight);
        stack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(RootminEntity rootminEntity) {
        return SKIN;
    }


    public static class FlowerBlockLayer extends RenderLayer<RootminEntity, RootminModel> {
        private final BlockRenderDispatcher blockRenderer;

        public FlowerBlockLayer(RenderLayerParent<RootminEntity, RootminModel> renderLayerParent, BlockRenderDispatcher blockRenderDispatcher) {
            super(renderLayerParent);
            this.blockRenderer = blockRenderDispatcher;
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, RootminEntity rootminEntity, float f, float g, float h, float j, float k, float l) {
            BlockState blockState = rootminEntity.getFlowerBlock();
            if (blockState == null) {
                return;
            }

            ModelPart bodyModel = this.getParentModel().root().getChild("body");
            double xSize = rootminEntity.getBoundingBox().getXsize();
            double ySize = rootminEntity.getBoundingBox().getYsize();
            double zSize = rootminEntity.getBoundingBox().getZsize();

            poseStack.pushPose();
            poseStack.translate(bodyModel.x, -bodyModel.y, bodyModel.z);
            poseStack.mulPose(Axis.XP.rotationDegrees(bodyModel.xRot));
            poseStack.mulPose(Axis.YP.rotationDegrees(bodyModel.yRot));
            poseStack.mulPose(Axis.ZP.rotationDegrees(bodyModel.zRot));
            poseStack.translate(-xSize/2, bodyModel.y - 0.05f, -zSize/2);
            poseStack.scale(1, -1, 1);

            this.blockRenderer.renderSingleBlock(blockState, poseStack, multiBufferSource, i, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        }
    }
}