package com.telepathicgrunt.the_bumblezone.client.rendering.rootmin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class RootminModel extends HierarchicalModel<RootminEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Bumblezone.MODID, "rootmin"), "main");
    private final ModelPart root;

    public RootminModel(ModelPart root) {
        this.root = root.getChild("root");
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -15.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 0.0F));

        PartDefinition slightly_shut_eyes = body.addOrReplaceChild("slightly_shut_eyes", CubeListBuilder.create().texOffs(27, 37).addBox(2.0F, -9.0F, -8.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.025F))
                .texOffs(27, 37).addBox(-8.0F, -9.0F, -8.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.025F)), PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition almost_shut_eyes = body.addOrReplaceChild("almost_shut_eyes", CubeListBuilder.create().texOffs(27, 37).addBox(2.0F, -9.0F, -8.0F, 6.0F, 3.0F, 2.0F, new CubeDeformation(0.025F))
                .texOffs(27, 37).addBox(-8.0F, -9.0F, -8.0F, 6.0F, 3.0F, 2.0F, new CubeDeformation(0.025F)), PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition shut_eyes = body.addOrReplaceChild("shut_eyes", CubeListBuilder.create().texOffs(27, 37).addBox(2.0F, -9.0F, -8.0F, 6.0F, 5.0F, 2.0F, new CubeDeformation(0.025F))
                .texOffs(27, 37).addBox(-8.0F, -9.0F, -8.0F, 6.0F, 5.0F, 2.0F, new CubeDeformation(0.025F)), PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition mouth = body.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(0, 33).addBox(-2.0F, -2.0F, -8.0F, 4.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, -8.0F));

        PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.0F, -12.0F, -1.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition leg_r1 = right_leg.addOrReplaceChild("leg_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -12.0F, -1.0F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 2.25F, 0.0F, 1.1345F, 0.0F));

        PartDefinition leg_r2 = right_leg.addOrReplaceChild("leg_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -12.0F, -1.0F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 2.25F, 0.0F, -1.1345F, 0.0F));

        PartDefinition right_foot = right_leg.addOrReplaceChild("right_foot", CubeListBuilder.create(), PartPose.offset(0.0F, 11.75F, 0.25F));

        PartDefinition leg_r3 = right_foot.addOrReplaceChild("leg_r3", CubeListBuilder.create().texOffs(0, 49).addBox(-3.0F, 0.0F, 0.5F, 5.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, -1.7453F, 0.0F, 0.0F));

        PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, -12.0F, -1.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition leg_r4 = left_leg.addOrReplaceChild("leg_r4", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -12.0F, -1.0F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 12.0F, 2.25F, 0.0F, 1.1345F, 0.0F));

        PartDefinition leg_r5 = left_leg.addOrReplaceChild("leg_r5", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-5.0F, -12.0F, -1.0F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 12.0F, 2.25F, 0.0F, -1.1345F, 0.0F));

        PartDefinition left_foot = left_leg.addOrReplaceChild("left_foot", CubeListBuilder.create(), PartPose.offset(1.0F, 11.75F, 0.25F));

        PartDefinition leg_r6 = left_foot.addOrReplaceChild("leg_r6", CubeListBuilder.create().texOffs(0, 49).mirror().addBox(-3.0F, 0.0F, 0.5F, 5.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, -1.7453F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(RootminEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity.idleAnimationState, RootminAnimations.IDLE, ageInTicks);
        this.animate(entity.angryAnimationState, RootminAnimations.ANGRY, ageInTicks);
        this.animate(entity.curiousAnimationState, RootminAnimations.CURIOUS, ageInTicks);
        this.animate(entity.curseAnimationState, RootminAnimations.CURSE, ageInTicks);
        this.animate(entity.embarassedAnimationState, RootminAnimations.EMBARASSED, ageInTicks);
        this.animate(entity.shockAnimationState, RootminAnimations.SHOCK, ageInTicks);
        this.animate(entity.shootAnimationState, RootminAnimations.SHOOT, ageInTicks);
        this.animate(entity.runAnimationState, RootminAnimations.RUN, ageInTicks);
        this.animate(entity.walkAnimationState, RootminAnimations.WALK, ageInTicks);
        this.animate(entity.blockToEntityAnimationState, RootminAnimations.BLOCK_TO_ENTITY, ageInTicks);
        this.animate(entity.entityToBlockAnimationState, RootminAnimations.ENTITY_TO_BLOCK, ageInTicks);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

}