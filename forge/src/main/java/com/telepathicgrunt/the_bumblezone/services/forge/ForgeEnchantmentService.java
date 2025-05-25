package com.telepathicgrunt.the_bumblezone.services.forge;

import com.telepathicgrunt.the_bumblezone.services.EnchantmentService;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class ForgeEnchantmentService implements EnchantmentService {
    public boolean canApplyAtEnchantingTable(Enchantment enchantment, ItemStack stack) {
        return enchantment.canApplyAtEnchantingTable(stack);
    }

    public boolean isAllowedOnBooks(Enchantment enchantment) {
        return enchantment.isAllowedOnBooks();
    }
}
