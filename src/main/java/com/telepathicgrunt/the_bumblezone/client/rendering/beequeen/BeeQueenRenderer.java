package com.telepathicgrunt.the_bumblezone.client.rendering.beequeen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BeeQueenRenderer extends MobRenderer<BeeQueenEntity, BeeQueenModel> {
    private static final ResourceLocation SKIN = new ResourceLocation(Bumblezone.MODID, "textures/entity/bee_queen.png");

    public BeeQueenRenderer(EntityRendererProvider.Context context) {
        super(context, new BeeQueenModel(context.bakeLayer(BeeQueenModel.LAYER_LOCATION)), 1.2F);
    }

    @Override
    public void render(BeeQueenEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack stack, MultiBufferSource pBuffer, int pPackedLight) {
        stack.pushPose();
        float scale = 2.3f;
        stack.scale(scale, scale, scale);
        super.render(pEntity, pEntityYaw, pPartialTicks, stack, pBuffer, pPackedLight);
        stack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(BeeQueenEntity bee) {
        return SKIN;
    }
}