package com.telepathicgrunt.the_bumblezone.client.particles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import com.telepathicgrunt.the_bumblezone.mixin.ParticleEngineAccessor;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class TradeHintParticle extends Particle {
    public final static int TRADE_REWARD_CYCLE_TIME = 40;
    public final static double PARTICLE_Y_OFFSET = 4.5D;

    private final RenderBuffers renderBuffers;
    private final Item tradeWantItem;
    private final List<Item> tradeRewardItems;
    private int life;
    private final ItemRenderer itemRenderer;
    protected TextureAtlasSprite sprite;

    public TradeHintParticle(ItemRenderer itemRenderer, RenderBuffers arg2, ClientLevel arg3, Entity queen, Item tradeWantItem, List<Item> tradeRewardItems) {
        super(arg3, queen.getX(), queen.getY() + PARTICLE_Y_OFFSET, queen.getZ(), 0, 0, 0);
        this.renderBuffers = arg2;
        this.tradeWantItem = tradeWantItem;
        this.tradeRewardItems = tradeRewardItems;
        this.itemRenderer = itemRenderer;
        this.sprite = ((ParticleEngineAccessor)Minecraft.getInstance().particleEngine).getTextureAtlas().getSprite(new ResourceLocation(Bumblezone.MODID, "trade_hint/1"));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @Override
    public void render(VertexConsumer doNotUse, Camera camera, float partialTick) {
        Vec3 vec3 = camera.getPosition();
        float x = (float)(Mth.lerp(partialTick, this.xo, this.x) - vec3.x());
        float y = (float)(Mth.lerp(partialTick, this.yo, this.y) - vec3.y());
        float z = (float)(Mth.lerp(partialTick, this.zo, this.z) - vec3.z());

        Quaternionf quaternionf;
        if (this.roll == 0.0F) {
            quaternionf = camera.rotation();
        }
        else {
            quaternionf = new Quaternionf(camera.rotation());
            quaternionf.rotateZ(Mth.lerp(partialTick, this.oRoll, this.roll));
        }

        Vector3f[] vector3fs = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, -1.0F, 0.0F)};

        for(int k = 0; k < 4; ++k) {
            Vector3f vector3f = vector3fs[k];
            vector3f.rotate(quaternionf);
            vector3f.mul(1);
            vector3f.add(x, y, z);
        }

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();
        int lightColor = 240;

        MultiBufferSource.BufferSource bufferSource = this.renderBuffers.bufferSource();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.text(TextureAtlas.LOCATION_PARTICLES));
        vertexConsumer.vertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).color(this.rCol, this.gCol, this.bCol, this.alpha).uv(u1, v1).uv2(lightColor).endVertex();
        vertexConsumer.vertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).color(this.rCol, this.gCol, this.bCol, this.alpha).uv(u1, v0).uv2(lightColor).endVertex();
        vertexConsumer.vertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z()).color(this.rCol, this.gCol, this.bCol, this.alpha).uv(u0, v0).uv2(lightColor).endVertex();
        vertexConsumer.vertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z()).color(this.rCol, this.gCol, this.bCol, this.alpha).uv(u0, v1).uv2(lightColor).endVertex();
        bufferSource.endBatch();

        Quaternionf reverseQuad = new Quaternionf(0, 0, 0, 1);
        reverseQuad.rotateAxis(Mth.PI, 0 , 1, 0); // flip around y-axis because block items were facing backwards
        Quaternionf normalToUse = new Quaternionf(-0.6F, 0F, 0F, 1); // Controls the lighting on the items
        //normalToUse.mul(camera.rotation().mul(-1));

        // Want item rendering
        PoseStack wantPoseStack = new PoseStack();
        wantPoseStack.pushPose();
        wantPoseStack.last().pose().translate(x, y, z);
        wantPoseStack.last().pose().scale(0.5F, 0.5F, 0.5F);
        wantPoseStack.last().pose().rotateAround(quaternionf, 0,0,0);
        wantPoseStack.last().pose().translate(0.68F, 0.85F, -0.03F);
        wantPoseStack.last().pose().scale(1F, 1F, 0.01F);
        wantPoseStack.last().pose().rotateAround(reverseQuad, 0,0,0);
        wantPoseStack.last().normal().set(normalToUse);
        ItemStack wantItemStack = this.tradeWantItem.getDefaultInstance();
        renderItem(wantItemStack, wantPoseStack, bufferSource);

        // Reward item rendering
        PoseStack rewardPoseStack = new PoseStack();
        rewardPoseStack.pushPose();
        rewardPoseStack.last().pose().translate(x, y, z);
        rewardPoseStack.last().pose().scale(0.5F, 0.5F, 0.5F);
        rewardPoseStack.last().pose().rotateAround(quaternionf, 0,0,0);
        rewardPoseStack.last().pose().translate(-0.68F, -0.3F, -0.03F);
        rewardPoseStack.last().pose().scale(1F, 1F, 0.01F);
        rewardPoseStack.last().pose().rotateAround(reverseQuad, 0,0,0);
        rewardPoseStack.last().normal().set(normalToUse);
        ItemStack rewardItemStack = this.tradeRewardItems.get((this.life / TRADE_REWARD_CYCLE_TIME) % this.tradeRewardItems.size()).getDefaultInstance();
        renderItem(rewardItemStack, rewardPoseStack, bufferSource);

        rewardPoseStack.popPose();
        bufferSource.endBatch();
    }

    private void renderItem(ItemStack itemStack, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource) {
        BakedModel bakedmodel = itemRenderer.getModel(itemStack, Minecraft.getInstance().level, null,
                Item.getId(itemStack.getItem()) + itemStack.getDamageValue());
        this.itemRenderer.render(
                itemStack,
                ItemDisplayContext.GUI,
                false,
                poseStack,
                bufferSource,
                LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY,
                bakedmodel);
    }

    @Override
    public void tick() {
        ++this.life;
        if (this.life == BeeQueenEntity.TRADE_HINT_PARTICLE_LIFETIME) {
            this.remove();
        }
    }
}
