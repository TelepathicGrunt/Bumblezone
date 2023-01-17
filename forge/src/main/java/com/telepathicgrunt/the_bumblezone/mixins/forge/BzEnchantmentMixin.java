package com.telepathicgrunt.the_bumblezone.mixins.forge;

import com.telepathicgrunt.the_bumblezone.platform.BzEnchantment;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BzEnchantment.class)
public abstract class BzEnchantmentMixin extends Enchantment {

    protected BzEnchantmentMixin(Rarity arg, EnchantmentCategory arg2, EquipmentSlot[] args) {
        super(arg, arg2, args);
    }

    @Shadow
    abstract OptionalBoolean bz$canApplyAtEnchantingTable(ItemStack stack);

    @Override
    public boolean canApplyAtEnchantingTable(@NotNull ItemStack stack) {
        return this.bz$canApplyAtEnchantingTable(stack)
                .orElseGet(() -> super.canApplyAtEnchantingTable(stack));
    }
}
