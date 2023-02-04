package com.telepathicgrunt.the_bumblezone.utils.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

public class EnchantmentUtilsImpl {
    public static boolean canApplyAtEnchantingTable(Enchantment enchantment, ItemStack stack) {
        return enchantment.canApplyAtEnchantingTable(stack);
    }

    public static boolean isAllowedOnBooks(Enchantment enchantment) {
        return enchantment.isAllowedOnBooks();
    }

    public static float getEnchantPower(Level world, BlockPos pos) {
        return world.getBlockState(pos).getEnchantPowerBonus(world, pos);
    }
}
