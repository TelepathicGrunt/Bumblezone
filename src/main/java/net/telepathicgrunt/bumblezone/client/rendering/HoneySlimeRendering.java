package net.telepathicgrunt.bumblezone.client.rendering;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.SlimeOverlayFeatureRenderer;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.entities.mobs.HoneySlimeEntity;

@Environment(EnvType.CLIENT)
public class HoneySlimeRendering extends MobEntityRenderer<HoneySlimeEntity, SlimeEntityModel<HoneySlimeEntity>> {
    private static final Identifier HONEY_TEXTURE = new Identifier(Bumblezone.MODID, "textures/entity/honey_slime.png");
    private static final Identifier HONEYLESS_TEXTURE = new Identifier(Bumblezone.MODID, "textures/entity/honey_slime_naked.png");

    public HoneySlimeRendering(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new SlimeEntityModel<>(16), 0.25F);
        this.addFeature(new SlimeOverlayFeatureRenderer<>(this));
    }

    public void render(HoneySlimeEntity honeySlimeEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.shadowRadius = 0.25F * (float)(honeySlimeEntity.isBaby() ? 1 : 2);
        super.render(honeySlimeEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    // unused. Dont ask how the scaling even works automatically
    protected void scale(HoneySlimeEntity honeySlimeEntity, MatrixStack matrixStack, float f) {
        matrixStack.scale(0.999F, 0.999F, 0.999F);
        matrixStack.translate(0.0D, 0.0010000000474974513D, 0.0D);
        float h = (float)(honeySlimeEntity.isBaby() ? 1 : 2);
        float i = MathHelper.lerp(f, honeySlimeEntity.prevSquishFactor, honeySlimeEntity.squishFactor) / (h * 0.5F + 1.0F);
        float j = 1.0F / (i + 1.0F);
        matrixStack.scale(j * h, 1.0F / j * h, j * h);
    }

    public Identifier getTexture(HoneySlimeEntity honeySlimeEntity) {
        return honeySlimeEntity.isInHoney() ? HONEY_TEXTURE : HONEYLESS_TEXTURE;
    }
}

