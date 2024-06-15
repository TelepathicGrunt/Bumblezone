package com.telepathicgrunt.the_bumblezone.client.armor;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.rendering.armor.BeeArmorModel;
import com.telepathicgrunt.the_bumblezone.items.BeeArmor;
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

public class BeeArmorModelProvider implements ArmorModelProvider {

    private BeeArmorModel model;
    private int variant;

    @Override
    public ResourceLocation getArmorTexture(Entity entity, ItemStack stack, EquipmentSlot slot, ArmorMaterial.Layer type) {
        if (stack.getItem() instanceof BeeArmor beeArmor && beeArmor.hasTransTexture()) {
            return ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "textures/models/armor/trans_bee_material_layer_" + variant + ".png");
        }
        else {
            return ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "textures/models/armor/bee_material_layer_" + variant + ".png");
        }
    }

    @Override
    public @NotNull HumanoidModel<?> getModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
        if (this.model == null || (stack.getItem() instanceof BeeArmor beeArmor && variant != beeArmor.getVariant())) {

            ModelPart layer = null;
            if(stack.getItem() instanceof BeeArmor beeArmor) {
                int newVariant = beeArmor.getVariant();
                if(newVariant == 1) {
                    layer = Minecraft.getInstance().getEntityModels().bakeLayer(BeeArmorModel.VARIANT_1_LAYER_LOCATION);
                }
                else if(newVariant == 2) {
                    layer = Minecraft.getInstance().getEntityModels().bakeLayer(BeeArmorModel.VARIANT_2_LAYER_LOCATION);
                }
                this.variant = newVariant;
            }

            if(layer == null) {
                layer = Minecraft.getInstance().getEntityModels().bakeLayer(BeeArmorModel.VARIANT_1_LAYER_LOCATION);
            }

            this.model = new BeeArmorModel(layer, slot, entity);
        }
        model.entityLiving = entity;
        return this.model;
    }
}
