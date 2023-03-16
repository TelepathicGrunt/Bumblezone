package com.telepathicgrunt.the_bumblezone.mixins.forge.client;

import com.telepathicgrunt.the_bumblezone.client.armor.ArmorModelProvider;
import com.telepathicgrunt.the_bumblezone.client.forge.ForgeArmorProviders;
import com.telepathicgrunt.the_bumblezone.items.BzArmor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Consumer;

@Mixin(BzArmor.class)
public class BzArmorMixin extends ArmorItem {

    @Unique
    private ArmorModelProvider bz$armorModelProvider;

    public BzArmorMixin(ArmorMaterial arg, ArmorItem.Type arg2, Properties arg3) {
        super(arg, arg2, arg3);
    }

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if (bz$armorModelProvider == null) {
            bz$armorModelProvider = ArmorModelProvider.get(stack.getItem());
        }
        return bz$armorModelProvider.getArmorTexture(entity, stack, slot, type);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            private ArmorModelProvider provider;

            @Override
            public @NotNull Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (provider == null) {
                    provider = ForgeArmorProviders.get(itemStack.getItem());
                }
                return provider.getFinalModel(livingEntity, itemStack, equipmentSlot, original);
            }
        });
    }
}
