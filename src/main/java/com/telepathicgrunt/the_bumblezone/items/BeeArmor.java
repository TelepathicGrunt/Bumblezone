package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.rendering.beearmor.BeeArmorModel;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;


public abstract class BeeArmor extends TickingArmorItem {
    private final int variant;
    private final boolean transTexture;

    public BeeArmor(ArmorMaterial material, EquipmentSlot slot, Properties properties, int variant, boolean transTexture) {
        super(material, slot, properties);
        this.variant = variant;
        this.transTexture = transTexture;
    }

    public int getVariant() {
        return variant;
    }

    public String getArmorTexture() {
        if (transTexture) {
            return Bumblezone.MODID + ":textures/models/armor/trans_bee_material_layer_" + variant + ".png";
        }
        else {
            return Bumblezone.MODID + ":textures/models/armor/bee_material_layer_" + variant + ".png";
        }
    }

    public Runnable registerRenderer() {

        Item itemInstance = this;
        return new Runnable() {
            private BeeArmorModel cachedModel = null;
            private int cachedVariant = 1;
            private ResourceLocation cachedTexture = null;
            @Override
            public void run() {
                ArmorRenderer.register((matrices, vertexConsumers, itemStack, entityLiving, armorSlot, light, modelIn) -> {
                    if (cachedModel == null || (itemStack.getItem() instanceof BeeArmor beeArmor && cachedVariant != beeArmor.getVariant())) {

                        ModelPart layer = null;
                        if(itemStack.getItem() instanceof BeeArmor beeArmor) {
                            int newVariant = beeArmor.getVariant();
                            if(newVariant == 1) {
                                layer = Minecraft.getInstance().getEntityModels().bakeLayer(BeeArmorModel.VARIANT_1_LAYER_LOCATION);
                            }
                            else if(newVariant == 2) {
                                layer = Minecraft.getInstance().getEntityModels().bakeLayer(BeeArmorModel.VARIANT_2_LAYER_LOCATION);
                            }
                            cachedVariant = newVariant;
                        }

                        if(layer == null) {
                            layer = Minecraft.getInstance().getEntityModels().bakeLayer(BeeArmorModel.VARIANT_1_LAYER_LOCATION);
                        }

                        cachedModel = new BeeArmorModel(layer, armorSlot, entityLiving);
                        cachedTexture = new ResourceLocation(getArmorTexture());
                    }
                    cachedModel.entityLiving = entityLiving;
                    modelIn.copyPropertiesTo(cachedModel);
                    ArmorRenderer.renderPart(matrices, vertexConsumers, light, itemStack, cachedModel, cachedTexture);
                }, itemInstance);
            }
        };
    }
}