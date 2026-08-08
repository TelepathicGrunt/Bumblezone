package com.telepathicgrunt.the_bumblezone.client.rendering.armor;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class FlowerHeadwearModel extends HumanoidModel<HumanoidRenderState> {
    public static final ModelLayerLocation FLOWER_HEADWEAR_LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "flower_headwear"), "flower_headwear");

    public final ModelPart head;

    public FlowerHeadwearModel(ModelPart part) {
        super(part);
        this.head = root.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-2.5F, -11.75F, -3F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(3.0F, -9.25F, -3F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(3.0F, -3.75F, -3F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.5F, -1.75F, -3F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-8.0F, -9.25F, -3F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-8.0F, -3.75F, -3F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 0.0F));

        head.addOrReplaceChild("hat", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0.0F)), PartPose.offset(0, 0, 0.0F));
        root.addOrReplaceChild("body", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0.0F)), PartPose.offset(0, 0, 0.0F));
        root.addOrReplaceChild("right_arm", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0.0F)), PartPose.offset(0, 0, 0.0F));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0.0F)), PartPose.offset(0, 0, 0.0F));
        root.addOrReplaceChild("right_leg", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0.0F)), PartPose.offset(0, 0, 0.0F));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create().addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0.0F)), PartPose.offset(0, 0, 0.0F));

        return LayerDefinition.create(mesh, 16, 16);
    }

    @Override
    public ModelPart getHead() {
        return this.head;
    }
}