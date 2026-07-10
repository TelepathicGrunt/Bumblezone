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

    public ItemStack itemStack;

    public FlowerHeadwearModel(ModelPart part) {
        super(part);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(new CubeDeformation(1.0F), 0F);
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition armorHead = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-2.5F, -11.75F, -3F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(3.0F, -9.25F, -3F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(3.0F, -3.75F, -3F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.5F, -1.75F, -3F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-8.0F, -9.25F, -3F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-8.0F, -3.75F, -3F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 0.0F));

        PartDefinition armorBody = partdefinition.addOrReplaceChild("armorBody", CubeListBuilder.create(), PartPose.offset(0.0F, 0.25F, 0.0F));

        PartDefinition armorRightArm = partdefinition.addOrReplaceChild("armorRightArm", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition armorLeftArm = partdefinition.addOrReplaceChild("armorLeftArm", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(2.0F, 0.0F, 0.0F));

        PartDefinition right_boot = partdefinition.addOrReplaceChild("right_boot", CubeListBuilder.create(), PartPose.offset(-2.0F, 0.0F, 0.0F));

        PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, 0.0F, 0.0F));

        PartDefinition left_boot = partdefinition.addOrReplaceChild("left_boot", CubeListBuilder.create(), PartPose.offset(2.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }
}