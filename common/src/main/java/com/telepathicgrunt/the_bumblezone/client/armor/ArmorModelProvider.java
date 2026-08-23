package com.telepathicgrunt.the_bumblezone.client.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;

public interface ArmorModelProvider {

    Map<Item, ArmorModelProvider> PROVIDERS = new IdentityHashMap<>();

    @ApiStatus.Internal
    static void register(Item item, ArmorModelProvider provider) {
        PROVIDERS.put(item, provider);
    }

    static ArmorModelProvider get(Item item) {
        return PROVIDERS.getOrDefault(item, (_, _, original) -> original);
    }

    default Identifier getArmorTexture(ItemStack stack) {
        return Identifier.fromNamespaceAndPath("minecraft", "textures/models/armor/leather_layer_1.png");
    }

    @Nullable HumanoidModel<? super HumanoidRenderState> getModel(ItemStack stack, @Nullable EquipmentSlot equipmentSlot, HumanoidModel<? super HumanoidRenderState> original);

    default @Nullable Model<? super HumanoidRenderState> getFinalModel(ItemStack stack, @Nullable EquipmentSlot equipmentSlot, HumanoidModel<? super HumanoidRenderState> original) {
        HumanoidModel<? super HumanoidRenderState> replacement = this.getModel(stack, equipmentSlot, original);
        if(replacement == null) {
            return null;
        }

        if (replacement != original) {
            copyPropertiesTo(original, replacement);
            return replacement;
        } else {
            return original;
        }
    }

    static void copyPropertiesTo(HumanoidModel<?> original, HumanoidModel<?> replacement) {
        replacement.head.visible = original.head.visible;
        replacement.hat.visible = original.hat.visible;
        replacement.body.visible = original.body.visible;
        replacement.rightArm.visible = original.rightArm.visible;
        replacement.leftArm.visible = original.leftArm.visible;
        replacement.rightLeg.visible = original.rightLeg.visible;
        replacement.leftLeg.visible = original.leftLeg.visible;
    }
}
