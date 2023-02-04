package com.telepathicgrunt.the_bumblezone.utils.fabric;

import com.telepathicgrunt.the_bumblezone.platform.BzEnchantment;
import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.quiltmc.qsl.block.content.registry.api.BlockContentRegistries;

public class EnchantmentUtilsImpl {
    public static boolean canApplyAtEnchantingTable(Enchantment enchantment, ItemStack stack) {
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

    public static boolean isAllowedOnBooks(Enchantment enchantment) {
        return true;
    }

    public static float getEnchantPower(Level world, BlockPos pos) {
        final BlockState state = world.getBlockState(pos);
        final Block block = world.getBlockState(pos).getBlock();
        return BlockContentRegistries.ENCHANTING_BOOSTERS.get(block)
                .map(booster -> booster.getEnchantingBoost(world, state, pos))
                .orElse(0f);
    }
}
