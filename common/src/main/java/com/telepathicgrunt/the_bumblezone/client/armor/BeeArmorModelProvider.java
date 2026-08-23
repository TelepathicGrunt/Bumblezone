package com.telepathicgrunt.the_bumblezone.client.armor;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.rendering.armor.BeeArmorModel;
import com.telepathicgrunt.the_bumblezone.items.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class BeeArmorModelProvider implements ArmorModelProvider {

    private BeeArmorModel model;
    private int variant;

    @Override
    public Identifier getArmorTexture(ItemStack stack) {
        if (stack.getItem() instanceof BeeArmor beeArmor && beeArmor.hasTransTexture()) {
            return Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/models/armor/trans_bee_material_layer_" + variant + ".png");
        }
        else {
            return Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/models/armor/bee_material_layer_" + variant + ".png");
        }
    }

    @Override
    public  @Nullable HumanoidModel<? super HumanoidRenderState> getModel(ItemStack stack, @Nullable EquipmentSlot equipmentSlot, HumanoidModel<? super HumanoidRenderState> original) {
        if (this.model == null || (stack.getItem() instanceof BeeArmor beeArmor && variant != beeArmor.getVariant())) {

            int layerIndex = getLayerIndex(stack, equipmentSlot);
            if (layerIndex == -1) {
                return null;
            }

            ModelPart layer = null;
            if (stack.getItem() instanceof BeeArmor beeArmor) {
                int newVariant = beeArmor.getVariant();
                if (newVariant == 1) {
                    layer = Minecraft.getInstance().getEntityModels().bakeLayer(BeeArmorModel.VARIANT_1_ARMOR_LAYER_LOCATIONS.get(layerIndex));
                }
                else if (newVariant == 2) {
                    layer = Minecraft.getInstance().getEntityModels().bakeLayer(BeeArmorModel.VARIANT_2_ARMOR_LAYER_LOCATIONS.get(layerIndex));
                }
                this.variant = newVariant;
            }

            if (layer == null) {
                layer = Minecraft.getInstance().getEntityModels().bakeLayer(BeeArmorModel.VARIANT_1_ARMOR_LAYER_LOCATIONS.get(layerIndex));
            }

            this.model = new BeeArmorModel(layer);
        }
        return this.model;
    }

    private static int getLayerIndex(ItemStack stack, @org.jspecify.annotations.Nullable EquipmentSlot equipmentSlot) {
        int layerIndex = -1;
        switch (equipmentSlot) {
            case HEAD -> layerIndex = 0;
            case CHEST -> layerIndex = 1;
            case LEGS -> layerIndex = 2;
            case FEET -> layerIndex = 3;
            case null, default -> {
                Item item = stack.getItem();
                switch (item) {
                    case StinglessBeeHelmet _ -> layerIndex = 0;
                    case BumbleBeeChestplate _ -> layerIndex = 1;
                    case HoneyBeeLeggings _ -> layerIndex = 2;
                    case CarpenterBeeBoots _ -> layerIndex = 3;
                    default -> {}
                }
            }
        }
        return layerIndex;
    }
}
