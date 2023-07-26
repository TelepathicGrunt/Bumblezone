package com.telepathicgrunt.the_bumblezone.client.rendering.boundlesscrystal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.living.BoundlessCrystalEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class BoundlessCrystalRenderer extends LivingEntityRenderer<BoundlessCrystalEntity, BoundlessCrystalModel<BoundlessCrystalEntity>> {
    private static final ResourceLocation SKIN = new ResourceLocation(Bumblezone.MODID, "textures/entity/boundless_crystal.png");

    public BoundlessCrystalRenderer(EntityRendererProvider.Context context) {
        super(context, new BoundlessCrystalModel<>(context.bakeLayer(BoundlessCrystalModel.LAYER_LOCATION)), 0.7F);
    }

    @Override
    public void render(BoundlessCrystalEntity boundlessCrystalEntity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        super.render(boundlessCrystalEntity, entityYaw, partialTicks, stack, buffer, packedLight);
        stack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(BoundlessCrystalEntity boundlessCrystalEntity) {
        return SKIN;
    }

    @Override
    protected boolean shouldShowName(BoundlessCrystalEntity entity) {
        return false;
    }
}