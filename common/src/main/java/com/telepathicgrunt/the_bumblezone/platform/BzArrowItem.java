package com.telepathicgrunt.the_bumblezone.platform;

import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;

public class BzArrowItem extends ArrowItem {

    public BzArrowItem(Properties properties) {
        super(properties);
    }

    public OptionalBoolean bz$isInfinite(ItemStack stack, ItemStack bow, Player player) {
        return OptionalBoolean.empty();
    }
}
