package com.telepathicgrunt.the_bumblezone.events.client;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.level.ItemLike;

public record RegisterItemColorEvent(ItemColors colors, BlockColors blockColors) {

    public static final EventHandler<RegisterItemColorEvent> EVENT = new EventHandler<>();

    public void register(ItemColor color, ItemLike... items) {
        colors.register(color, items);
    }
}
