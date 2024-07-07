package com.telepathicgrunt.the_bumblezone.events.client;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;

public record BzRegisterBlockColorEvent(BiConsumer<BlockColor, Block[]> colors) {

    public static final EventHandler<BzRegisterBlockColorEvent> EVENT = new EventHandler<>();

    public void register(BlockColor color, Block... blocks) {
        colors.accept(color, blocks);
    }
}
