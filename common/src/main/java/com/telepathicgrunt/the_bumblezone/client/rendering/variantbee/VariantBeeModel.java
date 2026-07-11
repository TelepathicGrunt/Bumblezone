package com.telepathicgrunt.the_bumblezone.client.rendering.variantbee;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class VariantBeeModel extends EntityModel<VariantBeeRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "variant_bee"), "main");

    protected final ModelPart bone;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart frontLeg;
    private final ModelPart midLeg;
    private final ModelPart backLeg;
    private final ModelPart stinger;
    private final ModelPart leftAntenna;
    private final ModelPart rightAntenna;

    public VariantBeeModel(ModelPart root) {
        super(root);
        this.bone = root.getChild("bone");
        ModelPart body = this.bone.getChild("body");
        this.stinger = body.getChild("stinger");
        this.rightWing = this.bone.getChild("right_wing");
        this.leftWing = this.bone.getChild("left_wing");
        this.frontLeg = this.bone.getChild("front_legs");
        this.midLeg = this.bone.getChild("middle_legs");
        this.backLeg = this.bone.getChild("back_legs");
        this.leftAntenna = body.getChild("left_antenna");
        this.rightAntenna = body.getChild("right_antenna");
    }

    @Override
    public void setupAnim(VariantBeeRenderState state) {
        super.setupAnim(state);
        this.stinger.visible = state.hasStinger;
        if (!state.isOnGround) {
            float speed = state.ageInTicks * 120.32113F * (float) (Math.PI / 180.0);
            this.rightWing.yRot = 0.0F;
            this.rightWing.zRot = Mth.cos(speed) * (float) Math.PI * 0.15F;
            this.leftWing.xRot = this.rightWing.xRot;
            this.leftWing.yRot = this.rightWing.yRot;
            this.leftWing.zRot = -this.rightWing.zRot;
            this.frontLeg.xRot = (float) (Math.PI / 4);
            this.midLeg.xRot = (float) (Math.PI / 4);
            this.backLeg.xRot = (float) (Math.PI / 4);
        }

        if (!state.isAngry && !state.isOnGround) {
            float speed = Mth.cos(state.ageInTicks * 0.18F);
            this.bobUpAndDown(speed, state.ageInTicks);
        }

        float rollAmount = state.rollAmount;
        if (rollAmount > 0.0F) {
            this.bone.xRot = Mth.rotLerpRad(rollAmount, this.bone.xRot, 3.0915928F);
        }
    }

    protected void bobUpAndDown(float speed, float ageInTicks) {
        this.bone.xRot = 0.1F + speed * (float) Math.PI * 0.025F;
        this.bone.y = this.bone.y - Mth.cos(ageInTicks * 0.18F) * 0.9F;
        this.frontLeg.xRot = -speed * (float) Math.PI * 0.1F + (float) (Math.PI / 8);
        this.backLeg.xRot = -speed * (float) Math.PI * 0.05F + (float) (Math.PI / 4);
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
                "left_antenna", CubeListBuilder.create().texOffs(2, 0).addBox(1.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F), PartPose.offset(0.0F, -2.0F, -5.0F)
        );
        body.addOrReplaceChild(
                "right_antenna", CubeListBuilder.create().texOffs(2, 3).addBox(-2.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F), PartPose.offset(0.0F, -2.0F, -5.0F)
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
