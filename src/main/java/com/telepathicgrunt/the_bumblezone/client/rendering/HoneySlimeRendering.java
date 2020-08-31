package com.telepathicgrunt.the_bumblezone.client.rendering;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SlimeGelLayer;
import net.minecraft.client.renderer.entity.model.SlimeModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;

@OnlyIn(Dist.CLIENT)
public class HoneySlimeRendering extends MobRenderer<HoneySlimeEntity, SlimeModel<HoneySlimeEntity>> {
    private static final ResourceLocation HONEY_TEXTURE = new ResourceLocation(Bumblezone.MODID, "textures/entity/honey_slime.png");
    private static final ResourceLocation HONEYLESS_TEXTURE = new ResourceLocation(Bumblezone.MODID, "textures/entity/honey_slime_naked.png");

    public HoneySlimeRendering(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new SlimeModel<>(16), 0.25F);
        this.addLayer(new SlimeGelLayer<>(this));
    }

    public void render(HoneySlimeEntity honeySlimeEntity, float f, float g, MatrixStack matrixStack, IRenderTypeBuffer vertexConsumerProvider, int i) {
        this.shadowSize = 0.25F * (float)(honeySlimeEntity.isChild() ? 1 : 2);
        super.render(honeySlimeEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    protected void scale(HoneySlimeEntity honeySlimeEntity, MatrixStack matrixStack, float f) {
        matrixStack.scale(0.999F, 0.999F, 0.999F);
        matrixStack.translate(0.0D, 0.0010000000474974513D, 0.0D);
        float h = (float)(honeySlimeEntity.isChild() ? 1 : 2);
        float i = MathHelper.lerp(f, honeySlimeEntity.prevSquishFactor, honeySlimeEntity.squishFactor) / (h * 0.5F + 1.0F);
        float j = 1.0F / (i + 1.0F);
        matrixStack.scale(j * h, 1.0F / j * h, j * h);
    }

    public ResourceLocation getEntityTexture(HoneySlimeEntity honeySlimeEntity) {
        return honeySlimeEntity.isInHoney() ? HONEY_TEXTURE : HONEYLESS_TEXTURE;
    }
}

