package com.telepathicgrunt.the_bumblezone.client.rendering.sentrywatcher;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.SentryWatcherEntity;
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

public class SentryWatcherModel extends EntityModel<SentryWatcherEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Bumblezone.MODID, "sentry_watcher"), "main");
    private final ModelPart root;

    public SentryWatcherModel(ModelPart root) {
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 12).mirror().addBox(-16.0F, -5.0F, -12.0F, 3.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 12).addBox(13.0F, -5.0F, -12.0F, 3.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).addBox(13.0F, -5.0F, -3.0F, 3.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).addBox(13.0F, -5.0F, 7.0F, 3.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).mirror().addBox(-16.0F, -5.0F, 7.0F, 3.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 12).mirror().addBox(-16.0F, -5.0F, -3.0F, 3.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).addBox(-13.0F, -22.0F, -20.0F, 26.0F, 22.0F, 40.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition antenna_right = root.addOrReplaceChild("antenna_right", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -7.0F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -22.0F, -17.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition antenna_left = root.addOrReplaceChild("antenna_left", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -7.0F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -22.0F, -17.0F, -0.2182F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(SentryWatcherEntity entity, float f, float g, float h, float i, float j) {

    }
}