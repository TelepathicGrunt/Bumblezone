package com.telepathicgrunt.bumblezone.client.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.entities.mobs.BeehemothEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import net.minecraft.util.Mth;

// Made with Blockbench 4.0.0-beta.5
// Exported for Minecraft version 1.17 with Mojang mappings

@Environment(EnvType.CLIENT)
public class BeehemothModel extends EntityModel<BeehemothEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Bumblezone.MODID, "beehemoth"), "main");
    private final ModelPart ROOT;
    protected final ModelPart SADDLE;
    protected final ModelPart CROWN;
    protected final ModelPart WING_RIGHT;
    protected final ModelPart WING_LEFT;
    protected final ModelPart LEG_FRONTLEFT;
    protected final ModelPart LEG_FRONTRIGHT;
    protected final ModelPart LEG_MIDLEFT;
    protected final ModelPart LEG_MIDRIGHT;
    protected final ModelPart LEG_REARLEFT;
    protected final ModelPart LEG_REARRIGHT;
    protected final ModelPart KneeFrontRightCube_r1;
    protected final ModelPart KneeMidRightCube_r1;
    protected final ModelPart KneeRearRightCube_r1;
    protected final ModelPart KneeFrontLeftCube_r1;
    protected final ModelPart KneeMidLeftCube_r1;
    protected final ModelPart KneeRearLeftCube_r1;
    protected final ModelPart ANTENNA_LEFT;
    protected final ModelPart ANTENNA_RIGHT;
    protected final ModelPart THORAX;
    protected final ModelPart FACE;
    protected final ModelPart ABDOMEN;

    public BeehemothModel(ModelPart modelPart) {
        this.ROOT = modelPart.getChild("ROOT");
        this.FACE = this.ROOT.getChild("FACE");
        this.CROWN = this.FACE.getChild("CROWN");
        this.ANTENNA_LEFT = this.FACE.getChild("ANTENNA_LEFT");
        this.ANTENNA_RIGHT = this.FACE.getChild("ANTENNA_RIGHT");
        this.THORAX = this.ROOT.getChild("THORAX");
        this.LEG_FRONTLEFT = this.THORAX.getChild("LEG_FRONTLEFT");
        this.KneeFrontLeftCube_r1 = this.LEG_FRONTLEFT.getChild("KNEE_FRONTLEFT").getChild("KneeFrontLeftCube_r1");
        this.LEG_MIDLEFT = this.THORAX.getChild("LEG_MIDLEFT");
        this.KneeMidLeftCube_r1 = this.LEG_MIDLEFT.getChild("KNEE_MIDLEFT").getChild("KneeMidLeftCube_r1");
        this.LEG_REARLEFT = this.THORAX.getChild("LEG_REARLEFT");
        this.KneeRearLeftCube_r1 = this.LEG_REARLEFT.getChild("KNEE_REARLEFT").getChild("KneeRearLeftCube_r1");
        this.LEG_FRONTRIGHT = this.THORAX.getChild("LEG_FRONTRIGHT");
        this.KneeFrontRightCube_r1 = this.LEG_FRONTRIGHT.getChild("KNEE_FRONTRIGHT").getChild("KneeFrontRightCube_r1");
        this.LEG_MIDRIGHT = this.THORAX.getChild("LEG_MIDRIGHT");
        this.KneeMidRightCube_r1 = this.LEG_MIDRIGHT.getChild("KNEE_MIDRIGHT").getChild("KneeMidRightCube_r1");
        this.LEG_REARRIGHT = this.THORAX.getChild("LEG_REARRIGHT");
        this.KneeRearRightCube_r1 = this.LEG_REARRIGHT.getChild("KNEE_REARRIGHT").getChild("KneeRearRightCube_r1");
        this.WING_LEFT = this.THORAX.getChild("WING_LEFT");
        this.WING_RIGHT = this.THORAX.getChild("WING_RIGHT");
        this.ABDOMEN = this.ROOT.getChild("ABDOMEN");
        this.SADDLE = this.ROOT.getChild("SADDLE");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition ROOT = partdefinition.addOrReplaceChild("ROOT", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition FACE = ROOT.addOrReplaceChild("FACE", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, 0.0F, -6.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, -6.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition CROWN = FACE.addOrReplaceChild("CROWN", CubeListBuilder.create().texOffs(40, 26).addBox(-2.5F, -3.0F, -5.0F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition ANTENNA_LEFT = FACE.addOrReplaceChild("ANTENNA_LEFT", CubeListBuilder.create().texOffs(57, 1).addBox(-1.5F, 0.0F, -9.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition ANTENNA_RIGHT = FACE.addOrReplaceChild("ANTENNA_RIGHT", CubeListBuilder.create().texOffs(57, 1).addBox(1.5F, 0.0F, -9.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition THORAX = ROOT.addOrReplaceChild("THORAX", CubeListBuilder.create().texOffs(0, 14).addBox(-4.5F, -9.0F, -6.0F, 9.0F, 9.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition LEG_FRONTLEFT = THORAX.addOrReplaceChild("LEG_FRONTLEFT", CubeListBuilder.create().texOffs(21, 0).addBox(-1.0F, 0.0F, -1.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, -2.0F, -4.0F, 0.0F, 0.0873F, 1.309F));

        PartDefinition KNEE_FRONTLEFT = LEG_FRONTLEFT.addOrReplaceChild("KNEE_FRONTLEFT", CubeListBuilder.create(), PartPose.offset(4.0F, 1.5F, 4.5F));

        PartDefinition KneeFrontLeftCube_r1 = KNEE_FRONTLEFT.addOrReplaceChild("KneeFrontLeftCube_r1", CubeListBuilder.create().texOffs(37, 2).addBox(0.0F, -1.0F, 0.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.0F, -0.6981F, 0.0F));

        PartDefinition LEG_MIDLEFT = THORAX.addOrReplaceChild("LEG_MIDLEFT", CubeListBuilder.create().texOffs(21, 0).addBox(-1.0F, 0.0F, -1.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, -2.0F, -0.5F, 0.0F, 0.0F, 1.1345F));

        PartDefinition KNEE_MIDLEFT = LEG_MIDLEFT.addOrReplaceChild("KNEE_MIDLEFT", CubeListBuilder.create(), PartPose.offset(4.0F, 1.5F, 4.5F));

        PartDefinition KneeMidLeftCube_r1 = KNEE_MIDLEFT.addOrReplaceChild("KneeMidLeftCube_r1", CubeListBuilder.create().texOffs(37, 2).addBox(0.0F, -1.0F, 0.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.0F, -0.8727F, 0.0F));

        PartDefinition LEG_REARLEFT = THORAX.addOrReplaceChild("LEG_REARLEFT", CubeListBuilder.create().texOffs(21, 0).addBox(-1.0F, 0.0F, -1.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, -2.0F, 3.0F, 0.0F, -0.2182F, 0.9599F));

        PartDefinition KNEE_REARLEFT = LEG_REARLEFT.addOrReplaceChild("KNEE_REARLEFT", CubeListBuilder.create(), PartPose.offset(4.0F, 1.5F, 4.5F));

        PartDefinition KneeRearLeftCube_r1 = KNEE_REARLEFT.addOrReplaceChild("KneeRearLeftCube_r1", CubeListBuilder.create().texOffs(37, 2).addBox(0.0F, -1.0F, 0.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.0F, -1.0472F, 0.0F));

        PartDefinition LEG_FRONTRIGHT = THORAX.addOrReplaceChild("LEG_FRONTRIGHT", CubeListBuilder.create().texOffs(21, 0).mirror().addBox(-4.0F, 0.0F, -1.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.5F, -2.0F, -4.0F, 0.0F, -0.0873F, -1.309F));

        PartDefinition KNEE_FRONTRIGHT = LEG_FRONTRIGHT.addOrReplaceChild("KNEE_FRONTRIGHT", CubeListBuilder.create(), PartPose.offset(-4.0F, 1.5F, 4.5F));

        PartDefinition KneeFrontRightCube_r1 = KNEE_FRONTRIGHT.addOrReplaceChild("KneeFrontRightCube_r1", CubeListBuilder.create().texOffs(37, 2).mirror().addBox(-5.0F, -1.0F, 0.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.0F, 0.6981F, 0.0F));

        PartDefinition LEG_MIDRIGHT = THORAX.addOrReplaceChild("LEG_MIDRIGHT", CubeListBuilder.create().texOffs(21, 0).mirror().addBox(-4.0F, 0.0F, -1.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.5F, -2.0F, -0.5F, 0.0F, 0.0F, -1.1345F));

        PartDefinition KNEE_MIDRIGHT = LEG_MIDRIGHT.addOrReplaceChild("KNEE_MIDRIGHT", CubeListBuilder.create(), PartPose.offset(-4.0F, 1.5F, 4.5F));

        PartDefinition KneeMidRightCube_r1 = KNEE_MIDRIGHT.addOrReplaceChild("KneeMidRightCube_r1", CubeListBuilder.create().texOffs(37, 2).mirror().addBox(-5.0F, -1.0F, 0.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.0F, 0.8727F, 0.0F));

        PartDefinition LEG_REARRIGHT = THORAX.addOrReplaceChild("LEG_REARRIGHT", CubeListBuilder.create().texOffs(21, 0).mirror().addBox(-4.0F, 0.0F, -1.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.5F, -2.0F, 3.0F, 0.0F, 0.2182F, -0.9599F));

        PartDefinition KNEE_REARRIGHT = LEG_REARRIGHT.addOrReplaceChild("KNEE_REARRIGHT", CubeListBuilder.create(), PartPose.offset(-4.0F, 1.5F, 4.5F));

        PartDefinition KneeRearRightCube_r1 = KNEE_REARRIGHT.addOrReplaceChild("KneeRearRightCube_r1", CubeListBuilder.create().texOffs(37, 2).mirror().addBox(-5.0F, -1.0F, 0.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.0F, 1.0472F, 0.0F));

        PartDefinition WING_LEFT = THORAX.addOrReplaceChild("WING_LEFT", CubeListBuilder.create(), PartPose.offset(3.5F, -9.0F, -5.0F));

        PartDefinition WingLeftPlane_r1 = WING_LEFT.addOrReplaceChild("WingLeftPlane_r1", CubeListBuilder.create().texOffs(5, 34).addBox(0.0F, 0.0F, -1.0F, 7.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.2182F, -0.1745F));

        PartDefinition WING_RIGHT = THORAX.addOrReplaceChild("WING_RIGHT", CubeListBuilder.create(), PartPose.offset(-3.5F, -9.0F, -5.0F));

        PartDefinition WingRightPlane_r1 = WING_RIGHT.addOrReplaceChild("WingRightPlane_r1", CubeListBuilder.create().texOffs(5, 34).mirror().addBox(-7.0F, 0.0F, -1.0F, 7.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, -0.2182F, 0.1745F));

        PartDefinition ABDOMEN = ROOT.addOrReplaceChild("ABDOMEN", CubeListBuilder.create().texOffs(29, 8).addBox(-3.5F, 0.0F, -1.0F, 7.0F, 7.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(51, 3).addBox(-0.5F, 3.0F, 9.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 5.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition SADDLE = ROOT.addOrReplaceChild("SADDLE", CubeListBuilder.create().texOffs(0, 42).addBox(-5.5F, -9.25F, -4.0F, 11.0F, 5.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    private float getSine(float time, float max, float min) {
        float so = Mth.sin(time * 0.25f);
        float range = max - min;
        return (so * range) + min;
    }
    
    @Override
    public void setupAnim(BeehemothEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        SADDLE.visible = entity.isSaddled();
        CROWN.visible = entity.isQueen();
        WING_RIGHT.xRot = 0.0f;
        ROOT.xRot = 0.0f;
        ROOT.y = 19.0f;
        boolean onGround = entity.isOnGround() && entity.getDeltaMovement().lengthSqr() < 1.0E-7D;
        if (onGround) {
            WING_RIGHT.yRot = -0.2618f;
            WING_RIGHT.zRot = 0.0f;
            WING_LEFT.xRot = 0.0f;
            WING_LEFT.yRot = 0.2618f;
            WING_LEFT.zRot = 0.0f;
            LEG_FRONTLEFT.xRot = 0.0f;
            LEG_FRONTRIGHT.xRot = 0.0f;
            LEG_MIDLEFT.xRot = 0.0f;
            LEG_MIDRIGHT.xRot = 0.0f;
            LEG_REARLEFT.xRot = 0.0f;
            LEG_REARRIGHT.xRot = 0.0f;
        } 
        else {
            WING_RIGHT.yRot = 0.0f;
            WING_LEFT.zRot = ((float) (Mth.cos((limbSwing + ageInTicks) * 2.1f) * Math.PI * 0.15f));
            WING_LEFT.xRot = WING_RIGHT.xRot;
            WING_LEFT.yRot = WING_RIGHT.yRot;
            WING_RIGHT.zRot = -WING_LEFT.zRot;
            LEG_FRONTLEFT.xRot = 0.7853982f;
            LEG_FRONTRIGHT.xRot = 0.7853982f;
            LEG_MIDLEFT.xRot = 0.7853982f;
            LEG_MIDRIGHT.xRot = 0.7853982f;
            LEG_REARLEFT.xRot = 0.7853982f;
            LEG_REARRIGHT.xRot = 0.7853982f;
            ROOT.xRot = 0.0f;
            ROOT.yRot = 0.0f;
            ROOT.zRot = 0.0f;

            // fr 20, 47.5
            // mr 37.5, 52.5
            // br 45, 62.5

            KneeFrontRightCube_r1.yRot = getSine(ageInTicks + entity.offset1, 0.2f, 0.475f);
            KneeMidRightCube_r1.yRot = getSine(ageInTicks + entity.offset2, 0.375f, 0.525f);
            KneeRearRightCube_r1.yRot = getSine(ageInTicks + entity.offset3, 0.45f, 0.625f);
            KneeFrontLeftCube_r1.yRot = getSine(ageInTicks + entity.offset4, -0.20f, -0.475f);
            KneeMidLeftCube_r1.yRot = getSine(ageInTicks + entity.offset5, -0.375f, -0.525f);
            KneeRearLeftCube_r1.yRot = getSine(ageInTicks + entity.offset6, -0.45f, -0.625f);
        }

        ROOT.xRot = 0.0F;
        ROOT.yRot = 0.0F;
        ROOT.zRot = 0.0F;
        if (!onGround) {
            float f1 = Mth.cos(ageInTicks * 0.18F);
            ROOT.xRot = 0.1F + f1 * (float) Math.PI * 0.015F;
            ANTENNA_LEFT.xRot = f1 * (float) Math.PI * 0.01F;
            ANTENNA_RIGHT.xRot = f1 * (float) Math.PI * 0.01F;
            ROOT.y = 19.0F - Mth.cos(ageInTicks * 0.18F) * 0.9F;
        }

        THORAX.xRot = 0;
        FACE.xRot = (float) (netHeadYaw / Math.PI / 180) + 0.3f;
        ABDOMEN.xRot = (float) (netHeadYaw / Math.PI / 180) - 0.3f;
    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        ROOT.render(poseStack, buffer, packedLight, packedOverlay);
    }
}