package com.telepathicgrunt.the_bumblezone.client.items;

import com.telepathicgrunt.the_bumblezone.blocks.PotionCandleBase;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterBlockColorEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterItemColorEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.world.item.BlockItem;

public class PotionCandleColoring {

    public static void registerBlockColors(BzRegisterBlockColorEvent event) {
        event.register(
            (state, reader, pos, color) -> PotionCandleBase.getBlockColor(reader, pos, color),
            BzBlocks.POTION_BASE_CANDLE.get()
        );
    }

    public static void registerItemColors(BzRegisterItemColorEvent event) {
        event.register(
            (stack, tintIndex) -> tintIndex != 0 ? -1 : event.blockColors().getColor(
                ((BlockItem) stack.getItem()).getBlock().defaultBlockState(),
                null,
                null,
                PotionCandleBase.getItemColor(stack)
            ),
            BzBlocks.POTION_BASE_CANDLE.get()
        );
    }
}
