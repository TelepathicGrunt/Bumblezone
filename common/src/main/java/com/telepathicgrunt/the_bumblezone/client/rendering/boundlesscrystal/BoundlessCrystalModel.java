package com.telepathicgrunt.the_bumblezone.client.rendering.boundlesscrystal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.rendering.rootmin.RootminAnimations;
import com.telepathicgrunt.the_bumblezone.entities.living.BoundlessCrystalEntity;
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
import org.joml.Vector3f;

public class BoundlessCrystalModel<T extends BoundlessCrystalEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Bumblezone.MODID, "boundless_crystal"), "main");

    private final ModelPart bottom;
    private final ModelPart middle;
    private final ModelPart top;
    private final ModelPart root;

    public BoundlessCrystalModel(ModelPart root) {
        super(RenderType::entityTranslucent);

        this.root = root;
        this.bottom = root.getChild("bottom");
        this.middle = root.getChild("middle");
        this.top = root.getChild("top");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bottom = partdefinition.addOrReplaceChild("bottom", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition insideB = bottom.addOrReplaceChild("insideB", CubeListBuilder.create(), PartPose.offset(0.6207F, 1.4229F, 0.783F));

        PartDefinition insideBottom_r1 = insideB.addOrReplaceChild("insideBottom_r1", CubeListBuilder.create().texOffs(18, 38).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(-1.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.1074F, -0.4245F, 0.6825F));

        PartDefinition outsideB = bottom.addOrReplaceChild("outsideB", CubeListBuilder.create(), PartPose.offset(0.4954F, 1.2721F, 0.9316F));

        PartDefinition outsideBottom_r1 = outsideB.addOrReplaceChild("outsideBottom_r1", CubeListBuilder.create().texOffs(24, 8).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.1074F, -0.4245F, 0.6825F));

        PartDefinition middle = partdefinition.addOrReplaceChild("middle", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition insideM = middle.addOrReplaceChild("insideM", CubeListBuilder.create(), PartPose.offset(0.6207F, 1.4229F, 0.533F));

        PartDefinition insideMiddle_r1 = insideM.addOrReplaceChild("insideMiddle_r1", CubeListBuilder.create().texOffs(0, 32).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(1.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.1074F, -0.4245F, 0.6825F));

        PartDefinition outsideM = middle.addOrReplaceChild("outsideM", CubeListBuilder.create(), PartPose.offset(0.4954F, 1.2721F, 0.9316F));

        PartDefinition outsideMiddle_r1 = outsideM.addOrReplaceChild("outsideMiddle_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(3.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.1074F, -0.4245F, 0.6825F));

        PartDefinition top = partdefinition.addOrReplaceChild("top", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition insideT = top.addOrReplaceChild("insideT", CubeListBuilder.create(), PartPose.offset(0.6207F, 1.4229F, 0.783F));

        PartDefinition insideTop_r1 = insideT.addOrReplaceChild("insideTop_r1", CubeListBuilder.create().texOffs(26, 26).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(-1.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.1074F, -0.4245F, 0.6825F));

        PartDefinition outsideT = top.addOrReplaceChild("outsideT", CubeListBuilder.create(), PartPose.offset(0.4954F, 1.2721F, 0.9316F));

        PartDefinition outsideTop_r1 = outsideT.addOrReplaceChild("outsideTop_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.1074F, -0.4245F, 0.6825F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.bottom.getAllParts().forEach(ModelPart::resetPose);
        this.middle.getAllParts().forEach(ModelPart::resetPose);
        this.top.getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity.rotateAnimationState, BoundlessCrystalAnimation.MODEL_ROTATE, ageInTicks);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.translate(0, -0.25, 0);
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
