package com.telepathicgrunt.the_bumblezone.platform;

import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;

public interface ItemExtension {

    @ApiStatus.Internal
    default Item getBzItem() {
        return (Item) this;
    }

    default int bz$getMaxDamage(ItemStack stack) {
        return getBzItem().getMaxDamage();
    }

    default void bz$setDamage(ItemStack stack, int damage) {
        stack.getOrCreateTag().putInt("Damage", Math.max(0, damage));
    }

    default OptionalBoolean bz$canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return OptionalBoolean.EMPTY;
    }

    default EquipmentSlot bz$getEquipmentSlot(ItemStack stack) {
        if (stack.getItem() instanceof ArmorItem) {
            return ((ArmorItem)stack.getItem()).getEquipmentSlot();
        }

        return EquipmentSlot.MAINHAND;
    }

    default void bz$onArmorTick(ItemStack itemstack, Level world, Player player) { }

    default boolean bz$canPerformAction(ItemStack stack, String toolAction) {
        return false;
    }
}
