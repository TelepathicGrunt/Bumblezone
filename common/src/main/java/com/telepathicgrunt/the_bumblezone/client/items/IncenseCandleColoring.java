package com.telepathicgrunt.the_bumblezone.client.items;

import com.telepathicgrunt.the_bumblezone.blocks.IncenseCandleBase;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

public class IncenseCandleColoring {

    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register(
            (state, reader, pos, color) -> IncenseCandleBase.getBlockColor(reader, pos, color),
            BzBlocks.INCENSE_BASE_CANDLE.get()
        );
    }

    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(
            (stack, tintIndex) -> tintIndex != 0 ? -1 : event.getBlockColors().getColor(
                ((BlockItem) stack.getItem()).getBlock().defaultBlockState(),
                null,
                null,
                IncenseCandleBase.getItemColor(stack)
            ),
            BzBlocks.INCENSE_BASE_CANDLE.get()
        );
    }
}
