package com.telepathicgrunt.the_bumblezone.client.rendering.rootmin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminState;
import com.telepathicgrunt.the_bumblezone.mixin.client.BlockRenderDispatcherAccessor;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class RootminRenderer extends MobRenderer<RootminEntity, RootminModel> {
    private static final ResourceLocation SKIN = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/rootmin.png");

    public RootminRenderer(EntityRendererProvider.Context context) {
        super(context, new RootminModel(context.bakeLayer(RootminModel.LAYER_LOCATION)), 0.7F);
        this.addLayer(new FlowerBlockLayer(this, context.getBlockRenderDispatcher()));
        this.addLayer(new RootminGrassRenderer(this, context.getModelSet()));
        this.addLayer(new RootminShieldRenderer(this, context.getModelSet()));
    }

    @Override
    public void render(RootminEntity rootminEntity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        super.render(rootminEntity, entityYaw, partialTicks, stack, buffer, packedLight);
        adjustShadow(rootminEntity, partialTicks);
        stack.popPose();
    }

    private void adjustShadow(RootminEntity rootminEntity, float partialTicks) {
        RootminState pose = rootminEntity.getRootminPose();
        float percentage = (20f - Math.max(rootminEntity.animationTimeBetweenHiding - partialTicks, 0)) / 20f;

        float target = pose == RootminState.ENTITY_TO_BLOCK ? 0f : 0.7f;
        float from = pose != RootminState.ENTITY_TO_BLOCK ? 0f : 0.7f;
        this.shadowRadius = Mth.lerp(percentage, from, target);

        target = pose == RootminState.ENTITY_TO_BLOCK ? 0f : 1.0f;
        from = pose != RootminState.ENTITY_TO_BLOCK ? 0f : 1.0f;
        this.shadowStrength = Mth.lerp(percentage, from, target);
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
        public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, RootminEntity rootminEntity, float f, float g, float h, float j, float k, float l) {
            BlockState blockState = rootminEntity.getFlowerBlock();
            if (blockState == null) {
                return;
            }

            ModelPart rootModel = this.getParentModel().root();
            ModelPart bodyModel = rootModel.getChild("body");
            poseStack.pushPose();
            rootModel.translateAndRotate(poseStack);
            bodyModel.translateAndRotate(poseStack);
            poseStack.translate(-0.5f, -15/16f, 0.5f);
            poseStack.scale(1,-1,-1);
            renderSingleBlock(blockState, poseStack, multiBufferSource, packedLight, OverlayTexture.NO_OVERLAY, rootminEntity.level(), rootminEntity.blockPosition());
            if (blockState.getBlock() instanceof DoublePlantBlock) {
                blockState = blockState.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER);
                poseStack.translate(0f, 1f, 0f);
                renderSingleBlock(blockState, poseStack, multiBufferSource, packedLight, OverlayTexture.NO_OVERLAY, rootminEntity.level(), rootminEntity.blockPosition());
            }
            poseStack.popPose();
        }

        public void renderSingleBlock(BlockState blockState, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, BlockAndTintGetter level, BlockPos blockPos) {
            RenderShape renderShape = blockState.getRenderShape();
            if (renderShape == RenderShape.INVISIBLE) {
                return;
            }
            switch (renderShape) {
                case MODEL -> {
                    BakedModel bakedModel = this.blockRenderer.getBlockModel(blockState);
                    int k = ((BlockRenderDispatcherAccessor) this.blockRenderer).getBlockColors().getColor(blockState, level, blockPos, 1);
                    float f = (float) (k >> 16 & 0xFF) / 255.0f;
                    float g = (float) (k >> 8 & 0xFF) / 255.0f;
                    float h = (float) (k & 0xFF) / 255.0f;
                    this.blockRenderer.getModelRenderer().renderModel(poseStack.last(), multiBufferSource.getBuffer(ItemBlockRenderTypes.getRenderType(blockState, false)), blockState, bakedModel, f, g, h, i, j);
                }
                case ENTITYBLOCK_ANIMATED ->
                    ((BlockRenderDispatcherAccessor) this.blockRenderer).getBlockEntityRenderer().renderByItem(new ItemStack(blockState.getBlock()), ItemDisplayContext.NONE, poseStack, multiBufferSource, i, j);
            }
        }
    }

    protected float getBob(RootminEntity livingEntity, float f) {
        return super.getBob(livingEntity, f);
    }

    protected float getAttackAnim(RootminEntity livingEntity, float f) {
        return super.getAttackAnim(livingEntity, f);
    }

    protected boolean isBodyVisible(RootminEntity livingEntity) {
        return super.isBodyVisible(livingEntity);
    }

    protected float getWhiteOverlayProgress(RootminEntity livingEntity, float f) {
        return super.getWhiteOverlayProgress(livingEntity, f);
    }
}