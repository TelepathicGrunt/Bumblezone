package com.telepathicgrunt.the_bumblezone.client.rendering.cosmiccrystal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.living.CosmicCrystalEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CosmicCrystalModel<T extends CosmicCrystalEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Bumblezone.MODID, "cosmic_crystal"), "main");

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart spikes;
    private final ModelPart charging;

    public CosmicCrystalModel(ModelPart root) {
        super(RenderType::entityTranslucent);

        this.root = root;
        this.body = root.getChild("body");
        this.spikes = root.getChild("laser").getChild("spikes");
        this.charging = root.getChild("laser").getChild("charging");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -0.15F));

        PartDefinition bottom = body.addOrReplaceChild("bottom", CubeListBuilder.create(), PartPose.offset(-0.5F, -5.0F, -0.75F));

        PartDefinition insideB = bottom.addOrReplaceChild("insideB", CubeListBuilder.create(), PartPose.offset(0.6207F, 1.4229F, 0.783F));

        PartDefinition insideBottom_r1 = insideB.addOrReplaceChild("insideBottom_r1", CubeListBuilder.create().texOffs(18, 38).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(-1.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.1074F, -0.4245F, 0.6825F));

        PartDefinition outsideB = bottom.addOrReplaceChild("outsideB", CubeListBuilder.create(), PartPose.offset(0.4954F, 1.2721F, 0.9316F));

        PartDefinition outsideBottom_r1 = outsideB.addOrReplaceChild("outsideBottom_r1", CubeListBuilder.create().texOffs(24, 8).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.1074F, -0.4245F, 0.6825F));

        PartDefinition top = body.addOrReplaceChild("top", CubeListBuilder.create(), PartPose.offset(-0.5F, -29.0F, -0.75F));

        PartDefinition insideT = top.addOrReplaceChild("insideT", CubeListBuilder.create(), PartPose.offset(0.6207F, 1.4229F, 0.783F));

        PartDefinition insideTop_r1 = insideT.addOrReplaceChild("insideTop_r1", CubeListBuilder.create().texOffs(26, 26).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(-1.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.1074F, -0.4245F, 0.6825F));

        PartDefinition outsideT = top.addOrReplaceChild("outsideT", CubeListBuilder.create(), PartPose.offset(0.4954F, 1.2721F, 0.9316F));

        PartDefinition outsideTop_r1 = outsideT.addOrReplaceChild("outsideTop_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.1074F, -0.4245F, 0.6825F));

        PartDefinition middle = body.addOrReplaceChild("middle", CubeListBuilder.create(), PartPose.offset(-0.5F, -17.0F, -0.75F));

        PartDefinition insideM = middle.addOrReplaceChild("insideM", CubeListBuilder.create(), PartPose.offset(0.6207F, 1.4229F, 0.533F));

        PartDefinition insideMiddle_r1 = insideM.addOrReplaceChild("insideMiddle_r1", CubeListBuilder.create().texOffs(0, 32).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(1.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.1074F, -0.4245F, 0.6825F));

        PartDefinition outsideM = middle.addOrReplaceChild("outsideM", CubeListBuilder.create(), PartPose.offset(0.4954F, 1.2721F, 0.9316F));

        PartDefinition outsideMiddle_r1 = outsideM.addOrReplaceChild("outsideMiddle_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(3.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.1074F, -0.4245F, 0.6825F));

        PartDefinition laser = partdefinition.addOrReplaceChild("laser", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition spikes = laser.addOrReplaceChild("spikes", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition spike4_r1 = spikes.addOrReplaceChild("spike4_r1", CubeListBuilder.create().texOffs(30, 58).addBox(-5.0F, 1.0F, -2.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 1.0472F, -1.5708F));

        PartDefinition spike3_r1 = spikes.addOrReplaceChild("spike3_r1", CubeListBuilder.create().texOffs(30, 58).addBox(-5.0F, 1.0F, -2.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, -2.618F));

        PartDefinition spike2_r1 = spikes.addOrReplaceChild("spike2_r1", CubeListBuilder.create().texOffs(30, 58).addBox(-5.0F, 1.0F, -2.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, -1.0472F, -1.5708F));

        PartDefinition spike1_r1 = spikes.addOrReplaceChild("spike1_r1", CubeListBuilder.create().texOffs(30, 58).addBox(-5.0F, 1.0F, -2.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition charging = laser.addOrReplaceChild("charging", CubeListBuilder.create().texOffs(-10, 50).addBox(-5.0F, 3.0F, -5.0F, 10.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }


    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.body.visible = true;
        this.body.getAllParts().forEach(ModelPart::resetPose);

        boolean isLaserState = CosmicCrystalEntity.isLaserState(entity.getCosmicCrystalState());

        if (isLaserState && entity.isLaserFiring()) {
            this.spikes.visible = true;
            this.charging.visible = false;
            this.spikes.getAllParts().forEach(ModelPart::resetPose);

            float pulse = Math.abs(Mth.sin((entity.currentStateTimeTick % 360) * 6 * Mth.DEG_TO_RAD)) + 1.5f;
            this.spikes.xScale = pulse;
            this.spikes.zScale = pulse;
        }
        else if (isLaserState && entity.currentStateTimeTick > entity.getLaserStartDelay()) {
            this.spikes.visible = false;
            this.charging.visible = true;
            this.charging.getAllParts().forEach(ModelPart::resetPose);

            float durationToFiring = entity.getLaserFireStartTime() - entity.getLaserStartDelay();
            float chargeTime = (1 - ((entity.currentStateTimeTick - entity.getLaserStartDelay()) / durationToFiring)) * 3f;
            this.charging.xScale = chargeTime;
            this.charging.zScale = chargeTime;
        }
        else {
            this.charging.visible = false;
            this.spikes.visible = false;
        }

        this.animate(entity.idleAnimationState, CosmicCrystalAnimation.MODEL_IDLE, ageInTicks);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
