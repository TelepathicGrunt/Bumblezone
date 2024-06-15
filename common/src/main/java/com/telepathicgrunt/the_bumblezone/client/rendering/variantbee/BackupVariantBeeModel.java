package com.telepathicgrunt.the_bumblezone.client.rendering.variantbee;

import com.google.common.collect.ImmutableList;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ModelUtils;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Bee;

public class BackupVariantBeeModel<T extends Bee> extends AgeableListModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "variant_bee"), "main");

    private static final String BONE = "bone";
    private static final String STINGER = "stinger";
    private static final String LEFT_ANTENNA = "left_antenna";
    private static final String RIGHT_ANTENNA = "right_antenna";
    private static final String FRONT_LEGS = "front_legs";
    private static final String MIDDLE_LEGS = "middle_legs";
    private static final String BACK_LEGS = "back_legs";
    private final ModelPart bone;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart frontLeg;
    private final ModelPart midLeg;
    private final ModelPart backLeg;
    private final ModelPart stinger;
    private final ModelPart leftAntenna;
    private final ModelPart rightAntenna;
    private float rollAmount;

    public BackupVariantBeeModel(ModelPart modelPart) {
        super(false, 24.0f, 0.0f);
        this.bone = modelPart.getChild(BONE);
        ModelPart modelPart2 = this.bone.getChild("body");
        this.stinger = modelPart2.getChild(STINGER);
        this.leftAntenna = modelPart2.getChild(LEFT_ANTENNA);
        this.rightAntenna = modelPart2.getChild(RIGHT_ANTENNA);
        this.rightWing = this.bone.getChild("right_wing");
        this.leftWing = this.bone.getChild("left_wing");
        this.frontLeg = this.bone.getChild(FRONT_LEGS);
        this.midLeg = this.bone.getChild(MIDDLE_LEGS);
        this.backLeg = this.bone.getChild(BACK_LEGS);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        PartDefinition partDefinition2 = partDefinition.addOrReplaceChild(BONE, CubeListBuilder.create(), PartPose.offset(0.0f, 19.0f, 0.0f));
        PartDefinition partDefinition3 = partDefinition2.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5f, -4.0f, -5.0f, 7.0f, 7.0f, 10.0f), PartPose.ZERO);
        partDefinition3.addOrReplaceChild(STINGER, CubeListBuilder.create().texOffs(26, 7).addBox(0.0f, -1.0f, 5.0f, 0.0f, 1.0f, 2.0f), PartPose.ZERO);
        partDefinition3.addOrReplaceChild(LEFT_ANTENNA, CubeListBuilder.create().texOffs(2, 0).addBox(1.5f, -2.0f, -3.0f, 1.0f, 2.0f, 3.0f), PartPose.offset(0.0f, -2.0f, -5.0f));
        partDefinition3.addOrReplaceChild(RIGHT_ANTENNA, CubeListBuilder.create().texOffs(2, 3).addBox(-2.5f, -2.0f, -3.0f, 1.0f, 2.0f, 3.0f), PartPose.offset(0.0f, -2.0f, -5.0f));
        CubeDeformation cubeDeformation = new CubeDeformation(0.001f);
        partDefinition2.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 18).addBox(-9.0f, 0.0f, 0.0f, 9.0f, 0.0f, 6.0f, cubeDeformation), PartPose.offsetAndRotation(-1.5f, -4.0f, -3.0f, 0.0f, -0.2618f, 0.0f));
        partDefinition2.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 18).mirror().addBox(0.0f, 0.0f, 0.0f, 9.0f, 0.0f, 6.0f, cubeDeformation), PartPose.offsetAndRotation(1.5f, -4.0f, -3.0f, 0.0f, 0.2618f, 0.0f));
        partDefinition2.addOrReplaceChild(FRONT_LEGS, CubeListBuilder.create().addBox(FRONT_LEGS, -5.0f, 0.0f, 0.0f, 7, 2, 0, 26, 1), PartPose.offset(1.5f, 3.0f, -2.0f));
        partDefinition2.addOrReplaceChild(MIDDLE_LEGS, CubeListBuilder.create().addBox(MIDDLE_LEGS, -5.0f, 0.0f, 0.0f, 7, 2, 0, 26, 3), PartPose.offset(1.5f, 3.0f, 0.0f));
        partDefinition2.addOrReplaceChild(BACK_LEGS, CubeListBuilder.create().addBox(BACK_LEGS, -5.0f, 0.0f, 0.0f, 7, 2, 0, 26, 5), PartPose.offset(1.5f, 3.0f, 2.0f));
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void prepareMobModel(T bee, float f, float g, float h) {
        super.prepareMobModel(bee, f, g, h);
        this.rollAmount = bee.getRollAmount(h);
        this.stinger.visible = !bee.hasStung();
    }

    @Override
    public void setupAnim(T bee, float f, float g, float h, float i, float j) {
        float k;
        boolean onGroundAndStill = bee.onGround() && bee.getDeltaMovement().lengthSqr() < 1.0E-7;
        this.rightWing.xRot = 0.0f;
        this.leftAntenna.xRot = 0.0f;
        this.rightAntenna.xRot = 0.0f;
        this.bone.xRot = 0.0f;
        if (onGroundAndStill) {
            this.rightWing.yRot = -0.2618f;
            this.rightWing.zRot = 0.0f;
            this.leftWing.xRot = 0.0f;
            this.leftWing.yRot = 0.2618f;
            this.leftWing.zRot = 0.0f;
            this.frontLeg.xRot = 0.0f;
            this.midLeg.xRot = 0.0f;
            this.backLeg.xRot = 0.0f;
        }
        else {
            k = h * 120.32113f * ((float)Math.PI / 180);
            this.rightWing.yRot = 0.0f;
            this.rightWing.zRot = Mth.cos(k) * (float)Math.PI * 0.15f;
            this.leftWing.xRot = this.rightWing.xRot;
            this.leftWing.yRot = this.rightWing.yRot;
            this.leftWing.zRot = -this.rightWing.zRot;
            this.frontLeg.xRot = 0.7853982f;
            this.midLeg.xRot = 0.7853982f;
            this.backLeg.xRot = 0.7853982f;
            this.bone.xRot = 0.0f;
            this.bone.yRot = 0.0f;
            this.bone.zRot = 0.0f;
        }

        if (!bee.isAngry()) {
            this.bone.xRot = 0.0f;
            this.bone.yRot = 0.0f;
            this.bone.zRot = 0.0f;
            if (!onGroundAndStill) {
                k = Mth.cos(h * 0.18f);
                this.bone.xRot = 0.1f + k * (float)Math.PI * 0.025f;
                this.leftAntenna.xRot = k * (float)Math.PI * 0.03f;
                this.rightAntenna.xRot = k * (float)Math.PI * 0.03f;
                this.frontLeg.xRot = -k * (float)Math.PI * 0.1f + 0.3926991f;
                this.backLeg.xRot = -k * (float)Math.PI * 0.05f + 0.7853982f;
                this.bone.y = 19.0f - Mth.cos(h * 0.18f) * 0.9f;
            }
        }

        if (this.rollAmount > 0.0f) {
            this.bone.xRot = ModelUtils.rotlerpRad(this.bone.xRot, 3.0915928f, this.rollAmount);
        }
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.bone);
    }
}
