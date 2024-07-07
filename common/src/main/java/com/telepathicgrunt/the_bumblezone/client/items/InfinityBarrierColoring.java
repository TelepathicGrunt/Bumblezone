package com.telepathicgrunt.the_bumblezone.client.items;

import com.telepathicgrunt.the_bumblezone.blocks.blockentities.InfinityBarrierBlockEntity;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterBlockColorEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;

public class InfinityBarrierColoring {

    public static void registerBlockColors(BzRegisterBlockColorEvent event) {
        event.register(
            (state, reader, pos, color) -> InfinityBarrierBlockEntity.getBlockColor(reader, pos, color),
            BzBlocks.INFINITY_BARRIER.get()
        );
    }
}
