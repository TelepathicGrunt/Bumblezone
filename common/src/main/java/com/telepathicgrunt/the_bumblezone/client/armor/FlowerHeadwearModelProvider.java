package com.telepathicgrunt.the_bumblezone.client.armor;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.rendering.armor.FlowerHeadwearModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FlowerHeadwearModelProvider implements ArmorModelProvider {
    private FlowerHeadwearModel model;

    @Override
    public Identifier getArmorTexture(ItemStack stack) {
        return Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/models/armor/flower_headwear_layer.png");
    }

    @Override
    public @NotNull HumanoidModel<?> getModel(ItemStack stack, @Nullable EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        if (this.model == null) {
            ModelPart layer = Minecraft.getInstance().getEntityModels().bakeLayer(FlowerHeadwearModel.FLOWER_HEADWEAR_LAYER_LOCATION);
            this.model = new FlowerHeadwearModel(layer);
        }
        return this.model;
    }
}
