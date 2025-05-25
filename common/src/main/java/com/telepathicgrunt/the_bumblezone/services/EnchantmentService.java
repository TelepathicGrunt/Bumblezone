package com.telepathicgrunt.the_bumblezone.services;

import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Contract;

public interface EnchantmentService {

    EnchantmentService INSTANCE = GeneralUtils.loadService(EnchantmentService.class);

    default boolean canApplyAtEnchantingTable(Enchantment enchantment, ItemStack stack) {
        throw new NotImplementedException();
    }

    default boolean isAllowedOnBooks(Enchantment enchantment) {
        throw new NotImplementedException();
    }
}
