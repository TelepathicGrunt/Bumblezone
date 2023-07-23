package com.telepathicgrunt.the_bumblezone.client.rendering.purplespike;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.PurpleSpikeEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class PurpleSpikeModel<T extends PurpleSpikeEntity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Bumblezone.MODID, "purple_spike"), "main");
    private final ModelPart topSpike;
    private final ModelPart bottomSpike;

    public PurpleSpikeModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.topSpike = root.getChild("topSpike");
        this.bottomSpike = root.getChild("bottomSpike");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition topSpike = partdefinition.addOrReplaceChild("topSpike", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = topSpike.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-15.0F, 1.0F, -8.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -7.0F, -5.0F, 1.0472F, 0.0F, 0.0F));

        PartDefinition cube_r2 = topSpike.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, 1.0F, -15.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -13.0F, 0.0F, 0.0F, 1.5708F, -1.0472F));

        PartDefinition cube_r3 = topSpike.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, 1.0F, -1.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, -1.0F, 0.0F, 0.0F, -1.5708F, 1.0472F));

        PartDefinition cube_r4 = topSpike.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-15.0F, 1.0F, -8.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -6.0F, 3.0F, 2.0944F, 0.0F, 0.0F));

        PartDefinition bottomSpike = partdefinition.addOrReplaceChild("bottomSpike", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r5 = bottomSpike.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 16).addBox(-8.0F, 1.0F, -15.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -9.0F, 0.0F, 0.0F, 1.5708F, -1.0472F));

        PartDefinition cube_r6 = bottomSpike.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 16).addBox(-8.0F, 1.0F, -1.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 3.0F, 0.0F, 0.0F, -1.5708F, 1.0472F));

        PartDefinition cube_r7 = bottomSpike.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 16).addBox(-15.0F, 1.0F, -8.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -2.0F, 3.0F, 2.0944F, 0.0F, 0.0F));

        PartDefinition cube_r8 = bottomSpike.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 16).addBox(-15.0F, 1.0F, -8.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -3.0F, -5.0F, 1.0472F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(T spike, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        topSpike.visible = !spike.hasSpikeCharge() && spike.hasSpike();
        bottomSpike.visible = spike.hasSpikeCharge() || spike.hasSpike();
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        topSpike.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bottomSpike.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}