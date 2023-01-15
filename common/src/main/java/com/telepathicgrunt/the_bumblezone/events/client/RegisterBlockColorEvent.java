package com.telepathicgrunt.the_bumblezone.events.client;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.world.level.block.Block;

public record RegisterBlockColorEvent(BlockColors colors) {

    public static final EventHandler<RegisterBlockColorEvent> EVENT = new EventHandler<>();

    public void register(BlockColor color, Block... blocks) {
        colors.register(color, blocks);
    }
}
