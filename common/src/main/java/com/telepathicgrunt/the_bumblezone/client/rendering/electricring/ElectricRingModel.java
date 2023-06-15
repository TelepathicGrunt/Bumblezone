package com.telepathicgrunt.the_bumblezone.client.rendering.electricring;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.ElectricRingEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ElectricRingModel<T extends ElectricRingEntity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Bumblezone.MODID, "electric_ring"), "main");
    private final ModelPart bb_main;

    public ElectricRingModel(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-24.0F, -48.0F, 0.0F, 48.0F, 48.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(1, 52).addBox(-9.0F, -1.0F, -2.0F, 18.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(1, 52).addBox(-9.0F, -47.0F, -2.0F, 18.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition side_view_r1 = bb_main.addOrReplaceChild("side_view_r1", CubeListBuilder.create()
                .texOffs(1, 52).addBox(-33.0F, 23.0F, -2.0F, 18.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(1, 52).addBox(-33.0F, -22.0F, -2.0F, 18.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition side_view_r2 = bb_main.addOrReplaceChild("side_view_r2", CubeListBuilder.create()
                .texOffs(1, 52).addBox(8.0F, 5.0F, -2.0F, 18.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(1, 52).addBox(8.0F, -40.0F, -2.0F, 18.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition side_view_r3 = bb_main.addOrReplaceChild("side_view_r3", CubeListBuilder.create()
                .texOffs(1, 52).addBox(-26.0F, -39.0F, -2.0F, 18.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(1, 52).addBox(-25.0F, 5.0F, -2.0F, 18.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        return LayerDefinition.create(meshdefinition, 96, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}