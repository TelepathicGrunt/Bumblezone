package com.telepathicgrunt.the_bumblezone.client.rendering.beehemoth;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public class BeehemothRenderer extends MobRenderer<BeehemothEntity, BeehemothModel> {
    private static final ResourceLocation SKIN = new ResourceLocation(Bumblezone.MODID, "textures/entity/beehemoth.png");

    public BeehemothRenderer(EntityRendererProvider.Context context) {
        super(context, new BeehemothModel(context.bakeLayer(BeehemothModel.LAYER_LOCATION)), 0.4F);
    }

    @Override
    public void render(BeehemothEntity beehemothEntity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        super.render(beehemothEntity, entityYaw, partialTicks, stack, buffer, packedLight);
        stack.popPose();

        if (beehemothEntity == this.entityRenderDispatcher.crosshairPickEntity) {
            if (!beehemothEntity.isQueen() && beehemothEntity.isTame() && beehemothEntity.isOwnedBy(Minecraft.getInstance().player)) {
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
        poseStack.pushPose();
        poseStack.translate(0.0f, f, 0.0f);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.scale(-0.025f, -0.025f, 0.025f);
        Matrix4f matrix4f = poseStack.last().pose();
        float g = Minecraft.getInstance().options.getBackgroundOpacity(0.25f);
        int k = (int)(g * 255.0f) << 24;
        Font font = this.getFont();
        float h = -font.width(component) / 2F;
        font.drawInBatch(component, h, 0, 0x20FFFFFF, false, matrix4f, multiBufferSource, false, k, packedLight);
        font.drawInBatch(component, h, 0, -1, false, matrix4f, multiBufferSource, false, 0, packedLight);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(BeehemothEntity bee) {
        return SKIN;
    }
}