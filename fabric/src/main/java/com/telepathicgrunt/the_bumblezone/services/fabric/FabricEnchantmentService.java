package com.telepathicgrunt.the_bumblezone.services.fabric;

import com.telepathicgrunt.the_bumblezone.platform.BzEnchantment;
import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import com.telepathicgrunt.the_bumblezone.services.EnchantmentService;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class FabricEnchantmentService implements EnchantmentService {
    public boolean canApplyAtEnchantingTable(Enchantment enchantment, ItemStack stack) {
        if (enchantment instanceof BzEnchantment bzEnchantment) {
            OptionalBoolean result = bzEnchantment.bz$canApplyAtEnchantingTable(stack);
            if (result.isPresent()) {
                return result.get();
            }
        }
        if (stack.getItem() instanceof ItemExtension extension) {
            OptionalBoolean result = extension.bz$canApplyAtEnchantingTable(stack, enchantment);
            if (result.isPresent()) {
                return result.get();
            }
        }
        return enchantment.category.canEnchant(stack.getItem());
    }

    public boolean isAllowedOnBooks(Enchantment enchantment) {
        return true;
    }
}
