package com.telepathicgrunt.the_bumblezone.client.rendering.honeycrystalshard;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.client.model.Model;
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

public class HoneyCrystalShardModel extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Bumblezone.MODID, "honey_crystal_shard"), "main");
    private final ModelPart root;

    public HoneyCrystalShardModel(ModelPart modelPart) {
        super(RenderType::entityTranslucent);
        this.root = modelPart;
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0f, 0.0F));

        main.addOrReplaceChild("9_r1", CubeListBuilder.create().texOffs(4, 15).addBox(-0.75F, 0.0F, 3.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 8).addBox(-0.75F, -1.0F, 2.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 0).addBox(-0.75F, -2.0F, 1.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 34).addBox(-0.75F, -3.0F, 0.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 26).addBox(-0.75F, -4.0F, -1.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 18).addBox(-0.75F, -5.0F, -2.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(-0.75F, -5.0F, -3.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 5).addBox(-0.75F, -5.0F, -4.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 21).addBox(-0.75F, 1.0F, 4.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-0.75F, -5.0F, -5.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 1.5708F, 1.5708F));

        return LayerDefinition.create(meshdefinition, 8, 42);
    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}