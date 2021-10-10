package com.telepathicgrunt.the_bumblezone.client.rendering;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class BeehemothRenderer extends MobRenderer<BeehemothEntity, BeehemothModel> {
    private static final ResourceLocation SKIN = new ResourceLocation(Bumblezone.MODID, "textures/entity/beehemoth.png");

    public BeehemothRenderer(EntityRendererManager bee) {
        super(bee, new BeehemothModel(), 0.4F);
    }

    @Override
    public void render(BeehemothEntity pEntity, float pEntityYaw, float pPartialTicks, MatrixStack stack, IRenderTypeBuffer pBuffer, int pPackedLight) {
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