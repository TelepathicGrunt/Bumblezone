package com.telepathicgrunt.the_bumblezone.client.rendering.rootmin;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class RootminModel extends EntityModel<RootminRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "rootmin"), "main");

    private final KeyframeAnimation idleAnimation;
    private final KeyframeAnimation angryAnimation;
    private final KeyframeAnimation curiousAnimation;
    private final KeyframeAnimation curseAnimation;
    private final KeyframeAnimation embarassedAnimation;
    private final KeyframeAnimation shockAnimation;
    private final KeyframeAnimation shootAnimation;
    private final KeyframeAnimation runAnimation;
    private final KeyframeAnimation walkAnimation;
    private final KeyframeAnimation blockToEntityAnimation;
    private final KeyframeAnimation entityToBlockAnimation;

    public RootminModel(ModelPart root) {
        super(root);
        this.idleAnimation = RootminAnimations.IDLE.bake(root);
        this.angryAnimation = RootminAnimations.ANGRY.bake(root);
        this.curiousAnimation = RootminAnimations.CURIOUS.bake(root);
        this.curseAnimation = RootminAnimations.CURSE.bake(root);
        this.embarassedAnimation = RootminAnimations.EMBARASSED.bake(root);
        this.shockAnimation = RootminAnimations.SHOCK.bake(root);
        this.shootAnimation = RootminAnimations.SHOOT.bake(root);
        this.runAnimation = RootminAnimations.RUN.bake(root);
        this.walkAnimation = RootminAnimations.WALK.bake(root);
        this.blockToEntityAnimation = RootminAnimations.BLOCK_TO_ENTITY.bake(root);
        this.entityToBlockAnimation = RootminAnimations.ENTITY_TO_BLOCK.bake(root);
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
    public void setupAnim(RootminRenderState state) {
        super.setupAnim(state);
        this.idleAnimation.apply(state.idleAnimationState, state.ageInTicks);
        this.angryAnimation.apply(state.angryAnimationState, state.ageInTicks);
        this.curiousAnimation.apply(state.curiousAnimationState, state.ageInTicks);
        this.curseAnimation.apply(state.curseAnimationState, state.ageInTicks);
        this.embarassedAnimation.apply(state.embarassedAnimationState, state.ageInTicks);
        this.shockAnimation.apply(state.shockAnimationState, state.ageInTicks);
        this.shootAnimation.apply(state.shootAnimationState, state.ageInTicks);
        this.runAnimation.apply(state.runAnimationState, state.ageInTicks);
        this.walkAnimation.apply(state.walkAnimationState, state.ageInTicks);
        this.blockToEntityAnimation.apply(state.blockToEntityAnimationState, state.ageInTicks);
        this.entityToBlockAnimation.apply(state.entityToBlockAnimationState, state.ageInTicks);
    }
}