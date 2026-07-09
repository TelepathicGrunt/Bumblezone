package com.telepathicgrunt.the_bumblezone.events.client;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.BiConsumer;

public record BzRegisterBlockColorEvent(BiConsumer<List<BlockTintSource>, Block[]> colors) {

    public static final EventHandler<BzRegisterBlockColorEvent> EVENT = new EventHandler<>();

    public void register(List<BlockTintSource> tintSources, Block... blocks) {
        colors.accept(tintSources, blocks);
    }
}
