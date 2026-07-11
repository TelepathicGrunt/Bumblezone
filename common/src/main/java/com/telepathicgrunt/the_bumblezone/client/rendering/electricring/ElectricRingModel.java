package com.telepathicgrunt.the_bumblezone.client.rendering.electricring;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public class ElectricRingModel extends EntityModel<ElectricRingRenderState> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "electric_ring"), "main");

    public ElectricRingModel(ModelPart root) {
        super(root, RenderTypes::entityTranslucentEmissive);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition ring = partdefinition.addOrReplaceChild("ring", CubeListBuilder.create().texOffs(0, 0).addBox(-32.0F, -56.0F, 0.0F, 64.0F, 64.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition side = partdefinition.addOrReplaceChild("side", CubeListBuilder.create().texOffs(1, 52).addBox(-9.0F, -1.0F, -2.0F, 18.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(1, 52).addBox(-9.0F, -47.0F, -2.0F, 18.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition side_view_r1 = side.addOrReplaceChild("side_view_r1", CubeListBuilder.create().texOffs(1, 52).addBox(-26.0F, -39.0F, -2.0F, 18.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(1, 52).addBox(-25.0F, 5.0F, -2.0F, 18.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition side_view_r2 = side.addOrReplaceChild("side_view_r2", CubeListBuilder.create().texOffs(1, 52).addBox(8.0F, 5.0F, -2.0F, 18.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(1, 52).addBox(8.0F, -40.0F, -2.0F, 18.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition side_view_r3 = side.addOrReplaceChild("side_view_r3", CubeListBuilder.create().texOffs(1, 52).addBox(-33.0F, 23.0F, -2.0F, 18.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(1, 52).addBox(-33.0F, -22.0F, -2.0F, 18.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}