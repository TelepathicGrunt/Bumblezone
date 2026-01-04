package com.telepathicgrunt.the_bumblezone.client.rendering.rootmin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminState;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;

public class RootminGrassRenderer extends RenderLayer<RootminEntity, RootminModel> {
    private static final Identifier GRASS = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/rootmin_grass.png");
    private final RootminRenderer renderLayerParent;

    public RootminGrassRenderer(RootminRenderer renderLayerParent, EntityModelSet entityModelSet) {
        super(renderLayerParent);
        this.renderLayerParent = renderLayerParent;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, RootminEntity entity, float f, float g, float partialTicks, float j, float k, float l) {
        poseStack.pushPose();
        renderGrassBodyTop(entity, partialTicks, poseStack, multiBufferSource, packedLight);
        poseStack.popPose();
    }

    private void renderGrassBodyTop(RootminEntity rootminEntity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        Minecraft minecraft = Minecraft.getInstance();
        boolean bl = renderLayerParent.isBodyVisible(rootminEntity);
        boolean bl2 = !bl && !rootminEntity.isInvisibleTo(minecraft.player);
        boolean bl3 = minecraft.shouldEntityAppearGlowing(rootminEntity);
        RenderType renderType = this.getRenderTypeGrass(bl, bl2, bl3);
        if (renderType != null) {
            VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
            int p = LivingEntityRenderer.getOverlayCoords(rootminEntity, renderLayerParent.getWhiteOverlayProgress(rootminEntity, partialTicks));
            if (rootminEntity.hasCustomName() && "jeb_".equals(rootminEntity.getName().getString())) {
                int speed = 25;
                int offset = rootminEntity.tickCount / speed + rootminEntity.getId();
                int dyeColors = DyeColor.values().length;
                int firstDye = offset % dyeColors;
                int secondDye = (offset + 1) % dyeColors;
                float theColorThingy = ((float)(rootminEntity.tickCount % speed) + partialTicks) / 25.0f;
                int fs = Sheep.getColor(DyeColor.byId(firstDye));
                int gs = Sheep.getColor(DyeColor.byId(secondDye));
                int color = FastColor.ARGB32.lerp(theColorThingy, fs, gs);
                ((Model) this.renderLayerParent.getModel()).renderToBuffer(stack, vertexConsumer, packedLight, p, color);
            }
            else {
                int biomeColor = rootminEntity.level().getBlockTint(rootminEntity.blockPosition(), BiomeColors.GRASS_COLOR_RESOLVER);

                ((Model) this.renderLayerParent.getModel()).renderToBuffer(
                        stack,
                        vertexConsumer,
                        packedLight,
                        p,
                        FastColor.ARGB32.color(
                                bl2 ? 38 : 255,
                                GeneralUtils.getRed(biomeColor),
                                GeneralUtils.getGreen(biomeColor),
                                GeneralUtils.getBlue(biomeColor)
                        ));
            }
        }
    }

    protected RenderType getRenderTypeGrass(boolean bl, boolean bl2, boolean bl3) {
        if (bl2) {
            return RenderType.itemEntityTranslucentCull(GRASS);
        }
        if (bl) {
            return this.renderLayerParent.getModel().renderType(GRASS);
        }
        if (bl3) {
            return RenderType.outline(GRASS);
        }
        return null;
    }

    protected float xOffset(float f) {
        return f * 0.01F;
    }

    protected Identifier getTextureLocation() {
        return GRASS;
    }

    protected EntityModel<RootminEntity> model() {
        return this.renderLayerParent.getModel();
    }
}