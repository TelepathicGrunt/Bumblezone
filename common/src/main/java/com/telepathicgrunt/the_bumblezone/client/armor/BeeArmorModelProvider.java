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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
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
        switch (equipmentSlot) {
            case HEAD -> layerIndex = 0;
            case CHEST -> layerIndex = 1;
            case LEGS -> layerIndex = 2;
            case FEET -> layerIndex = 3;
            case null, default -> {
                Item item = stack.getItem();
                switch (item) {
                    case StinglessBeeHelmet ignored -> layerIndex = 0;
                    case BumbleBeeChestplate ignored -> layerIndex = 1;
                    case HoneyBeeLeggings ignored -> layerIndex = 2;
                    case CarpenterBeeBoots ignored -> layerIndex = 3;
                    default -> {}
                }
            }
        }
        return layerIndex;
    }
}
