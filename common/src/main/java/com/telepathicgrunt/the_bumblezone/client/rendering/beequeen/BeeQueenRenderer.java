package com.telepathicgrunt.the_bumblezone.client.rendering.beequeen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.rendering.transparentitem.TranslucentItemRenderTypeBuffer;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BeeQueenRenderer extends MobRenderer<BeeQueenEntity, BeeQueenModel> {
    private static final ResourceLocation SKIN = new ResourceLocation(Bumblezone.MODID, "textures/entity/bee_queen.png");
    private static final ResourceLocation ANGRY_SKIN = new ResourceLocation(Bumblezone.MODID, "textures/entity/bee_queen_angry.png");

    public BeeQueenRenderer(EntityRendererProvider.Context context) {
        super(context, new BeeQueenModel(context.bakeLayer(BeeQueenModel.LAYER_LOCATION)), 1.2F);
    }

    @Override
    public void render(BeeQueenEntity beeQueenEntity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        super.render(beeQueenEntity, entityYaw, partialTicks, stack, buffer, packedLight);
        stack.popPose();

        if (!beeQueenEntity.isAngry()) {
            stack.pushPose();
            stack.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(BeeQueenEntity bee) {
        return bee.isAngry() ? ANGRY_SKIN : SKIN;
    }
}