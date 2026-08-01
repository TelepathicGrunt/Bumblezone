package com.telepathicgrunt.the_bumblezone.client.rendering.beequeen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class BeeQueenRenderer extends MobRenderer<BeeQueenEntity, BeeQueenRenderState, BeeQueenModel> {
    private static final Identifier SKIN = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/bee_queen.png");
    private static final Identifier ANGRY_SKIN = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/bee_queen_angry.png");

    public BeeQueenRenderer(EntityRendererProvider.Context context) {
        super(context, new BeeQueenModel(context.bakeLayer(BeeQueenModel.LAYER_LOCATION)), 1.2F);
    }

    @Override
    protected void scale(BeeQueenRenderState state, PoseStack poseStack) {
        float scale = 2.6f;
        poseStack.scale(scale, scale, scale);
    }

    @Override
    public BeeQueenRenderState createRenderState() {
        return new BeeQueenRenderState();
    }

    @Override
    public void extractRenderState(BeeQueenEntity entity, BeeQueenRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.attackAnimationState.copyFrom(entity.attackAnimationState);
        state.idleAnimationState.copyFrom(entity.idleAnimationState);
        state.itemThrownAnimationState.copyFrom(entity.itemThrownAnimationState);
        state.itemRejectAnimationState.copyFrom(entity.itemRejectAnimationState);
        state.angry = entity.isAngry();
    }

    @Override
    public Identifier getTextureLocation(BeeQueenRenderState bee) {
        return bee.angry ? ANGRY_SKIN : SKIN;
    }
}