package com.telepathicgrunt.the_bumblezone.client.rendering.beequeen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class BeeQueenRenderer extends MobRenderer<BeeQueenEntity, BeeQueenRenderState, BeeQueenModel> {
    private static final Identifier SKIN = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/bee_queen/normal.png");
    private static final Identifier ANGRY_SKIN = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/bee_queen/angry.png");
    public final static Identifier SPEECH_BUBBLE_TEXTURE = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/bee_queen/trade_hint.png");
    private static final RenderType SPEECH_BUBBLE_RENDER_TYPE = RenderTypes.entityCutout(SPEECH_BUBBLE_TEXTURE, false);
    private static final Vector3f[] TRADE_HINT_VERTEX_POSITIONS = new Vector3f[]{
            new Vector3f(-1.0F, -1.0F, -0.01F),
            new Vector3f(-1.0F, 1.0F, -0.01F),
            new Vector3f(1.0F, 1.0F, -0.01F),
            new Vector3f(1.0F, -1.0F, -0.01F)};

    public final static int TRADE_REWARD_CYCLE_TIME = 40;

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
        itemModelResolver.updateForLiving(state.wantItem, entity.getWantItemForTradeHint(), ItemDisplayContext.GUI, entity);
        List<ItemStack> slicedRewardItemsForTradeHint = entity.getSlicedRewardItemsForTradeHint();
        state.slicedRewardItems = new ArrayList<>(slicedRewardItemsForTradeHint.size());
        for (ItemStack itemStack : slicedRewardItemsForTradeHint) {
            ItemStackRenderState itemStackRenderState = new ItemStackRenderState();
            itemModelResolver.updateForLiving(itemStackRenderState, itemStack, ItemDisplayContext.GUI, entity);
            state.slicedRewardItems.add(itemStackRenderState);
        }
        state.tradeHintTimeRemaining = entity.tradeHintTimeRemaining;
        state.animationProgress = getAnimationProgressForRemainingTime(state.tradeHintTimeRemaining, state.ageInTicks, state.animationProgress);
    }

    @Override
    public Identifier getTextureLocation(BeeQueenRenderState bee) {
        return bee.angry ? ANGRY_SKIN : SKIN;
    }

    @Override
    public void submit(BeeQueenRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);

        if (!BzClientConfigs.showBeeQueenSpeechBubble) {
            return;
        }

        if (state.tradeHintTimeRemaining > 0 && !state.wantItem.isEmpty() && !state.slicedRewardItems.isEmpty()) {
            float offset = 1 - state.animationProgress;
            Vector3f offsetVec = new Vector3f(-offset, -offset, 0.0F);

            Vec3 vec3 = camera.pos;
    //        Vec2 frontOffset = (new Vec2((float) (vec3.x() - this.x), (float) (vec3.z() - this.z))).normalized();
    //        float x = (float)(Mth.lerp(partialTick, this.xo, this.x) - vec3.x()) + offsetVec.x() + frontOffset.x;
    //        float y = (float)(Mth.lerp(partialTick, this.yo, this.y) - vec3.y()) + offsetVec.y();
    //        float z = (float)(Mth.lerp(partialTick, this.zo, this.z) - vec3.z()) + offsetVec.z() + frontOffset.y;
            float x = 0;
            float y = 4.5f;
            float z = 0;

    //        poseStack.translate(x, y, z);
    //        poseStack.scale(state.animationProgress, state.animationProgress, state.animationProgress);

            poseStack.pushPose();
            poseStack.translate(x, y, z);
            poseStack.mulPose(camera.orientation);

            submitNodeCollector.submitCustomGeometry(poseStack, SPEECH_BUBBLE_RENDER_TYPE, (pose, buffer) -> {
                float u0 = 0;
                float u1 = 1;
                float v0 = 0;
                float v1 = 1;
                int lightCoords = 255;

                buffer.addVertex(pose, TRADE_HINT_VERTEX_POSITIONS[0].x(), TRADE_HINT_VERTEX_POSITIONS[0].y(), TRADE_HINT_VERTEX_POSITIONS[0].z()).setColor(ARGB.white(255)).setUv(u1, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(pose, 0.0f, -1.0f, 0.0f);
                buffer.addVertex(pose, TRADE_HINT_VERTEX_POSITIONS[1].x(), TRADE_HINT_VERTEX_POSITIONS[1].y(), TRADE_HINT_VERTEX_POSITIONS[1].z()).setColor(ARGB.white(255)).setUv(u1, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(pose, 0.0f, -1.0f, 0.0f);
                buffer.addVertex(pose, TRADE_HINT_VERTEX_POSITIONS[2].x(), TRADE_HINT_VERTEX_POSITIONS[2].y(), TRADE_HINT_VERTEX_POSITIONS[2].z()).setColor(ARGB.white(255)).setUv(u0, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(pose, 0.0f, -1.0f, 0.0f);
                buffer.addVertex(pose, TRADE_HINT_VERTEX_POSITIONS[3].x(), TRADE_HINT_VERTEX_POSITIONS[3].y(), TRADE_HINT_VERTEX_POSITIONS[3].z()).setColor(ARGB.white(255)).setUv(u0, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(pose, 0.0f, -1.0f, 0.0f);
            });

            Quaternionf reverseQuad = new Quaternionf(0, 0, 0, 1);
            reverseQuad.rotateAxis(Mth.PI, 0 , 1, 0); // flip around y-axis because block items were facing backwards

            // Want item rendering
            poseStack.pushPose();
            setupPoseStack(poseStack, 0.31F, 0.43F, -0.001F);
            renderItem(state.wantItem, poseStack, submitNodeCollector);
            poseStack.popPose();

            // Reward item rendering
            poseStack.pushPose();
            setupPoseStack(poseStack, -0.31F, -0.13F, -0.001F);
            ItemStackRenderState slicedRewardItemStackRenderState = state.slicedRewardItems.get((state.tradeHintTimeRemaining / TRADE_REWARD_CYCLE_TIME) % state.slicedRewardItems.size());
            renderItem(slicedRewardItemStackRenderState, poseStack, submitNodeCollector);
            poseStack.popPose();

            poseStack.popPose();
        }
    }

    private static void setupPoseStack(PoseStack poseStack,
                                       float xOffset,
                                       float yOffset,
                                       float zOffset)
    {
        poseStack.last().pose().translate(xOffset, yOffset, zOffset); // move to correct spot on sprite
        poseStack.last().pose().scale(0.5F, 0.5F, 0.001F); // scale to correct size on the sprite at location
        Quaternionf normalToUse = new Quaternionf(-0.3F, 0F, 0F, 1F); // Controls the lighting on the items...
        poseStack.last().normal().set(normalToUse); // set normal for lighting
    }

    private float getAnimationProgressForRemainingTime(long remainingTime, float partialTick, float pastAnimationProgress) {
        float animationProgress = 1;
        float animationChangeTime = 20F;

        if (remainingTime <= animationChangeTime) {
            float currentProgress = remainingTime / animationChangeTime;
            float c1 = 1.70158F;
            float c3 = c1 + 1;
            float calcSize = (float) (1 + c3 * Math.pow(currentProgress - 1, 3) + c1 * Math.pow(currentProgress - 1, 2));
            animationProgress = Mth.lerp(partialTick, pastAnimationProgress, calcSize);
        }
        else if (BeeQueenEntity.TRADE_HINT_PARTICLE_LIFETIME - remainingTime <= (animationChangeTime / 2)) {
            float currentProgress = (BeeQueenEntity.TRADE_HINT_PARTICLE_LIFETIME - remainingTime) / (animationChangeTime / 2);
            float calcSize = (float) (1 - Math.cos((currentProgress * Math.PI) / 2));
            animationProgress = Mth.lerp(partialTick, pastAnimationProgress, calcSize);
        }

        return animationProgress;
    }

    private void renderItem(ItemStackRenderState itemStackRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
        if (itemStackRenderState.isEmpty()) {
            return;
        }
        itemStackRenderState.submit(poseStack, submitNodeCollector, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
    }
}