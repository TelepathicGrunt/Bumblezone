package com.telepathicgrunt.the_bumblezone.mixins.fabric;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.telepathicgrunt.the_bumblezone.platform.BzEnchantment;
import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @WrapOperation(
            method = "getAvailableEnchantmentResults",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentCategory;canEnchant(Lnet/minecraft/world/item/Item;)Z")
    )
    private static boolean bumblezone$canEnchant(EnchantmentCategory instance, Item item, Operation<Boolean> operation, int i, ItemStack stack, @Local(ordinal = 0, name = "enchantment") Enchantment enchantment) {
        if (enchantment instanceof BzEnchantment bzEnchantment) {
            OptionalBoolean result = bzEnchantment.bz$canApplyAtEnchantingTable(stack);
            if (result.isPresent()) {
                return result.get();
            }
        }
        if (item instanceof ItemExtension extension) {
            OptionalBoolean result = extension.bz$canApplyAtEnchantingTable(stack, enchantment);
            if (result.isPresent()) {
                return result.get();
            }
        }
        return operation.call(instance, item);
    }
}