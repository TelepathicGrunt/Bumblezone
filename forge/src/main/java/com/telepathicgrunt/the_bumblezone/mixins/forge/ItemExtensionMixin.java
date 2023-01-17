package com.telepathicgrunt.the_bumblezone.mixins.forge;

import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.extensions.IForgeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemExtension.class)
public interface ItemExtensionMixin extends IForgeItem {

    @Shadow
    int bz$getMaxDamage(ItemStack stack);

    @Shadow
    void bz$setDamage(ItemStack stack, int damage);

    @Shadow
    OptionalBoolean bz$canApplyAtEnchantingTable(ItemStack stack, net.minecraft.world.item.enchantment.Enchantment enchantment);

    @Override
    default int getMaxDamage(ItemStack stack) {
        return this.bz$getMaxDamage(stack);
    }

    @Override
    default void setDamage(ItemStack stack, int damage) {
        this.bz$setDamage(stack, damage);
    }

    @Override
    default boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return this.bz$canApplyAtEnchantingTable(stack, enchantment)
                .orElseGet(() -> IForgeItem.super.canApplyAtEnchantingTable(stack, enchantment));
    }
}
