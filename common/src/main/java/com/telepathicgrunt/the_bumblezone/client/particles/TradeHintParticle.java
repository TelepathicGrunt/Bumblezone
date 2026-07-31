package com.telepathicgrunt.the_bumblezone.client.particles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

//public class TradeHintParticle extends Particle {
//    public final static int TRADE_REWARD_CYCLE_TIME = 40;
//    public final static double PARTICLE_Y_OFFSET = 4D;
//    public final static Identifier SPEECH_BUBBLE_TEXTURE = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "trade_hint/1");
//
//    private final RenderBuffers renderBuffers;
//    private final Item tradeWantItem;
//    private final List<ItemStack> tradeRewardItems;
//    protected TextureAtlasSprite sprite;
//    private int life;
//    private float pastAnimationProgress;
//
//    public TradeHintParticle(RenderBuffers arg2, ClientLevel arg3, Entity queen, Item tradeWantItem, List<ItemStack> tradeRewardItems) {
//        super(arg3, queen.getX(), queen.getY() + PARTICLE_Y_OFFSET, queen.getZ(), 0, 0, 0);
//        this.renderBuffers = arg2;
//        this.tradeWantItem = tradeWantItem;
//        this.tradeRewardItems = tradeRewardItems;
//        this.sprite = ((ParticleEngineAccessor)Minecraft.getInstance().particleEngine).bumblezone$getTextureAtlas().getSprite(SPEECH_BUBBLE_TEXTURE);
//    }
//
//    @Override
//    public void render(VertexConsumer doNotUse, Camera camera, float partialTick) {
//        if (!BzClientConfigs.showBeeQueenSpeechBubble) {
//            return;
//        }
//
//        float animationProgress = getAnimationProgressForCurrentLife(partialTick);
//        this.pastAnimationProgress = animationProgress;
//
//        Quaternionf cameraRotationQuat;
//        if (this.roll == 0.0F) {
//            cameraRotationQuat = new Quaternionf(camera.rotation());
//        }
//        else {
//            cameraRotationQuat = new Quaternionf(camera.rotation());
//            cameraRotationQuat.rotateZ(Mth.lerp(partialTick, this.oRoll, this.roll));
//        }
//        cameraRotationQuat.rotateAxis(Mth.PI, 0, 1, 0); // Account for camera orientation flip in 1.21.1
//
//        float offset = 1 - animationProgress;
//        Vector3f offsetVec = new Vector3f(-offset, -offset, 0.0F);
//        offsetVec.rotate(cameraRotationQuat);
//
//        Vec3 vec3 = camera.getPosition();
//        Vec2 frontOffset = (new Vec2((float) (vec3.x() - this.x), (float) (vec3.z() - this.z))).normalized();
//        float x = (float)(Mth.lerp(partialTick, this.xo, this.x) - vec3.x()) + offsetVec.x() + frontOffset.x;
//        float y = (float)(Mth.lerp(partialTick, this.yo, this.y) - vec3.y()) + offsetVec.y();
//        float z = (float)(Mth.lerp(partialTick, this.zo, this.z) - vec3.z()) + offsetVec.z() + frontOffset.y;
//
//        Vector3f[] vector3fs = new Vector3f[]{
//                new Vector3f(-1.0F, -1.0F, 0.0F),
//                new Vector3f(-1.0F, 1.0F, 0.0F),
//                new Vector3f(1.0F, 1.0F, 0.0F),
//                new Vector3f(1.0F, -1.0F, 0.0F)};
//
//        for(int k = 0; k < 4; ++k) {
//            Vector3f vector3f = vector3fs[k];
//            vector3f.rotate(cameraRotationQuat);
//            vector3f.mul(animationProgress);
//            vector3f.add(x, y, z);
//        }
//
//        float u0 = sprite.getU0();
//        float u1 = sprite.getU1();
//        float v0 = sprite.getV0();
//        float v1 = sprite.getV1();
//        int lightColor = 240;
//
//        MultiBufferSource.BufferSource bufferSource = this.renderBuffers.bufferSource();
//        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.text(TextureAtlas.LOCATION_PARTICLES));
//        vertexConsumer.addVertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setUv(u1, v1).setLight(lightColor);
//        vertexConsumer.addVertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setUv(u1, v0).setLight(lightColor);
//        vertexConsumer.addVertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z()).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setUv(u0, v0).setLight(lightColor);
//        vertexConsumer.addVertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z()).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setUv(u0, v1).setLight(lightColor);
//        bufferSource.endBatch();
//
//        Quaternionf reverseQuad = new Quaternionf(0, 0, 0, 1);
//        reverseQuad.rotateAxis(Mth.PI, 0 , 1, 0); // flip around y-axis because block items were facing backwards
//        Quaternionf normalToUse = new Quaternionf(-0.3F, 0F, 0F, 1F); // Controls the lighting on the items...
//
//        // Want item rendering
//        PoseStack wantPoseStack = new PoseStack();
//        setupPoseStack(wantPoseStack, x, y, z, 0.35F, 0.437F, -0.001F, animationProgress, cameraRotationQuat, reverseQuad, normalToUse);
//        ItemStack wantItemStack = this.tradeWantItem.getDefaultInstance();
//        renderItem(wantItemStack, wantPoseStack, bufferSource);
//
//        // Reward item rendering
//        PoseStack rewardPoseStack = new PoseStack();
//        setupPoseStack(rewardPoseStack, x, y, z, -0.32F, -0.15F, -0.001F, animationProgress, cameraRotationQuat, reverseQuad, normalToUse);
//        ItemStack rewardItemStack = this.tradeRewardItems.get((this.life / TRADE_REWARD_CYCLE_TIME) % this.tradeRewardItems.size());
//        renderItem(rewardItemStack, rewardPoseStack, bufferSource);
//
//        rewardPoseStack.popPose();
//        bufferSource.endBatch();
//    }
//
//    private static void setupPoseStack(PoseStack poseStack,
//                                       float x,
//                                       float y,
//                                       float z,
//                                       float xOffset,
//                                       float yOffset,
//                                       float zOffset,
//                                       float size,
//                                       Quaternionf quaternionf,
//                                       Quaternionf reverseQuad,
//                                       Quaternionf normalToUse)
//    {
//        poseStack.pushPose();
//        poseStack.last().pose().translate(x, y, z); // move to particle location
//        poseStack.last().pose().scale(size, size, size); // resize to match particle sprite size
//        poseStack.last().pose().rotateAround(quaternionf, 0,0,0); // face player
//        poseStack.last().pose().translate(xOffset, yOffset, zOffset); // move to correct spot on sprite
//        poseStack.last().pose().scale(0.5F, 0.5F, 0.001F); // scale to correct size on the sprite at location
//        poseStack.last().pose().rotateAround(reverseQuad, 0,0,0); // reverse around y-axis as block-entities were backwards
//        poseStack.last().normal().set(normalToUse); // set normal for lighting
//    }
//
//    private float getAnimationProgressForCurrentLife(float partialTick) {
//        float animationProgress = 1;
//        float animationChangeTime = 20F;
//
//        if (this.life <= animationChangeTime) {
//            float currentProgress = this.life / animationChangeTime;
//            float c1 = 1.70158F;
//            float c3 = c1 + 1;
//            float calcSize = (float) (1 + c3 * Math.pow(currentProgress - 1, 3) + c1 * Math.pow(currentProgress - 1, 2));
//            animationProgress = Mth.lerp(partialTick, this.pastAnimationProgress, calcSize);
//        }
//        else if (BeeQueenEntity.TRADE_HINT_PARTICLE_LIFETIME - this.life <= (animationChangeTime / 2)) {
//            float currentProgress = (BeeQueenEntity.TRADE_HINT_PARTICLE_LIFETIME - this.life) / (animationChangeTime / 2);
//            float calcSize = (float) (1 - Math.cos((currentProgress * Math.PI) / 2));
//            animationProgress = Mth.lerp(partialTick, this.pastAnimationProgress, calcSize);
//        }
//
//        return animationProgress;
//    }
//
//    private void renderItem(ItemStack itemStack, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource) {
//        this.itemStackRenderState.renderStatic(
//                itemStack,
//                ItemDisplayContext.GUI,
//                LightTexture.FULL_BRIGHT,
//                OverlayTexture.NO_OVERLAY,
//                poseStack,
//                bufferSource,
//                Minecraft.getInstance().level,
//                0);
//    }
//
//    @Override
//    public void tick() {
//        ++this.life;
//        if (this.life == BeeQueenEntity.TRADE_HINT_PARTICLE_LIFETIME) {
//            this.remove();
//        }
//    }
//
//    @Override
//    public ParticleRenderType getGroup() {
//        return ParticleRenderType.SINGLE_QUADS;
//    }
//}
