package com.telepathicgrunt.the_bumblezone.platform;

import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;

public interface ItemExtension {

    @ApiStatus.Internal
    default Item getBzItem() {
        return (Item) this;
    }

    default void bz$setDamage(ItemStack stack, int damage) {
        stack.set(DataComponents.DAMAGE, Mth.clamp(damage, 0, stack.getMaxDamage()));
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
