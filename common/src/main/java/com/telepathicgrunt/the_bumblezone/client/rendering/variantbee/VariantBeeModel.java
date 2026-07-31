package com.telepathicgrunt.the_bumblezone.client.rendering.variantbee;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.client.model.animal.bee.BeeModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.Identifier;

public class VariantBeeModel extends BeeModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "variant_bee"), "main");

    private static final String LEFT_ANTENNA = "left_antenna";
    private static final String RIGHT_ANTENNA = "right_antenna";
    private final ModelPart leftAntenna;
    private final ModelPart rightAntenna;

    public VariantBeeModel(ModelPart root) {
        super(root);
        ModelPart body = this.bone.getChild("body");
        this.leftAntenna = body.getChild(LEFT_ANTENNA);
        this.rightAntenna = body.getChild(RIGHT_ANTENNA);
    }

    @Override
    protected void bobUpAndDown(float speed, float ageInTicks) {
        super.bobUpAndDown(speed, ageInTicks);
        this.leftAntenna.xRot = speed * (float) Math.PI * 0.03F;
        this.rightAntenna.xRot = speed * (float) Math.PI * 0.03F;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition bone = root.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 19.0F, 0.0F));
        PartDefinition body = bone.addOrReplaceChild(
                "body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -5.0F, 7.0F, 7.0F, 10.0F), PartPose.ZERO
        );
        body.addOrReplaceChild("stinger", CubeListBuilder.create().texOffs(26, 7).addBox(0.0F, -1.0F, 5.0F, 0.0F, 1.0F, 2.0F), PartPose.ZERO);
        body.addOrReplaceChild(
                LEFT_ANTENNA, CubeListBuilder.create().texOffs(2, 0).addBox(1.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F), PartPose.offset(0.0F, -2.0F, -5.0F)
        );
        body.addOrReplaceChild(
                RIGHT_ANTENNA, CubeListBuilder.create().texOffs(2, 3).addBox(-2.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F), PartPose.offset(0.0F, -2.0F, -5.0F)
        );
        CubeDeformation wingDeformation = new CubeDeformation(0.001F);
        bone.addOrReplaceChild(
                "right_wing",
                CubeListBuilder.create().texOffs(0, 18).addBox(-9.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, wingDeformation),
                PartPose.offsetAndRotation(-1.5F, -4.0F, -3.0F, 0.0F, -0.2618F, 0.0F)
        );
        bone.addOrReplaceChild(
                "left_wing",
                CubeListBuilder.create().texOffs(0, 18).mirror().addBox(0.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, wingDeformation),
                PartPose.offsetAndRotation(1.5F, -4.0F, -3.0F, 0.0F, 0.2618F, 0.0F)
        );
        bone.addOrReplaceChild(
                "front_legs", CubeListBuilder.create().addBox("front_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 1), PartPose.offset(1.5F, 3.0F, -2.0F)
        );
        bone.addOrReplaceChild(
                "middle_legs", CubeListBuilder.create().addBox("middle_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 3), PartPose.offset(1.5F, 3.0F, 0.0F)
        );
        bone.addOrReplaceChild("back_legs", CubeListBuilder.create().addBox("back_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 5), PartPose.offset(1.5F, 3.0F, 2.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }
}
