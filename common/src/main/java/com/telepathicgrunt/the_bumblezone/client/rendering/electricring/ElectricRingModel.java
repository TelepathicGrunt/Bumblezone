package com.telepathicgrunt.the_bumblezone.client.rendering.electricring;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.ElectricRingEntity;
import net.minecraft.Util;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.function.BiFunction;
import java.util.function.Function;

import static net.minecraft.client.renderer.RenderStateShard.*;

public class ElectricRingModel<T extends ElectricRingEntity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Bumblezone.MODID, "electric_ring"), "main");
    private final ModelPart ring;
    private final ModelPart side;

    private static final Function<ResourceLocation, RenderType> RING = Util.memoize((resourceLocation) -> {
        RenderType.CompositeState compositeState = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false))
                .setTransparencyState(NO_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setOverlayState(NO_OVERLAY)
                .createCompositeState(false);

        return RenderType.create("the_bumblezone:ring",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                false,
                true,
                compositeState);
    });

    public ElectricRingModel(ModelPart root) {
        super(RING);
        this.ring = root.getChild("ring");
        this.side = root.getChild("side");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition ring = partdefinition.addOrReplaceChild("ring", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-24.0F, -48.0F, 0.0F, 48.0F, 48.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition side = partdefinition.addOrReplaceChild("side", CubeListBuilder.create()
                .texOffs(1, 52).addBox(-9.0F, -1.0F, -2.0F, 18.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(1, 52).addBox(-9.0F, -47.0F, -2.0F, 18.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition side_view_r1 = side.addOrReplaceChild("side_view_r1", CubeListBuilder.create()
                .texOffs(1, 52).addBox(-26.0F, -39.0F, -2.0F, 18.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(1, 52).addBox(-25.0F, 5.0F, -2.0F, 18.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition side_view_r2 = side.addOrReplaceChild("side_view_r2", CubeListBuilder.create()
                .texOffs(1, 52).addBox(8.0F, 5.0F, -2.0F, 18.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(1, 52).addBox(8.0F, -40.0F, -2.0F, 18.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition side_view_r3 = side.addOrReplaceChild("side_view_r3", CubeListBuilder.create()
                .texOffs(1, 52).addBox(-33.0F, 23.0F, -2.0F, 18.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(1, 52).addBox(-33.0F, -22.0F, -2.0F, 18.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        return LayerDefinition.create(meshdefinition, 96, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        ring.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        side.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}