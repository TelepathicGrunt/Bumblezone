package com.telepathicgrunt.the_bumblezone.client.items;

import com.telepathicgrunt.the_bumblezone.blocks.IncenseCandleBase;
import com.telepathicgrunt.the_bumblezone.events.client.RegisterBlockColorEvent;
import com.telepathicgrunt.the_bumblezone.events.client.RegisterItemColorEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.world.item.BlockItem;

public class IncenseCandleColoring {

    public static void registerBlockColors(RegisterBlockColorEvent event) {
        event.register(
            (state, reader, pos, color) -> IncenseCandleBase.getBlockColor(reader, pos, color),
            BzBlocks.INCENSE_BASE_CANDLE.get()
        );
    }

    public static void registerItemColors(RegisterItemColorEvent event) {
        event.register(
            (stack, tintIndex) -> tintIndex != 0 ? -1 : event.blockColors().getColor(
                ((BlockItem) stack.getItem()).getBlock().defaultBlockState(),
                null,
                null,
                IncenseCandleBase.getItemColor(stack)
            ),
            BzBlocks.INCENSE_BASE_CANDLE.get()
        );
    }
}
