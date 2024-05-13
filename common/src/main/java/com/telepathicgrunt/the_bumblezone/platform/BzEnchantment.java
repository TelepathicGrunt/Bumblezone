package com.telepathicgrunt.the_bumblezone.platform;

import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class BzEnchantment extends Enchantment {

    public BzEnchantment(Enchantment.EnchantmentDefinition enchantmentDefinition) {
        super(enchantmentDefinition);
    }

    public OptionalBoolean bz$canApplyAtEnchantingTable(ItemStack stack) {
        return OptionalBoolean.EMPTY;
    }
}
