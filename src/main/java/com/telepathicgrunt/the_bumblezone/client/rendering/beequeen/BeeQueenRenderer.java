package com.telepathicgrunt.the_bumblezone.client.rendering.beequeen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.rendering.TranslucentItemRenderTypeBuffer;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class BeeQueenRenderer extends MobRenderer<BeeQueenEntity, BeeQueenModel> {
    private static final ResourceLocation SKIN = new ResourceLocation(Bumblezone.MODID, "textures/entity/bee_queen.png");
    private static final ResourceLocation ANGRY_SKIN = new ResourceLocation(Bumblezone.MODID, "textures/entity/bee_queen_angry.png");
    private final ItemInHandRenderer itemRenderer;

    public BeeQueenRenderer(EntityRendererProvider.Context context) {
        super(context, new BeeQueenModel(context.bakeLayer(BeeQueenModel.LAYER_LOCATION)), 1.2F);
        this.itemRenderer = context.getItemInHandRenderer();
    }

    @Override
    public void render(BeeQueenEntity beeQueenEntity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        float scale = 2.6f;
        stack.scale(scale, scale, scale);
        super.render(beeQueenEntity, entityYaw, partialTicks, stack, buffer, packedLight);
        stack.popPose();

        if (BzClientConfigs.renderBeeQueenSuperTradeItem.get() && !beeQueenEntity.getSuperTradeItem().isEmpty()) {
            stack.pushPose();
            stack.scale(scale, scale, scale);
            float rotYaw = Mth.rotLerp(partialTicks, beeQueenEntity.yBodyRotO, beeQueenEntity.yBodyRot);
            stack.mulPose(Vector3f.YP.rotationDegrees(180.0F - rotYaw + 180.0f));
            this.getModel().root().translateAndRotate(stack);
            this.getModel().root().getChild("segment3").translateAndRotate(stack);
            stack.translate(-0.15f, -1.25f, -0.22f);
            stack.mulPose(Vector3f.YP.rotationDegrees(-75F));
            stack.mulPose(Vector3f.XP.rotationDegrees(-20F));
            stack.scale(0.6f, 0.6f, 0.6f);

            int alpha = 150;
            TranslucentItemRenderTypeBuffer translucentItemRenderTypeBuffer = new TranslucentItemRenderTypeBuffer(buffer, alpha);
            this.itemRenderer.renderItem(
                    beeQueenEntity,
                    beeQueenEntity.getSuperTradeItem(),
                    ItemTransforms.TransformType.GROUND,
                    false,
                    stack,
                    translucentItemRenderTypeBuffer,
                    packedLight);
            stack.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(BeeQueenEntity bee) {
        return bee.isAngry() ? ANGRY_SKIN : SKIN;
    }
}