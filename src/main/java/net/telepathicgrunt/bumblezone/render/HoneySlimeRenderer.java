package net.telepathicgrunt.bumblezone.render;

import com.bagel.buzzierbees.common.entities.HoneySlimeEntity;
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

@OnlyIn(Dist.CLIENT)
public class HoneySlimeRenderer extends MobRenderer<HoneySlimeEntity, SlimeModel<HoneySlimeEntity>> {
   private static final ResourceLocation HONEY_SLIME_TEXTURES = new ResourceLocation("buzzierbees:textures/entity/honey_slime.png");
   private static final ResourceLocation DESTICKED_HONEY_SLIME_TEXTURES = new ResourceLocation("buzzierbees:textures/entity/honey_slime_bottled.png");

   public HoneySlimeRenderer(EntityRendererManager renderManagerIn) {
      super(renderManagerIn, new SlimeModel<>(16), 0.25F);
      this.addLayer(new SlimeGelLayer<>(this));
   }

   public void render(HoneySlimeEntity honeySlimeEntity, float f1, float f2, MatrixStack matrixStack, IRenderTypeBuffer RenderTypeBuffer, int i) {
      this.shadowSize = 0.25F * (float)(honeySlimeEntity.isChild() ? 1 : 2);
      super.render(honeySlimeEntity, f1, f2, matrixStack, RenderTypeBuffer, i);
   }

   @SuppressWarnings("unused")
   protected void preRenderCallback(HoneySlimeEntity honeySlimeEntity, MatrixStack matrixStack, float f) {
      float f0 = 0.999F;
      matrixStack.scale(0.999F, 0.999F, 0.999F);
      matrixStack.translate(0.0D, (double)0.001F, 0.0D);
      float f1 = (float)(honeySlimeEntity.isChild() ? 1 : 2);
      float f2 = MathHelper.lerp(f, honeySlimeEntity.prevSquishFactor, honeySlimeEntity.squishFactor) / (f1 * 0.5F + 1.0F);
      float f3 = 1.0F / (f2 + 1.0F);
      matrixStack.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
   }

   public ResourceLocation getEntityTexture(HoneySlimeEntity entity) {
      return entity.isInHoney() ? HONEY_SLIME_TEXTURES : DESTICKED_HONEY_SLIME_TEXTURES;
   }
}