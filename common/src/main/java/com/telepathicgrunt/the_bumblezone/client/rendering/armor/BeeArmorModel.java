package com.telepathicgrunt.the_bumblezone.client.rendering.armor;

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
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class BeeArmorModel extends HumanoidModel<LivingEntity> {

    public static final List<ModelLayerLocation> VARIANT_1_ARMOR_LAYER_LOCATIONS = Arrays.asList(
            new ModelLayerLocation(new ResourceLocation(Bumblezone.MODID, "bee_armor_1"), "helmet"),
            new ModelLayerLocation(new ResourceLocation(Bumblezone.MODID, "bee_armor_1"), "chestplate"),
            new ModelLayerLocation(new ResourceLocation(Bumblezone.MODID, "bee_armor_1"), "leggings"),
            new ModelLayerLocation(new ResourceLocation(Bumblezone.MODID, "bee_armor_1"), "boots")
    );
    public static final List<ModelLayerLocation> VARIANT_2_ARMOR_LAYER_LOCATIONS = Arrays.asList(
            new ModelLayerLocation(new ResourceLocation(Bumblezone.MODID, "bee_armor_2"), "helmet"),
            new ModelLayerLocation(new ResourceLocation(Bumblezone.MODID, "bee_armor_2"), "chestplate"),
            new ModelLayerLocation(new ResourceLocation(Bumblezone.MODID, "bee_armor_2"), "leggings"),
            new ModelLayerLocation(new ResourceLocation(Bumblezone.MODID, "bee_armor_2"), "boots")
    );

    protected final EquipmentSlot slot;
    public LivingEntity entityLiving;
    public final ModelPart leftWing;
    public final ModelPart rightWing;
    public final ModelPart leftPollen;
    public final ModelPart rightPollen;
    public final ModelPart trueRightLeg;
    public final ModelPart trueLeftLeg;
    public final ModelPart bootRight;
    public final ModelPart bootLeft;

    public BeeArmorModel(ModelPart part, EquipmentSlot slot, LivingEntity livingEntity) {
        super(part);
        this.slot = slot;
        this.entityLiving = livingEntity;
        this.leftWing = part.getChild("body").getChild("left_wing");
        this.rightWing = part.getChild("body").getChild("right_wing");
        this.leftPollen = part.getChild("left_leg").getChild("true_left_leg").getChild("pollen_left");
        this.rightPollen = part.getChild("right_leg").getChild("true_right_leg").getChild("pollen_right");
        this.trueLeftLeg = part.getChild("left_leg").getChild("true_left_leg");
        this.trueRightLeg = part.getChild("right_leg").getChild("true_right_leg");
        this.bootRight = part.getChild("right_leg").getChild("right_boot");
        this.bootLeft = part.getChild("left_leg").getChild("left_boot");
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
                        leftWing.yRot = -44.5f;
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
                body.render(poseStack, buffer, light, overlay);
            }
            case LEGS -> {
                body.visible = true;
                rightLeg.visible = true;
                leftLeg.visible = true;
                trueRightLeg.visible = true;
                trueLeftLeg.visible = true;
                bootRight.visible = false;
                bootLeft.visible = false;

                ItemStack itemStack = HoneyBeeLeggings.getEntityBeeLegging(entityLiving);
                if (!itemStack.isEmpty() && HoneyBeeLeggings.isPollinated(itemStack)) {
                    leftPollen.visible = true;
                    rightPollen.visible = true;
                }
                else {
                    leftPollen.visible = false;
                    rightPollen.visible = false;
                }
                body.render(poseStack, buffer, light, overlay);
                leftLeg.render(poseStack, buffer, light, overlay);
                rightLeg.render(poseStack, buffer, light, overlay);
            }
            case FEET -> {
                rightLeg.visible = true;
                leftLeg.visible = true;
                bootRight.visible = true;
                bootLeft.visible = true;
                trueRightLeg.visible = false;
                trueLeftLeg.visible = false;
                leftLeg.render(poseStack, buffer, light, overlay);
                rightLeg.render(poseStack, buffer, light, overlay);
            }
        }
    }

    public static void setupBlankBodyDefaults(PartDefinition root) {
        root.addOrReplaceChild("head", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(0, 0, 0));
        root.addOrReplaceChild("hat", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(0, 0, 0));
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(0, 0, 0));
        root.addOrReplaceChild("right_arm", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(0, 0, 0));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(0, 0, 0));

        PartDefinition leftWing = body.addOrReplaceChild("left_wing", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(0, 0, 0));
        PartDefinition rightWing = body.addOrReplaceChild("right_wing", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(0, 0, 0));

        leftWing.addOrReplaceChild("left_wing_parts", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(0, 0, 0));
        rightWing.addOrReplaceChild("right_wing_parts", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(0, 0, 0));

        PartDefinition right_leg_main = root.addOrReplaceChild("right_leg", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(0, 0, 0));
        PartDefinition right_leg = right_leg_main.addOrReplaceChild("true_right_leg", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(0, 0, 0));
        right_leg.addOrReplaceChild("pollen_right", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(0, 0, 0));
        right_leg_main.addOrReplaceChild("right_boot",  CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(0, 0, 0));

        PartDefinition left_leg_main = root.addOrReplaceChild("left_leg", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(0, 0, 0));
        PartDefinition left_leg = left_leg_main.addOrReplaceChild("true_left_leg", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(0, 0, 0));
        left_leg.addOrReplaceChild("pollen_left", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(0, 0, 0));
        left_leg_main.addOrReplaceChild("left_boot", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(0, 0, 0));
    }

    public static LayerDefinition createVariant1Head() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        setupBlankBodyDefaults(root);

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -9F, -6.0F, 10.0F, 9F, 12.0F, new CubeDeformation(0.005F)), PartPose.offset(0, 0.25F, 0));

        head.addOrReplaceChild("antenna_1", CubeListBuilder.create().texOffs(28, 82).addBox(1.5F, -14.0F, 6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0))
                .texOffs(20, 82).addBox(-3.5F, -14.0F, 6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0))
                .texOffs(16, 89).addBox(2.0F, -12.0F, 6.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0))
                .texOffs(20, 89).addBox(-3.0F, -12.0F, 6.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0)), PartPose.offsetAndRotation(0, 0, 0, 1.1781F, 0, 0));

        head.addOrReplaceChild("antenna_2", CubeListBuilder.create().texOffs(40, 82).addBox(-3.0F, -12.5F, 1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0))
                .texOffs(44, 82).addBox(2.0F, -12.5F, 1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0)), PartPose.offsetAndRotation(0, 0, 0, 0.6981F, 0, 0));

        return LayerDefinition.create(mesh, 128, 128);
    }

    public static LayerDefinition createVariant1Chestplate() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        setupBlankBodyDefaults(root);

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 21).addBox(-4.5F, 4.0F, -2.5F, 9.0F, 8.0F, 5.0F, new CubeDeformation(0))
                .texOffs(32, 66).addBox(-3.5F, 2.0F, -2.5F, 7.0F, 2.0F, 5.0F, new CubeDeformation(0))
                .texOffs(0, 82).addBox(-2.5F, 0, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0)), PartPose.offset(0, 0.25F, 0));

        body.addOrReplaceChild("stinger_r1", CubeListBuilder.create().texOffs(36, 82).addBox(-1.0F, 6.5F, -9.25F, 2.0F, 5.0F, 0, new CubeDeformation(0)), PartPose.offsetAndRotation(0, 0, 0, 1.0908F, 0, 0));


        PartDefinition left_wing = body.addOrReplaceChild("left_wing", CubeListBuilder.create(), PartPose.offset(-1.82F, 3.1027F, 2.1854F));

        PartDefinition LeftWing_r1 = left_wing.addOrReplaceChild("LeftWing_r1", CubeListBuilder.create().texOffs(0, 89).addBox(-0.25F, -2.75F, 3.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(1, 87).addBox(-0.25F, -0.75F, 4.5F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(3, 89).addBox(-0.25F, -1.75F, 6.5F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 90).addBox(-0.25F, -2.75F, 7.5F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(1, 91).addBox(-0.25F, -4.75F, 4.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(2, 93).addBox(-0.25F, -5.75F, 5.5F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(3, 91).addBox(-0.25F, -4.75F, 6.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(3, 93).addBox(-0.25F, -6.75F, 6.5F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(5, 91).addBox(-0.25F, -5.75F, 8.5F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5623F, -3.1027F, -2.1854F, -0.6695F, 0.7911F, 1.3711F));

        PartDefinition right_wing = body.addOrReplaceChild("right_wing", CubeListBuilder.create(), PartPose.offset(2.0946F, 2.7065F, 2.1491F));

        PartDefinition RightWing_r1 = right_wing.addOrReplaceChild("RightWing_r1", CubeListBuilder.create().texOffs(12, 91).addBox(-1.0F, -10.25F, 3.75F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 93).addBox(-1.0F, -11.25F, 1.75F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(10, 91).addBox(-1.0F, -9.25F, 1.75F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(9, 93).addBox(-1.0F, -10.25F, 0.75F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 91).addBox(-1.0F, -9.25F, -0.25F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(11, 90).addBox(-1.0F, -7.25F, 2.75F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 89).addBox(-1.0F, -6.25F, 1.75F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 87).addBox(-1.0F, -5.25F, -0.25F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(7, 89).addBox(-1.0F, -7.25F, -1.25F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0946F, -2.7065F, -2.1491F, -0.9071F, 0.8312F, 1.7963F));

        root.addOrReplaceChild("armorRightArm", CubeListBuilder.create(), PartPose.offset(0, 0, 0));

        root.addOrReplaceChild("armorLeftArm", CubeListBuilder.create(), PartPose.offset(0, 0, 0));

        return LayerDefinition.create(mesh, 128, 128);
    }

    public static LayerDefinition createVariant1Leggings() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        setupBlankBodyDefaults(root);

        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(40, 55).addBox(-4.5F, 8.06F, -2.5F, 9.0F, 4.0F, 5.0F, new CubeDeformation(0.05F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_leg_main = root.addOrReplaceChild("right_leg", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0).extend(-0.1F)), PartPose.offset(-1.9F, 12.0F, 0));
        PartDefinition right_leg = right_leg_main.addOrReplaceChild("true_right_leg", CubeListBuilder.create().texOffs(20, 37).addBox(-2.75F, 0, -2.5F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0)), PartPose.offset(0, 0, 0));
        right_leg.addOrReplaceChild("pollen_right", CubeListBuilder.create().texOffs(40, 37).addBox(1.005F, 1.25F, -2.995F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0)), PartPose.offset(-4.0F, 0, 0));

        PartDefinition left_leg_main = root.addOrReplaceChild("left_leg", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0).extend(-0.1F)), PartPose.offset(1.9F, 12.0F, 0));
        PartDefinition left_leg = left_leg_main.addOrReplaceChild("true_left_leg", CubeListBuilder.create().texOffs(0, 37).addBox(-2.25F, 0, -2.5F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0)), PartPose.offset(0, 0, 0));
        left_leg.addOrReplaceChild("pollen_left", CubeListBuilder.create().texOffs(0, 50).addBox(3.0F, 1.25F, -3.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0)), PartPose.offset(-4.0F, 0, 0));

        return LayerDefinition.create(mesh, 128, 128);
    }

    public static LayerDefinition createVariant1Boots() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        setupBlankBodyDefaults(root);

        PartDefinition right_leg_main = root.addOrReplaceChild("right_leg", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(-1.9F, 12.0F, 0));
        right_leg_main.addOrReplaceChild("right_boot", CubeListBuilder.create()
                .texOffs(52, 44).addBox(-1.0F, 6.25F, -5.0F, 2.0F, 2F, 2.0F, new CubeDeformation(0.005F))
                .texOffs(0, 96).addBox(-3.0F, 7F, -4.0F, 6.0F, 5F, 7.0F, new CubeDeformation(0.005F)), PartPose.offset(0, 0, 0));

        PartDefinition left_leg_main = root.addOrReplaceChild("left_leg", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(1.9F, 12.0F, 0));
        left_leg_main.addOrReplaceChild("left_boot", CubeListBuilder.create()
                .texOffs(0, 96).addBox(-3.0F, 7F, -4.0F, 6.0F, 5F, 7.0F, new CubeDeformation(0.005F))
                .texOffs(52, 44).addBox(-1.0F, 6.25F, -5.0F, 2.0F, 2F, 2.0F, new CubeDeformation(0.005F)), PartPose.offset(0, 0, 0));

        return LayerDefinition.create(mesh, 128, 128);
    }

    public static LayerDefinition createVariant2Head() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        setupBlankBodyDefaults(root);

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -9F, -6.0F, 10.0F, 4F, 12.0F, new CubeDeformation(0)), PartPose.offset(0, 0, 0));
        PartDefinition antennas = head.addOrReplaceChild("antennas", CubeListBuilder.create(), PartPose.offset(0, -3.0F, 3.0F));

        antennas.addOrReplaceChild("antennaEnd_1", CubeListBuilder.create().texOffs(28, 82).addBox(1.5F, -14.0F, 6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0))
                .texOffs(20, 82).addBox(-3.5F, -14.0F, 6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0))
                .texOffs(16, 89).addBox(2.0F, -12.0F, 6.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0))
                .texOffs(20, 89).addBox(-3.0F, -12.0F, 6.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0)), PartPose.offsetAndRotation(0, 0, 0, 1.6144F, 0, 0));

        antennas.addOrReplaceChild("antennaEnd_2", CubeListBuilder.create().texOffs(40, 82).addBox(-3.0F, -12.5F, 1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0))
                .texOffs(44, 82).addBox(2.0F, -12.5F, 1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0)), PartPose.offsetAndRotation(0, 0, 0, 1.1345F, 0, 0));

        return LayerDefinition.create(mesh, 128, 128);
    }

    public static LayerDefinition createVariant2Chestplate() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        setupBlankBodyDefaults(root);

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 19).addBox(-4.5F, 0, -2.5F, 9.0F, 12.0F, 5.0F, new CubeDeformation(0)), PartPose.offset(0, 0.25F, 0));

        body.addOrReplaceChild("stinger_r1", CubeListBuilder.create().texOffs(36, 82).addBox(-0.5F, 0.5F, 0.75F, 1.0F, 5.0F, 0, new CubeDeformation(0)), PartPose.offsetAndRotation(0, 12.0F, 0, 1.5708F, 0, 0));

        PartDefinition right_wing = body.addOrReplaceChild("right_wing", CubeListBuilder.create(), PartPose.offset(2.0946F, 2.9565F, 2.3991F));

        PartDefinition RightWing_r1 = right_wing.addOrReplaceChild("RightWing_r1", CubeListBuilder.create().texOffs(13, 77).addBox(-1.0F, -10.25F, 3.75F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(9, 85).addBox(-1.0F, -9.25F, 1.75F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 82).addBox(-1.0F, -11.25F, 1.75F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(6, 86).addBox(-1.0F, -10.25F, 0.75F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(6, 86).addBox(-1.0F, -6.25F, 1.75F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(6, 86).addBox(-1.0F, -6.25F, 1.75F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(3, 86).addBox(-1.0F, -7.25F, 2.75F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(12, 85).addBox(-1.0F, -9.25F, -0.25F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 77).addBox(-1.0F, -5.25F, -0.25F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 85).addBox(-1.0F, -7.25F, -1.25F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.8446F, -2.9565F, -2.8991F, -0.9071F, 0.8312F, 1.7963F));

        PartDefinition left_wing = body.addOrReplaceChild("left_wing", CubeListBuilder.create(), PartPose.offset(-2.1554F, 2.9565F, 2.3991F));

        PartDefinition LeftWing_r1 = left_wing.addOrReplaceChild("LeftWing_r1", CubeListBuilder.create().texOffs(5, 74).addBox(-1.0F, -10.25F, 1.75F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(5, 82).addBox(-1.0F, -8.25F, 1.75F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 85).addBox(-1.0F, -9.25F, 4.75F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(6, 86).addBox(-1.0F, -7.25F, 3.75F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(6, 86).addBox(-1.0F, -8.25F, -0.25F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(3, 86).addBox(-1.0F, -9.25F, 0.75F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 82).addBox(-1.0F, -6.25F, 1.75F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 82).addBox(-1.0F, -5.25F, -0.25F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 80).addBox(-1.0F, -7.25F, -1.25F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.8446F, -2.9565F, -2.8991F, -0.9071F, 0.8312F, 1.7963F));

        root.addOrReplaceChild("armorRightArm", CubeListBuilder.create(), PartPose.offset(0, 0, 0));

        root.addOrReplaceChild("armorLeftArm", CubeListBuilder.create(), PartPose.offset(0, 0, 0));

        return LayerDefinition.create(mesh, 128, 128);
    }

    public static LayerDefinition createVariant2Leggings() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        setupBlankBodyDefaults(root);

        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(40, 55).addBox(-4.5F, 8.06F, -2.5F, 9.0F, 4.0F, 5.0F, new CubeDeformation(-0.05F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_leg_main = root.addOrReplaceChild("right_leg", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(-1.9F, 12.0F, 0));
        PartDefinition right_leg = right_leg_main.addOrReplaceChild("true_right_leg", CubeListBuilder.create().texOffs(0, 37).addBox(-2.5F, 0, -2.5F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0)), PartPose.offset(0, 0, 0));
        PartDefinition pollen_right = right_leg.addOrReplaceChild("pollen_right", CubeListBuilder.create().texOffs(40, 37).addBox(-2.995F, 1.5F, -3.0F, 3.0F, 5.0F, 6.0F, new CubeDeformation(0)), PartPose.offset(0, 0, 0));

        PartDefinition left_leg_main = root.addOrReplaceChild("left_leg", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(1.9F, 12.0F, 0));
        PartDefinition left_leg = left_leg_main.addOrReplaceChild("true_left_leg", CubeListBuilder.create().texOffs(20, 37).addBox(-2.5F, 0, -2.5F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0)), PartPose.offset(0, 0, 0));
        PartDefinition pollen_left = left_leg.addOrReplaceChild("pollen_left", CubeListBuilder.create().texOffs(0, 50).addBox(0.005F, 1.5F, -3.0F, 3.0F, 5.0F, 6.0F, new CubeDeformation(0)), PartPose.offset(0, 0, 0));

        return LayerDefinition.create(mesh, 128, 128);
    }

    public static LayerDefinition createVariant2Boots() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        setupBlankBodyDefaults(root);

        PartDefinition right_leg_main = root.addOrReplaceChild("right_leg", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(-1.9F, 12.0F, 0));
        right_leg_main.addOrReplaceChild("right_boot", CubeListBuilder.create()
                        .texOffs(0, 96).addBox(-3.0F, 7.0F, -3.0F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.005F))
                        .texOffs(22, 107).addBox(-2.901F, 7.9F, -2.1F, 5.0F, 4.0F, 5.0F, new CubeDeformation(-0.1F)),
                PartPose.offset(0, 0, 0));

        PartDefinition left_leg_main = root.addOrReplaceChild("left_leg", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(1.9F, 12.0F, 0));
        left_leg_main.addOrReplaceChild("left_boot", CubeListBuilder.create()
                        .texOffs(0, 96).addBox(-2.0F, 7.0F, -3.0F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.005F))
                        .texOffs(22, 98).addBox(-2.099F, 7.9F, -2.1F, 5.0F, 4.0F, 5.0F, new CubeDeformation(-0.1F)),
                PartPose.offset(0, 0, 0));

        return LayerDefinition.create(mesh, 128, 128);
    }
}