package com.telepathicgrunt.the_bumblezone.client.rendering;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.ModelUtils;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class BeehemothModel extends EntityModel<BeehemothEntity> {
    private float bodyPitch;

    private final ModelRenderer ROOT;
    private final ModelRenderer FACE;
    private final ModelRenderer THORAX;
    private final ModelRenderer CROWN;
    private final ModelRenderer LEG_FRONTLEFT;
    private final ModelRenderer KNEE_FRONTLEFT;
    private final ModelRenderer ANTENNA_LEFT;
    private final ModelRenderer ANTENNA_RIGHT;
    private final ModelRenderer KneeFrontLeftCube_r1;
    private final ModelRenderer LEG_MIDLEFT;
    private final ModelRenderer KNEE_MIDLEFT;
    private final ModelRenderer KneeMidLeftCube_r1;
    private final ModelRenderer LEG_REARLEFT;
    private final ModelRenderer KNEE_REARLEFT;
    private final ModelRenderer KneeRearLeftCube_r1;
    private final ModelRenderer LEG_FRONTRIGHT;
    private final ModelRenderer KNEE_FRONTRIGHT;
    private final ModelRenderer KneeFrontRightCube_r1;
    private final ModelRenderer LEG_MIDRIGHT;
    private final ModelRenderer KNEE_MIDRIGHT;
    private final ModelRenderer KneeMidRightCube_r1;
    private final ModelRenderer LEG_REARRIGHT;
    private final ModelRenderer KNEE_REARRIGHT;
    private final ModelRenderer KneeRearRightCube_r1;
    private final ModelRenderer WING_LEFT;
    private final ModelRenderer WingLeftPlane_r1;
    private final ModelRenderer WING_RIGHT;
    private final ModelRenderer WingRightPlane_r1;
    private final ModelRenderer ABDOMEN;
    private final ModelRenderer SADDLE;

    public BeehemothModel() {
        texWidth = 64;
        texHeight = 64;

        ROOT = new ModelRenderer(this);
        ROOT.setPos(0.0F, 24.0F, 0.0F);

        FACE = new ModelRenderer(this);
        FACE.setPos(0.0F, -8.0F, -6.0F);
        ROOT.addChild(FACE);
        setRotationAngle(FACE, 0.3927F, 0.0F, 0.0F);
        FACE.texOffs(0, 0).addBox(-3.5F, 0.0F, -6.0F, 7.0F, 7.0F, 7.0F, 0.0F, false);

        ANTENNA_LEFT = new ModelRenderer(this);
        ANTENNA_LEFT.setPos(0.0F, 8.0F, 6.0F);
        FACE.addChild(ANTENNA_LEFT);
        ANTENNA_LEFT.texOffs(57, 1).addBox(-1.5F, -8.0F, -15.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);

        ANTENNA_RIGHT = new ModelRenderer(this);
        ANTENNA_RIGHT.setPos(0.0F, 8.0F, 6.0F);
        FACE.addChild(ANTENNA_RIGHT);
        ANTENNA_RIGHT.texOffs(57, 1).addBox(1.5F, -8.0F, -15.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);

        CROWN = new ModelRenderer(this);
        CROWN.setPos(0.0F, 0.0F, 0.0F);
        FACE.addChild(CROWN);
        CROWN.texOffs(40, 26).addBox(-2.5F, -3.0F, -5.0F, 5.0F, 3.0F, 5.0F, 0.0F, false);

        THORAX = new ModelRenderer(this);
        THORAX.setPos(0.0F, 0.0F, 0.0F);
        ROOT.addChild(THORAX);
        THORAX.texOffs(0, 14).addBox(-4.5F, -9.0F, -6.0F, 9.0F, 9.0F, 11.0F, 0.0F, false);

        LEG_FRONTLEFT = new ModelRenderer(this);
        LEG_FRONTLEFT.setPos(4.5F, -2.0F, -4.0F);
        THORAX.addChild(LEG_FRONTLEFT);
        setRotationAngle(LEG_FRONTLEFT, 0.0F, 0.0873F, 1.309F);
        LEG_FRONTLEFT.texOffs(21, 0).addBox(-1.0F, 0.0F, -1.5F, 5.0F, 3.0F, 3.0F, 0.0F, false);

        KNEE_FRONTLEFT = new ModelRenderer(this);
        KNEE_FRONTLEFT.setPos(4.0F, 1.5F, 4.5F);
        LEG_FRONTLEFT.addChild(KNEE_FRONTLEFT);


        KneeFrontLeftCube_r1 = new ModelRenderer(this);
        KneeFrontLeftCube_r1.setPos(0.0F, 0.0F, -6.0F);
        KNEE_FRONTLEFT.addChild(KneeFrontLeftCube_r1);
        setRotationAngle(KneeFrontLeftCube_r1, 0.0F, -0.6981F, 0.0F);
        KneeFrontLeftCube_r1.texOffs(37, 2).addBox(0.0F, -1.0F, 0.0F, 5.0F, 2.0F, 2.0F, 0.0F, false);

        LEG_MIDLEFT = new ModelRenderer(this);
        LEG_MIDLEFT.setPos(4.5F, -2.0F, -0.5F);
        THORAX.addChild(LEG_MIDLEFT);
        setRotationAngle(LEG_MIDLEFT, 0.0F, 0.0F, 1.1345F);
        LEG_MIDLEFT.texOffs(21, 0).addBox(-1.0F, 0.0F, -1.5F, 5.0F, 3.0F, 3.0F, 0.0F, false);

        KNEE_MIDLEFT = new ModelRenderer(this);
        KNEE_MIDLEFT.setPos(4.0F, 1.5F, 4.5F);
        LEG_MIDLEFT.addChild(KNEE_MIDLEFT);


        KneeMidLeftCube_r1 = new ModelRenderer(this);
        KneeMidLeftCube_r1.setPos(0.0F, 0.0F, -6.0F);
        KNEE_MIDLEFT.addChild(KneeMidLeftCube_r1);
        setRotationAngle(KneeMidLeftCube_r1, 0.0F, -0.8727F, 0.0F);
        KneeMidLeftCube_r1.texOffs(37, 2).addBox(0.0F, -1.0F, 0.0F, 5.0F, 2.0F, 2.0F, 0.0F, false);

        LEG_REARLEFT = new ModelRenderer(this);
        LEG_REARLEFT.setPos(4.5F, -2.0F, 3.0F);
        THORAX.addChild(LEG_REARLEFT);
        setRotationAngle(LEG_REARLEFT, 0.0F, -0.2182F, 0.9599F);
        LEG_REARLEFT.texOffs(21, 0).addBox(-1.0F, 0.0F, -1.5F, 5.0F, 3.0F, 3.0F, 0.0F, false);

        KNEE_REARLEFT = new ModelRenderer(this);
        KNEE_REARLEFT.setPos(4.0F, 1.5F, 4.5F);
        LEG_REARLEFT.addChild(KNEE_REARLEFT);


        KneeRearLeftCube_r1 = new ModelRenderer(this);
        KneeRearLeftCube_r1.setPos(0.0F, 0.0F, -6.0F);
        KNEE_REARLEFT.addChild(KneeRearLeftCube_r1);
        setRotationAngle(KneeRearLeftCube_r1, 0.0F, -1.0472F, 0.0F);
        KneeRearLeftCube_r1.texOffs(37, 2).addBox(0.0F, -1.0F, 0.0F, 5.0F, 2.0F, 2.0F, 0.0F, false);

        LEG_FRONTRIGHT = new ModelRenderer(this);
        LEG_FRONTRIGHT.setPos(-4.5F, -2.0F, -4.0F);
        THORAX.addChild(LEG_FRONTRIGHT);
        setRotationAngle(LEG_FRONTRIGHT, 0.0F, -0.0873F, -1.309F);
        LEG_FRONTRIGHT.texOffs(21, 0).addBox(-4.0F, 0.0F, -1.5F, 5.0F, 3.0F, 3.0F, 0.0F, true);

        KNEE_FRONTRIGHT = new ModelRenderer(this);
        KNEE_FRONTRIGHT.setPos(-4.0F, 1.5F, 4.5F);
        LEG_FRONTRIGHT.addChild(KNEE_FRONTRIGHT);


        KneeFrontRightCube_r1 = new ModelRenderer(this);
        KneeFrontRightCube_r1.setPos(0.0F, 0.0F, -6.0F);
        KNEE_FRONTRIGHT.addChild(KneeFrontRightCube_r1);
        setRotationAngle(KneeFrontRightCube_r1, 0.0F, 0.6981F, 0.0F);
        KneeFrontRightCube_r1.texOffs(37, 2).addBox(-5.0F, -1.0F, 0.0F, 5.0F, 2.0F, 2.0F, 0.0F, true);

        LEG_MIDRIGHT = new ModelRenderer(this);
        LEG_MIDRIGHT.setPos(-4.5F, -2.0F, -0.5F);
        THORAX.addChild(LEG_MIDRIGHT);
        setRotationAngle(LEG_MIDRIGHT, 0.0F, 0.0F, -1.1345F);
        LEG_MIDRIGHT.texOffs(21, 0).addBox(-4.0F, 0.0F, -1.5F, 5.0F, 3.0F, 3.0F, 0.0F, true);

        KNEE_MIDRIGHT = new ModelRenderer(this);
        KNEE_MIDRIGHT.setPos(-4.0F, 1.5F, 4.5F);
        LEG_MIDRIGHT.addChild(KNEE_MIDRIGHT);


        KneeMidRightCube_r1 = new ModelRenderer(this);
        KneeMidRightCube_r1.setPos(0.0F, 0.0F, -6.0F);
        KNEE_MIDRIGHT.addChild(KneeMidRightCube_r1);
        setRotationAngle(KneeMidRightCube_r1, 0.0F, 0.8727F, 0.0F);
        KneeMidRightCube_r1.texOffs(37, 2).addBox(-5.0F, -1.0F, 0.0F, 5.0F, 2.0F, 2.0F, 0.0F, true);

        LEG_REARRIGHT = new ModelRenderer(this);
        LEG_REARRIGHT.setPos(-4.5F, -2.0F, 3.0F);
        THORAX.addChild(LEG_REARRIGHT);
        setRotationAngle(LEG_REARRIGHT, 0.0F, 0.2182F, -0.9599F);
        LEG_REARRIGHT.texOffs(21, 0).addBox(-4.0F, 0.0F, -1.5F, 5.0F, 3.0F, 3.0F, 0.0F, true);

        KNEE_REARRIGHT = new ModelRenderer(this);
        KNEE_REARRIGHT.setPos(-4.0F, 1.5F, 4.5F);
        LEG_REARRIGHT.addChild(KNEE_REARRIGHT);


        KneeRearRightCube_r1 = new ModelRenderer(this);
        KneeRearRightCube_r1.setPos(0.0F, 0.0F, -6.0F);
        KNEE_REARRIGHT.addChild(KneeRearRightCube_r1);
        setRotationAngle(KneeRearRightCube_r1, 0.0F, 1.0472F, 0.0F);
        KneeRearRightCube_r1.texOffs(37, 2).addBox(-5.0F, -1.0F, 0.0F, 5.0F, 2.0F, 2.0F, 0.0F, true);

        WING_LEFT = new ModelRenderer(this);
        WING_LEFT.setPos(3.5F, -9.0F, -5.0F);
        THORAX.addChild(WING_LEFT);

        WingLeftPlane_r1 = new ModelRenderer(this);
        WingLeftPlane_r1.setPos(0.0F, 0.0F, 1.0F);
        WING_LEFT.addChild(WingLeftPlane_r1);
        setRotationAngle(WingLeftPlane_r1, 0.0F, 0.2182F, -0.1745F);
        WingLeftPlane_r1.texOffs(5, 34).addBox(0.0F, 0.0F, -1.0F, 7.0F, 0.0F, 8.0F, 0.0F, false);

        WING_RIGHT = new ModelRenderer(this);
        WING_RIGHT.setPos(-3.5F, -9.0F, -5.0F);
        THORAX.addChild(WING_RIGHT);


        WingRightPlane_r1 = new ModelRenderer(this);
        WingRightPlane_r1.setPos(0.0F, 0.0F, 1.0F);
        WING_RIGHT.addChild(WingRightPlane_r1);
        setRotationAngle(WingRightPlane_r1, 0.0F, -0.2182F, 0.1745F);
        WingRightPlane_r1.texOffs(5, 34).addBox(-7.0F, 0.0F, -1.0F, 7.0F, 0.0F, 8.0F, 0.0F, true);

        ABDOMEN = new ModelRenderer(this);
        ABDOMEN.setPos(0.0F, -8.0F, 5.0F);
        ROOT.addChild(ABDOMEN);
        setRotationAngle(ABDOMEN, -0.3927F, 0.0F, 0.0F);
        ABDOMEN.texOffs(29, 8).addBox(-3.5F, 0.0F, -1.0F, 7.0F, 7.0F, 10.0F, 0.0F, false);
        ABDOMEN.texOffs(51, 3).addBox(-0.5F, 3.0F, 9.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        SADDLE = new ModelRenderer(this);
        SADDLE.setPos(0.0F, 0.0F, 0.0F);
        ROOT.addChild(SADDLE);
        SADDLE.texOffs(0, 42).addBox(-5.5F, -9.25F, -4.0F, 11.0F, 5.0F, 9.0F, 0.0F, false);
    }

    @Override
    public void prepareMobModel(BeehemothEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        super.prepareMobModel(entityIn, limbSwing, limbSwingAmount, partialTick);
    }

    private float getSine(float time, float max, float min) {
        float so = MathHelper.sin(time * 0.25f);
        float range = max - min;
        float out = (so * range) + min;
        return out;
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
        } else {
            WING_RIGHT.yRot = 0.0f;
            WING_LEFT.zRot = ((float) (MathHelper.cos((limbSwing + ageInTicks) * 2.1f) * Math.PI * 0.15f));
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
            float f1 = MathHelper.cos(ageInTicks * 0.18F);
            ROOT.xRot = 0.1F + f1 * (float) Math.PI * 0.015F;
            ANTENNA_LEFT.xRot = f1 * (float) Math.PI * 0.01F;
            ANTENNA_RIGHT.xRot = f1 * (float) Math.PI * 0.01F;
            ROOT.y = 19.0F - MathHelper.cos(ageInTicks * 0.18F) * 0.9F;
        }

        if (this.bodyPitch > 0.0F) {
            // Change pitch to affect abdomen and head
            ROOT.xRot = ModelUtils.rotlerpRad(ROOT.xRot, 3.0915928F, this.bodyPitch);
        }

        THORAX.xRot = 0;
        FACE.xRot = (float) (netHeadYaw / Math.PI / 180) + 0.3f;
        ABDOMEN.xRot = (float) (netHeadYaw / Math.PI / 180) - 0.3f;
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        ROOT.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}