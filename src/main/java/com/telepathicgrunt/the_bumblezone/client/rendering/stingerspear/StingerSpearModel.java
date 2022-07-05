package com.telepathicgrunt.the_bumblezone.client.rendering.stingerspear;

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

// Made with Blockbench 4.1.3
// Exported for Minecraft version 1.17 with Mojang mappings
// Paste this class into your mod and generate all required imports
public class StingerSpearModel extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Bumblezone.MODID, "stinger_spear"), "main");
    private final ModelPart root;

    public StingerSpearModel(ModelPart modelPart) {
        super(RenderType::entitySolid);
        this.root = modelPart;
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("main", CubeListBuilder.create()
                .texOffs(9, 4).addBox(0.5F, -26.0F, 1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(9, 0).addBox(0.5F, -26.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 0).addBox(0.5F, -31.0F, 0.0F, 1.0F, 31.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 32, 32);
    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}