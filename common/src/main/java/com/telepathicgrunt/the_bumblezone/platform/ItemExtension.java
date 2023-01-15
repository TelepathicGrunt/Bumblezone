package com.telepathicgrunt.the_bumblezone.platform;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

public interface ItemExtension {

    @ApiStatus.Internal
    default Item getBzItem() {
        return (Item) this;
    }

    default int getMaxDamage(ItemStack stack) {
        return getBzItem().getMaxDamage();
    }

    default void setDamage(ItemStack stack, int damage) {
        stack.getOrCreateTag().putInt("Damage", Math.max(0, damage));
    }
}
