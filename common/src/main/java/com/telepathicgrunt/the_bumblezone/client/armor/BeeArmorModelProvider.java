package com.telepathicgrunt.the_bumblezone.client.armor;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.rendering.armor.BeeArmorModel;
import com.telepathicgrunt.the_bumblezone.items.BeeArmor;
import com.telepathicgrunt.the_bumblezone.items.BumbleBeeChestplate;
import com.telepathicgrunt.the_bumblezone.items.CarpenterBeeBoots;
import com.telepathicgrunt.the_bumblezone.items.HoneyBeeLeggings;
import com.telepathicgrunt.the_bumblezone.items.StinglessBeeHelmet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BeeArmorModelProvider implements ArmorModelProvider {

    private BeeArmorModel model;
    private int variant;

    @Override
    public String getArmorTexture(Entity entity, ItemStack stack, EquipmentSlot slot, String type) {
        if (stack.getItem() instanceof BeeArmor beeArmor && beeArmor.hasTransTexture()) {
            return Bumblezone.MODID + ":textures/models/armor/trans_bee_material_layer_" + variant + ".png";
        }
        else {
            return Bumblezone.MODID + ":textures/models/armor/bee_material_layer_" + variant + ".png";
        }
    }

    @Override
    public HumanoidModel<?> getModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
        if (this.model == null || (stack.getItem() instanceof BeeArmor beeArmor && variant != beeArmor.getVariant())) {

            int layerIndex = getLayerIndex(stack, slot);
            if (layerIndex == -1) {
                return null;
            }

            ModelPart layer = null;
            if(stack.getItem() instanceof BeeArmor beeArmor) {
                int newVariant = beeArmor.getVariant();
                if(newVariant == 1) {
                    layer = Minecraft.getInstance().getEntityModels().bakeLayer(BeeArmorModel.VARIANT_1_ARMOR_LAYER_LOCATIONS.get(layerIndex));
                }
                else if(newVariant == 2) {
                    layer = Minecraft.getInstance().getEntityModels().bakeLayer(BeeArmorModel.VARIANT_2_ARMOR_LAYER_LOCATIONS.get(layerIndex));
                }
                this.variant = newVariant;
            }

            if(layer == null) {
                layer = Minecraft.getInstance().getEntityModels().bakeLayer(BeeArmorModel.VARIANT_1_ARMOR_LAYER_LOCATIONS.get(layerIndex));
            }

            this.model = new BeeArmorModel(layer, slot, entity);
        }
        model.entityLiving = entity;
        return this.model;
    }

    private static int getLayerIndex(ItemStack stack, EquipmentSlot equipmentSlot) {
        int layerIndex = -1;
        if (equipmentSlot == null) {
            return getLayerIndexByItem(stack, layerIndex);
        }

        switch (equipmentSlot) {
            case HEAD -> layerIndex = 0;
            case CHEST -> layerIndex = 1;
            case LEGS -> layerIndex = 2;
            case FEET -> layerIndex = 3;
            default -> layerIndex = getLayerIndexByItem(stack, layerIndex);
        }
        return layerIndex;
    }

    private static int getLayerIndexByItem(ItemStack stack, int layerIndex) {
        Item item = stack.getItem();

        if (item instanceof StinglessBeeHelmet) {
            layerIndex = 0;
        }
        else if (item instanceof BumbleBeeChestplate) {
            layerIndex = 1;
        }
        else if (item instanceof HoneyBeeLeggings) {
            layerIndex = 2;
        }
        else if (item instanceof CarpenterBeeBoots) {
            layerIndex = 3;
        }
        return layerIndex;
    }
}
