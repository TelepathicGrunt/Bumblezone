package com.telepathicgrunt.the_bumblezone.client.rendering.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.items.FlowerHeadwearColoring;
import com.telepathicgrunt.the_bumblezone.items.FlowerHeadwearHelmet;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

public class FlowerHeadwearModel extends HumanoidModel<LivingEntity> {

    public static final ModelLayerLocation FLOWER_HEADWEAR_LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "flower_headwear"), "flower_headwear");

    protected final EquipmentSlot slot;
    public LivingEntity entityLiving;

    public FlowerHeadwearModel(ModelPart part, EquipmentSlot slot, LivingEntity livingEntity) {
        super(part);
        this.slot = slot;
        this.entityLiving = livingEntity;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int light, int overlay, int packedColor) {
        setAllVisible(false);

        if (slot == EquipmentSlot.HEAD && this.entityLiving != null) {
            head.visible = true;
            hat.visible = true;

            int red;
            int green;
            int blue;
            ItemStack stack = this.entityLiving.getItemBySlot(slot);
            if (stack.getItem() instanceof FlowerHeadwearHelmet flowerHeadwearHelmet)
            {
                int color = DyedItemColor.getOrDefault(stack, FlowerHeadwearColoring.DEFAULT_COLOR);
                red = GeneralUtils.getRed(color);
                green = GeneralUtils.getGreen(color);
                blue = GeneralUtils.getBlue(color);
            }
            else {
                red = GeneralUtils.getRed(FlowerHeadwearColoring.DEFAULT_COLOR);
                green = GeneralUtils.getGreen(FlowerHeadwearColoring.DEFAULT_COLOR);
                blue = GeneralUtils.getBlue(FlowerHeadwearColoring.DEFAULT_COLOR);
            }

            head.render(poseStack, buffer, light, overlay, FastColor.ARGB32.color(red, green, blue, 1));
        }
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