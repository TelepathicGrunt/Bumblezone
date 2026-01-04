package com.telepathicgrunt.the_bumblezone.client.rendering.beehemoth;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;

public class BeehemothRenderer extends MobRenderer<BeehemothEntity, BeehemothModel> {
    private static final Identifier SKIN = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/beehemoth.png");

    public BeehemothRenderer(EntityRendererProvider.Context context) {
        super(context, new BeehemothModel(context.bakeLayer(BeehemothModel.LAYER_LOCATION)), 0.4F);
    }

    @Override
    public void render(BeehemothEntity beehemothEntity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        super.render(beehemothEntity, entityYaw, partialTicks, stack, buffer, packedLight);
        stack.popPose();

        if (beehemothEntity == this.entityRenderDispatcher.crosshairPickEntity) {
            if (!beehemothEntity.isMaxFriendship() && beehemothEntity.isTame() && beehemothEntity.isOwnedBy(GeneralUtilsClient.getClientPlayer())) {
                renderFriendshipProgress(
                        beehemothEntity,
                        Component.translatable("entity.the_bumblezone.beehemoth_friendship_progress", beehemothEntity.getFriendship()),
                        stack,
                        buffer,
                        packedLight);
            }
        }
    }


    protected void renderFriendshipProgress(BeehemothEntity entity, Component component, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        if (this.entityRenderDispatcher.distanceToSqr(entity) > 100.0) {
            return;
        }

        float f = entity.getBbHeight() + 0.75F;
        Font font = this.getFont();
        float h = -font.width(component) / 2F;

        float shadowXYOffsets = 0.011f;
        float shadowZOffsets = -0.0001f;
        float textSize = 0.025f;

        // Shadow
        RenderTextShadow(component, poseStack, multiBufferSource, packedLight, f, shadowXYOffsets, shadowXYOffsets, shadowZOffsets, textSize, font, h);
        RenderTextShadow(component, poseStack, multiBufferSource, packedLight, f, shadowXYOffsets, -shadowXYOffsets, shadowZOffsets, textSize, font, h);
        RenderTextShadow(component, poseStack, multiBufferSource, packedLight, f, -shadowXYOffsets, shadowXYOffsets, shadowZOffsets, textSize, font, h);
        RenderTextShadow(component, poseStack, multiBufferSource, packedLight, f, -shadowXYOffsets, -shadowXYOffsets, shadowZOffsets, textSize, font, h);

        // Actual text
        poseStack.pushPose();
        poseStack.translate(0.0f, f, 0.0f);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.scale(textSize, -textSize, textSize);
        Matrix4f matrix4f = poseStack.last().pose();
        font.drawInBatch(component, h, 0, -1, false, matrix4f, multiBufferSource, Font.DisplayMode.NORMAL, 0, packedLight);
        poseStack.popPose();
    }

    private void RenderTextShadow(
            Component component,
            PoseStack poseStack,
            MultiBufferSource multiBufferSource,
            int packedLight,
            float f,
            float shadowXOffsets,
            float shadowYOffsets,
            float shadowZOffsets,
            float textSize,
            Font font,
            float h)
    {
        poseStack.pushPose();
        poseStack.translate(0.0f, f, 0.0f);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.translate(shadowXOffsets, shadowYOffsets, shadowZOffsets);
        poseStack.scale(textSize, -textSize, textSize);
        Matrix4f matrix4f = poseStack.last().pose();
        font.drawInBatch(component, h, 0, 0, false, matrix4f, multiBufferSource, Font.DisplayMode.NORMAL, 0, packedLight);
        poseStack.popPose();
    }

    @Override
    public Identifier getTextureLocation(BeehemothEntity bee) {
        return SKIN;
    }
}