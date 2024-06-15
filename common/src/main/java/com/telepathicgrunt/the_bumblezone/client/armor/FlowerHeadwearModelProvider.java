package com.telepathicgrunt.the_bumblezone.client.armor;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.rendering.armor.FlowerHeadwearModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FlowerHeadwearModelProvider implements ArmorModelProvider {
    private FlowerHeadwearModel model;

    @Override
    public ResourceLocation getArmorTexture(Entity entity, ItemStack stack, EquipmentSlot slot, ArmorMaterial.Layer type) {
        return ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "textures/models/armor/flower_headwear_layer.png");
    }

    @Override
    public @NotNull HumanoidModel<?> getModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
        if (this.model == null) {
            ModelPart layer = Minecraft.getInstance().getEntityModels().bakeLayer(FlowerHeadwearModel.FLOWER_HEADWEAR_LAYER_LOCATION);
            this.model = new FlowerHeadwearModel(layer, slot, entity);
        }
        model.entityLiving = entity;
        return this.model;
    }
}
