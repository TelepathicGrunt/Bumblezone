package com.telepathicgrunt.the_bumblezone.client.rendering.beequeen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BeeQueenRenderer extends MobRenderer<BeeQueenEntity, BeeQueenModel> {
    private static final ResourceLocation SKIN = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/bee_queen.png");
    private static final ResourceLocation ANGRY_SKIN = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/bee_queen_angry.png");
    private final ItemInHandRenderer itemRenderer;

    public BeeQueenRenderer(EntityRendererProvider.Context context) {
        super(context, new BeeQueenModel(context.bakeLayer(BeeQueenModel.LAYER_LOCATION)), 1.2F);
        this.itemRenderer = context.getItemInHandRenderer();
    }

    @Override
    public void render(BeeQueenEntity beeQueenEntity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        super.render(beeQueenEntity, entityYaw, partialTicks, stack, buffer, packedLight);
        stack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(BeeQueenEntity bee) {
        return bee.isAngry() ? ANGRY_SKIN : SKIN;
    }
}