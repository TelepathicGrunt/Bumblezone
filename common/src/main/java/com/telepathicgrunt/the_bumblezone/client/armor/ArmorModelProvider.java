package com.telepathicgrunt.the_bumblezone.client.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.IdentityHashMap;
import java.util.Map;

public interface ArmorModelProvider {

    ArmorModelProvider DEFAULT = (entity, stack, slot, original) -> original;

    Map<Item, ArmorModelProvider> PROVIDERS = new IdentityHashMap<>();

    @ApiStatus.Internal
    static void register(Item item, ArmorModelProvider provider) {
        PROVIDERS.put(item, provider);
    }

    static ArmorModelProvider get(Item item) {
        return PROVIDERS.getOrDefault(item, DEFAULT);
    }

    default String getArmorTexture(Entity entity, ItemStack stack, EquipmentSlot slot, String type) {
        return "minecraft:textures/models/armor/leather_layer_1.png";
    }

    @NotNull HumanoidModel<?> getModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original);

    default @NotNull Model getFinalModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
        HumanoidModel<?> replacement = this.getModel(entity, stack, slot, original);
        if (replacement != original) {
            copyPropertiesTo(original, replacement);
            return replacement;
        } else {
            return original;
        }
    }

    @SuppressWarnings("unchecked")
    static <T extends LivingEntity> void copyPropertiesTo(HumanoidModel<T> original, HumanoidModel<?> replacement) {
        original.copyPropertiesTo((HumanoidModel<T>)replacement);
        replacement.head.visible = original.head.visible;
        replacement.hat.visible = original.hat.visible;
        replacement.body.visible = original.body.visible;
        replacement.rightArm.visible = original.rightArm.visible;
        replacement.leftArm.visible = original.leftArm.visible;
        replacement.rightLeg.visible = original.rightLeg.visible;
        replacement.leftLeg.visible = original.leftLeg.visible;
    }
}
