package com.telepathicgrunt.the_bumblezone.client.rendering.rootmin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.rendering.beequeen.BeeQueenModel;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RootminRenderer extends MobRenderer<RootminEntity, RootminModel> {
    private static final ResourceLocation SKIN = new ResourceLocation(Bumblezone.MODID, "textures/entity/rootmin.png");

    public RootminRenderer(EntityRendererProvider.Context context) {
        super(context, new RootminModel(context.bakeLayer(RootminModel.LAYER_LOCATION)), 0.7F);
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
}