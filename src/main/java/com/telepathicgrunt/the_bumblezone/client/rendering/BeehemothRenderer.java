package com.telepathicgrunt.the_bumblezone.client.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BeehemothRenderer extends MobRenderer<BeehemothEntity, BeehemothModel> {
    private static final ResourceLocation SKIN = new ResourceLocation(Bumblezone.MODID, "textures/entity/beehemoth.png");

    public BeehemothRenderer(EntityRendererProvider.Context context) {
        super(context, new BeehemothModel(context.bakeLayer(BeehemothModel.LAYER_LOCATION)), 0.4F);
    }

    @Override
    public void render(BeehemothEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack stack, MultiBufferSource pBuffer, int pPackedLight) {
        stack.pushPose();
        float scale = 1.6f;
        stack.scale(scale, scale, scale);
        super.render(pEntity, pEntityYaw, pPartialTicks, stack, pBuffer, pPackedLight);
        stack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(BeehemothEntity bee) {
        return SKIN;
    }
}