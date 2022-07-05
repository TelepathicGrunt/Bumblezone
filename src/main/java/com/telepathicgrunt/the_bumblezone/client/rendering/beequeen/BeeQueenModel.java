package com.telepathicgrunt.the_bumblezone.client.rendering.beequeen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
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

public class BeeQueenModel extends HierarchicalModel<BeeQueenEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Bumblezone.MODID, "bee_queen"), "main");
    private final ModelPart root;

    public BeeQueenModel(ModelPart root) {
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, 24.0F, 0.0F, 0.0F, -1.9635F, 0.0F));

        PartDefinition segment3 = root.addOrReplaceChild("segment3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Stinger_r1 = segment3.addOrReplaceChild("Stinger_r1", CubeListBuilder.create().texOffs(0, 9).addBox(-2.5F, -1.0F, -0.25F, 4.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -4.5F, 14.0F, 0.0F, 0.4363F, 0.0F));

        PartDefinition body_r1 = segment3.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(0, 65).addBox(-5.5F, -5.49F, -10.0F, 11.0F, 11.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -5.5F, 11.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition segment2 = root.addOrReplaceChild("segment2", CubeListBuilder.create().texOffs(0, 40).addBox(3.0F, -10.0F, -8.0F, 10.0F, 10.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition segment1 = root.addOrReplaceChild("segment1", CubeListBuilder.create(), PartPose.offset(2.8299F, -10.1237F, -5.7857F));

        PartDefinition leftwing = segment1.addOrReplaceChild("leftwing", CubeListBuilder.create(), PartPose.offset(1.6615F, -5.5959F, -2.9868F));

        PartDefinition leftwing_r1 = leftwing.addOrReplaceChild("leftwing_r1", CubeListBuilder.create().texOffs(35, 45).addBox(-1.9582F, 0.1148F, -2.5295F, 10.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4804F, 0.059F, -1.3798F, 2.6042F, 0.6829F, -2.3124F));

        PartDefinition rightwing = segment1.addOrReplaceChild("rightwing", CubeListBuilder.create(), PartPose.offset(3.6F, -5.4777F, 1.1254F));

        PartDefinition rightwing_r1 = rightwing.addOrReplaceChild("rightwing_r1", CubeListBuilder.create().texOffs(32, 21).addBox(-1.0418F, -0.1148F, -8.4705F, 10.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0574F, -0.0681F, 0.0922F, 2.442F, -0.9062F, -1.3675F));

        PartDefinition upperbody = segment1.addOrReplaceChild("upperbody", CubeListBuilder.create(), PartPose.offset(-1.3299F, 1.6237F, 0.7857F));

        PartDefinition body_r2 = upperbody.addOrReplaceChild("body_r2", CubeListBuilder.create().texOffs(0, 17).addBox(-4.35F, -6.5F, -8.0F, 9.0F, 9.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.9199F, 1.1345F, 3.1416F));

        PartDefinition legs2 = segment1.addOrReplaceChild("legs2", CubeListBuilder.create(), PartPose.offset(-3.8699F, 1.7995F, 1.9238F));

        PartDefinition right2_r1 = legs2.addOrReplaceChild("right2_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 12.7F, 1.15F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-3.0F, 12.7F, 8.15F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.04F, -7.6758F, -0.1381F, 2.7286F, -0.1451F, -1.8894F));

        PartDefinition legs1 = segment1.addOrReplaceChild("legs1", CubeListBuilder.create(), PartPose.offset(-2.64F, 4.6186F, 1.2902F));

        PartDefinition right1_r1 = legs1.addOrReplaceChild("right1_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, 12.7F, 1.15F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-6.0F, 12.7F, 8.15F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.8101F, -10.4948F, 0.4955F, 2.7286F, -0.1451F, -1.8894F));

        PartDefinition head = segment1.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(-9.6029F, -6.902F, 4.4631F, -0.0175F, 0.0262F, 0.0F));

        PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -3.5F, -5.0F, 7.0F, 7.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.9236F, 2.1826F, -0.8131F, -2.8195F, 1.1643F, 3.1257F));

        PartDefinition crown_r1 = head.addOrReplaceChild("crown_r1", CubeListBuilder.create().texOffs(34, 0).addBox(-2.5F, -2.0F, -3.5F, 3.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.7897F, -1.8522F, 1.1881F, -3.0061F, -0.3845F, 2.7772F));

        PartDefinition legs3 = head.addOrReplaceChild("legs3", CubeListBuilder.create(), PartPose.offset(1.4945F, 6.019F, -0.5393F));

        PartDefinition right3_r1 = legs3.addOrReplaceChild("right3_r1", CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, 3.5F, 2.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(2, 0).addBox(-3.0F, 3.5F, 2.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6789F, -3.9174F, -0.3982F, -0.3221F, -1.1643F, -0.0159F));

        PartDefinition antenna = head.addOrReplaceChild("antenna", CubeListBuilder.create(), PartPose.offset(-3.0823F, 0.0827F, 1.1882F));

        PartDefinition right_r1 = antenna.addOrReplaceChild("right_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2613F, -3.4864F, 0.1431F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.7907F, 0.9874F, 1.837F, -0.3221F, -1.1643F, -0.0159F));

        PartDefinition left_r1 = antenna.addOrReplaceChild("left_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2613F, -3.4864F, 0.1431F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2093F, 1.0126F, -1.837F, -0.3221F, -1.1643F, -0.0159F));

        return LayerDefinition.create(meshdefinition, 76, 92);
    }

    @Override
    public void setupAnim(BeeQueenEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity.idleAnimationState, BeeQueenAnimations.BEE_QUEEN_IDLE, ageInTicks);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root().render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}