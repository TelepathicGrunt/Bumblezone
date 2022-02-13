package com.telepathicgrunt.the_bumblezone.client.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.items.BeeArmor;
import com.telepathicgrunt.the_bumblezone.items.BumbleBeeChestplate;
import com.telepathicgrunt.the_bumblezone.items.HoneyBeeLeggings;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class BeeArmorModel extends HumanoidModel<LivingEntity> {

    public static final ModelLayerLocation VARIANT_1_LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Bumblezone.MODID, "bee_armor"), "bee_armor");
    public static final ModelLayerLocation VARIANT_2_LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Bumblezone.MODID, "bee_armor"), "bee_armor_2");
    protected final EquipmentSlot slot;
    protected final LivingEntity entityLiving;
    public final ModelPart leftWing;
    public final ModelPart rightWing;
    public final ModelPart leftPollen;
    public final ModelPart rightPollen;

    public BeeArmorModel(ModelPart part, EquipmentSlot slot, LivingEntity itemStack) {
        super(part);
        this.slot = slot;
        this.entityLiving = itemStack;
        this.leftWing = part.getChild("body").getChild("left_wing");
        this.rightWing = part.getChild("body").getChild("right_wing");
        this.leftPollen = part.getChild("left_leg").getChild("left_pollen");
        this.rightPollen = part.getChild("right_leg").getChild("right_pollen");
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int light, int overlay, float r, float g, float b, float a) {

        setAllVisible(false);
        switch (slot) {
            case HEAD -> {
                head.visible = true;
                hat.visible = true;

                head.render(poseStack, buffer, light, overlay);
            }
            case CHEST -> {
                body.visible = true;
                rightArm.visible = true;
                leftArm.visible = true;

                body.render(poseStack, buffer, light, overlay);
                ItemStack itemStack = BumbleBeeChestplate.getEntityBeeChestplate(entityLiving);
                if (!itemStack.isEmpty() && itemStack.getOrCreateTag().getBoolean("isFlying")) {
                    long time = System.currentTimeMillis();
                    double currentProg = Math.abs(Math.sin(time / 40d));
                    leftWing.yRot = -45;
                    leftWing.xRot = (float) Mth.lerp(currentProg, -0.5f, 1.5f);
                    rightWing.yRot = 45;
                    rightWing.xRot = (float) Mth.lerp(currentProg, -0.5f, 1.5f);
                    if (itemStack.getItem() instanceof BeeArmor beeArmor && beeArmor.getVariant() == 2) {
                        rightWing.zRot = 0f;
                    }
                }
                else {
                    if (itemStack.getItem() instanceof BeeArmor beeArmor && beeArmor.getVariant() == 2) {
                        leftWing.yRot = -0.2f;
                        leftWing.xRot = -0.15f;
                        rightWing.yRot = 0.2f;
                        rightWing.xRot = -0.2f;
                        rightWing.zRot = -0.5f;
                    }
                    else {
                        leftWing.yRot = -0.6f;
                        leftWing.xRot = -0.2f;
                        rightWing.yRot = 0.6f;
                        rightWing.xRot = -0.2f;
                    }
                }
            }
            case LEGS -> {
                body.visible = true;
                rightLeg.visible = true;
                leftLeg.visible = true;

                leftLeg.render(poseStack, buffer, light, overlay);
                rightLeg.render(poseStack, buffer, light, overlay);
                ItemStack itemStack = HoneyBeeLeggings.getEntityBeeLegging(entityLiving);
                if (!itemStack.isEmpty() && HoneyBeeLeggings.isPollinated(itemStack)) {
                    leftPollen.visible = true;
                    rightPollen.visible = true;
                }
                else {
                    leftPollen.visible = false;
                    rightPollen.visible = false;
                }
            }
            case FEET -> {
                rightLeg.visible = true;
                leftLeg.visible = true;
            }
        }
    }

    public static LayerDefinition createVariant1() {
        var meshdefinition = HumanoidModel.createMesh(new CubeDeformation(1.0F), 0F);
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition armorHead = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -9.0F, -6.0F, 10.0F, 9.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.25F, 0.0F));

        PartDefinition cube_r1 = armorHead.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(28, 82).addBox(1.5F, -14.0F, 6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(20, 82).addBox(-3.5F, -14.0F, 6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(16, 89).addBox(2.0F, -12.0F, 6.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 89).addBox(-3.0F, -12.0F, 6.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));

        PartDefinition cube_r2 = armorHead.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(40, 82).addBox(-3.0F, -12.5F, 1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(44, 82).addBox(2.0F, -12.5F, 1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.6981F, 0.0F, 0.0F));

        PartDefinition armorBody = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 21).addBox(-4.505F, 4.0F, -2.505F, 9.01F, 8.0F, 5.01F, new CubeDeformation(0.0F))
                .texOffs(32, 66).addBox(-3.5F, 2.0F, -2.5F, 7.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 82).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.25F, 0.0F));

        PartDefinition cube_r3 = armorBody.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(36, 82).addBox(-1.0F, 6.5F, -9.25F, 2.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.0908F, 0.0F, 0.0F));

        PartDefinition left_wing = armorBody.addOrReplaceChild("left_wing", CubeListBuilder.create(), PartPose.offset(-1.5623F, 3.1027F, 2.1854F));

        PartDefinition LeftWing_r1 = left_wing.addOrReplaceChild("LeftWing_r1", CubeListBuilder.create().texOffs(0, 89).addBox(-0.25F, -2.75F, 3.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(1, 87).addBox(-0.25F, -0.75F, 4.5F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(3, 89).addBox(-0.25F, -1.75F, 6.5F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 90).addBox(-0.25F, -2.75F, 7.5F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(1, 91).addBox(-0.25F, -4.75F, 4.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(2, 93).addBox(-0.25F, -5.75F, 5.5F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(3, 91).addBox(-0.25F, -4.75F, 6.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(3, 93).addBox(-0.25F, -6.75F, 6.5F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(5, 91).addBox(-0.25F, -5.75F, 8.5F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5623F, -3.1027F, -2.1854F, -0.6695F, 0.7911F, 1.3711F));

        PartDefinition right_wing = armorBody.addOrReplaceChild("right_wing", CubeListBuilder.create(), PartPose.offset(2.0946F, 2.7065F, 2.1491F));

        PartDefinition RightWing_r1 = right_wing.addOrReplaceChild("RightWing_r1", CubeListBuilder.create().texOffs(12, 91).addBox(-1.0F, -10.25F, 3.75F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 93).addBox(-1.0F, -11.25F, 1.75F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(10, 91).addBox(-1.0F, -9.25F, 1.75F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(9, 93).addBox(-1.0F, -10.25F, 0.75F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 91).addBox(-1.0F, -9.25F, -0.25F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(11, 90).addBox(-1.0F, -7.25F, 2.75F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 89).addBox(-1.0F, -6.25F, 1.75F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 87).addBox(-1.0F, -5.25F, -0.25F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(7, 89).addBox(-1.0F, -7.25F, -1.25F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0946F, -2.7065F, -2.1491F, -0.9071F, 0.8312F, 1.7963F));

        PartDefinition armorRightLeg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(20, 37).addBox(-2.75F, 0.0F, -2.5F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition pollen_right = armorRightLeg.addOrReplaceChild("right_pollen", CubeListBuilder.create().texOffs(40, 37).addBox(1.005F, 1.25F, -2.995F, 3.99F, 4.0F, 5.99F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, 0.0F));

        PartDefinition armorLeftLeg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 37).addBox(-2.25F, 0.0F, -2.5F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, 0.0F));

        PartDefinition pollen_left = armorLeftLeg.addOrReplaceChild("left_pollen", CubeListBuilder.create().texOffs(0, 50).addBox(2.995F, 1.25F, -2.995F, 3.99F, 4.0F, 5.99F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    public static LayerDefinition createVariant2() {
        var meshdefinition = HumanoidModel.createMesh(new CubeDeformation(1.0F), 0F);
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition armorHead = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -9.0F, -6.0F, 10.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.25F, 0.0F));

        PartDefinition antennas = armorHead.addOrReplaceChild("antennas", CubeListBuilder.create(), PartPose.offset(0.0F, -3.0F, 3.0F));

        PartDefinition cube_r1 = antennas.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(28, 82).addBox(1.5F, -14.0F, 6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(20, 82).addBox(-3.5F, -14.0F, 6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(16, 89).addBox(2.0F, -12.0F, 6.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 89).addBox(-3.0F, -12.0F, 6.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.6144F, 0.0F, 0.0F));

        PartDefinition cube_r2 = antennas.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(40, 82).addBox(-3.0F, -12.5F, 1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(44, 82).addBox(2.0F, -12.5F, 1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1345F, 0.0F, 0.0F));

        PartDefinition armorBody = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 19).addBox(-4.505F, 0.0F, -2.505F, 9.01F, 12.0F, 5.01F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.25F, 0.0F));

        PartDefinition cube_r3 = armorBody.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(36, 82).addBox(-0.5F, 0.5F, 0.75F, 1.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition right_wing = armorBody.addOrReplaceChild("right_wing", CubeListBuilder.create(), PartPose.offset(2.0946F, 2.9565F, 2.3991F));

        PartDefinition RightWing_r1 = right_wing.addOrReplaceChild("RightWing_r1", CubeListBuilder.create().texOffs(13, 77).addBox(-1.0F, -10.25F, 3.75F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(9, 85).addBox(-1.0F, -9.25F, 1.75F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 82).addBox(-1.0F, -11.25F, 1.75F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(6, 86).addBox(-1.0F, -10.25F, 0.75F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(6, 86).addBox(-1.0F, -6.25F, 1.75F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(3, 86).addBox(-1.0F, -7.25F, 2.75F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(12, 85).addBox(-1.0F, -9.25F, -0.25F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 77).addBox(-1.0F, -5.25F, -0.25F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 85).addBox(-1.0F, -7.25F, -1.25F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.8446F, -2.9565F, -2.8991F, -0.9071F, 0.8312F, 1.7963F));

        PartDefinition left_wing = armorBody.addOrReplaceChild("left_wing", CubeListBuilder.create(), PartPose.offset(-2.1554F, 2.9565F, 2.3991F));

        PartDefinition LeftWing_r1 = left_wing.addOrReplaceChild("LeftWing_r1", CubeListBuilder.create().texOffs(5, 74).addBox(-1.0F, -10.25F, 1.75F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(5, 82).addBox(-1.0F, -8.25F, 1.75F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 85).addBox(-1.0F, -9.25F, 4.75F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(6, 86).addBox(-1.0F, -7.25F, 3.75F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(6, 86).addBox(-1.0F, -8.25F, -0.25F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(3, 86).addBox(-1.0F, -9.25F, 0.75F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 82).addBox(-1.0F, -6.25F, 1.75F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 82).addBox(-1.0F, -5.25F, -0.25F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 80).addBox(-1.0F, -7.25F, -1.25F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.8446F, -2.9565F, -2.8991F, -0.9071F, 0.8312F, 1.7963F));

        PartDefinition leftLeg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(20, 37).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, 0.0F));

        PartDefinition leftPollen = leftLeg.addOrReplaceChild("left_pollen", CubeListBuilder.create().texOffs(40, 37).addBox(0.005F, 1.5F, -2.995F, 2.99f, 5.0F, 5.99f, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition rightLeg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 37).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.0F, 0.0F));

        PartDefinition rightPollen = rightLeg.addOrReplaceChild("right_pollen", CubeListBuilder.create().texOffs(0, 50).addBox(-2.995F, 1.5F, -2.995F, 2.99f, 5.0F, 5.99f, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}