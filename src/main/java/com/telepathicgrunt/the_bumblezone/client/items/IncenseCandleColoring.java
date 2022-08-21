package com.telepathicgrunt.the_bumblezone.client.items;

import com.telepathicgrunt.the_bumblezone.blocks.IncenseCandleBase;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.world.item.BlockItem;

import java.util.Objects;

public class IncenseCandleColoring {

    public static void registerBlockColors() {
        ColorProviderRegistry.BLOCK.register(
            (state, reader, pos, color) -> IncenseCandleBase.getBlockColor(reader, pos, color),
            BzBlocks.INCENSE_BASE_CANDLE
        );
    }

    public static void registerItemColors() {
        ColorProviderRegistry.ITEM.register(
            (stack, tintIndex) -> tintIndex != 0 ? -1 : Objects.requireNonNull(ColorProviderRegistry.BLOCK.get(BzBlocks.INCENSE_BASE_CANDLE))
                .getColor(
                    ((BlockItem) stack.getItem()).getBlock().defaultBlockState(),
                    null,
                    null,
                    IncenseCandleBase.getItemColor(stack)
                ),
            BzBlocks.INCENSE_BASE_CANDLE
        );
    }
}
