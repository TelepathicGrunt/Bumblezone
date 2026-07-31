package com.telepathicgrunt.the_bumblezone.client.rendering.variantbee;

import net.minecraft.client.model.animal.bee.BeeModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class VariantBabyBeeModel extends BeeModel {
    public VariantBabyBeeModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition bone = root.addOrReplaceChild(
                "bone",
                CubeListBuilder.create()
                        .texOffs(6, 12)
                        .addBox(1.0F, -1.6667F, -2.1633F, 1.0F, 2.0F, 2.0F)
                        .texOffs(0, 12)
                        .addBox(-2.0F, -1.6667F, -2.1933F, 1.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 19.6667F, -1.8567F)
        );
        PartDefinition body = bone.addOrReplaceChild(
                "body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -2.5F, 4.0F, 4.0F, 5.0F), PartPose.offset(0.0F, 1.3333F, 2.3567F)
        );
        body.addOrReplaceChild(
                "stinger", CubeListBuilder.create().texOffs(13, 2).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 1.0F), PartPose.offset(0.0F, 0.5F, 2.5F)
        );
        bone.addOrReplaceChild(
                "right_wing",
                CubeListBuilder.create().texOffs(3, 9).addBox(-3.0F, 0.0F, 0.0F, 3.0F, 0.0F, 3.0F),
                PartPose.offsetAndRotation(-1.0F, -0.6667F, 0.8567F, 0.2182F, 0.3491F, 0.0F)
        );
        bone.addOrReplaceChild(
                "left_wing",
                CubeListBuilder.create().texOffs(-3, 9).mirror().addBox(0.0F, 0.0F, 0.0F, 3.0F, 0.0F, 3.0F).mirror(false),
                PartPose.offsetAndRotation(1.0F, -0.6667F, 0.8567F, 0.2182F, -0.3491F, 0.0F)
        );
        bone.addOrReplaceChild(
                "front_legs", CubeListBuilder.create().texOffs(13, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 0.0F), PartPose.offset(0.0F, 3.3333F, 1.8567F)
        );
        bone.addOrReplaceChild(
                "middle_legs", CubeListBuilder.create().texOffs(13, 1).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 0.0F), PartPose.offset(0.0F, 3.3333F, 2.8567F)
        );
        bone.addOrReplaceChild(
                "back_legs", CubeListBuilder.create().texOffs(13, 2).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 0.0F), PartPose.offset(0.0F, 3.3333F, 3.8567F)
        );
        return LayerDefinition.create(mesh, 32, 32);
    }
}
