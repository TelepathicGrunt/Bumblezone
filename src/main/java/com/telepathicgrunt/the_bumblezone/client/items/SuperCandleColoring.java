package com.telepathicgrunt.the_bumblezone.client.items;

import com.telepathicgrunt.the_bumblezone.blocks.SuperIncenseCandleBase;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

public class SuperCandleColoring {

    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register(
            (state, reader, pos, color) -> SuperIncenseCandleBase.getBlockColor(reader, pos, color),
            BzBlocks.SUPER_INCENSE_BASE_CANDLE.get()
        );
    }

    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(
            (stack, tintIndex) -> tintIndex != 0 ? -1 : event.getBlockColors().getColor(
                ((BlockItem) stack.getItem()).getBlock().defaultBlockState(),
                null,
                null,
                SuperIncenseCandleBase.getItemColor(stack)
            ),
            BzBlocks.SUPER_INCENSE_BASE_CANDLE.get()
        );
    }
}
