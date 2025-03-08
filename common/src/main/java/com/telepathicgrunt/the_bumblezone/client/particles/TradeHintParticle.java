package com.telepathicgrunt.the_bumblezone.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.mixin.ParticleEngineAccessor;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class TradeHintParticle extends Particle {
    private final RenderBuffers renderBuffers;
    private final Item tradeWantItem;
    private final List<Item> tradeRewardItems;
    private final Entity queen;
    private final Entity viewer;
    private int life;
    private final EntityRenderDispatcher entityRenderDispatcher;
    protected TextureAtlasSprite sprite;

    public TradeHintParticle(EntityRenderDispatcher arg, RenderBuffers arg2, ClientLevel arg3, Entity queen, Entity viewer, Item tradeWantItem, List<Item> tradeRewardItems) {
        super(arg3, queen.getX(), queen.getY() + 4, queen.getZ(), 0, 0, 0);
        this.renderBuffers = arg2;
        this.tradeWantItem = tradeWantItem;
        this.tradeRewardItems = tradeRewardItems;
        this.queen = queen;
        this.viewer = viewer;
        this.entityRenderDispatcher = arg;
        this.sprite = ((ParticleEngineAccessor)Minecraft.getInstance().particleEngine).getTextureAtlas().getSprite(new ResourceLocation(Bumblezone.MODID, "particle/trade_hint/1"));
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    public void render(VertexConsumer doNotUse, Camera arg2, float f) {
        Vec3 vec3 = arg2.getPosition();
        float x = (float)(Mth.lerp(f, this.xo, this.x) - vec3.x());
        float y = (float)(Mth.lerp(f, this.yo, this.y) - vec3.y());
        float z = (float)(Mth.lerp(f, this.zo, this.z) - vec3.z());

        Quaternionf quaternionf;
        if (this.roll == 0.0F) {
            quaternionf = arg2.rotation();
        }
        else {
            quaternionf = new Quaternionf(arg2.rotation());
            quaternionf.rotateZ(Mth.lerp(f, this.oRoll, this.roll));
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
        int lightColor = this.getLightColor(f);

        MultiBufferSource.BufferSource bufferSource = this.renderBuffers.bufferSource();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.gui());
        vertexConsumer.vertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).uv(u1, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(lightColor).endVertex();
        vertexConsumer.vertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).uv(u1, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(lightColor).endVertex();
        vertexConsumer.vertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z()).uv(u0, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(lightColor).endVertex();
        vertexConsumer.vertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z()).uv(u0, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(lightColor).endVertex();
        bufferSource.endBatch();
    }

    public void tick() {
        ++this.life;
        if (this.life == 200) {
            this.remove();
        }
    }
}
