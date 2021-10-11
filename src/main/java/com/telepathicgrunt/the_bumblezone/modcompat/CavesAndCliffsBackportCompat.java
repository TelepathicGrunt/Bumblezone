package com.telepathicgrunt.the_bumblezone.modcompat;

import com.blackgear.cavesandcliffs.common.blocks.CandleBlock;
import com.blackgear.cavesandcliffs.core.registries.CCBBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CavesAndCliffsBackportCompat {

    private static List<Block> CANDLES_VARIANTS;

    public static void setupCavesAndCliffs() {
        CANDLES_VARIANTS = new ArrayList<>();

        //Tier 1 candles - basic no effects basic colors candles
        CANDLES_VARIANTS.addAll(Arrays.asList(
                CCBBlocks.CANDLE.get(),
                CCBBlocks.WHITE_CANDLE.get(),
                CCBBlocks.ORANGE_CANDLE.get(),
                CCBBlocks.MAGENTA_CANDLE.get(),
                CCBBlocks.LIGHT_BLUE_CANDLE.get(),
                CCBBlocks.YELLOW_CANDLE.get(),
                CCBBlocks.LIME_CANDLE.get(),
                CCBBlocks.PINK_CANDLE.get(),
                CCBBlocks.GRAY_CANDLE.get(),
                CCBBlocks.LIGHT_GRAY_CANDLE.get(),
                CCBBlocks.CYAN_CANDLE.get(),
                CCBBlocks.PURPLE_CANDLE.get(),
                CCBBlocks.BLUE_CANDLE.get(),
                CCBBlocks.BROWN_CANDLE.get(),
                CCBBlocks.GREEN_CANDLE.get(),
                CCBBlocks.RED_CANDLE.get(),
                CCBBlocks.BLACK_CANDLE.get()
        ));

        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.cavesAndCliffsPresent = true;
    }

    /**
     * Picks a random Caves and Cliffs candle
     */
    public static BlockState CACGetRandomCandle(Random random, int numOfCandles, boolean waterlogged, boolean lit) {
        int index = random.nextInt(CANDLES_VARIANTS.size());
        return CANDLES_VARIANTS.get(index).defaultBlockState()
                .setValue(CandleBlock.CANDLES, numOfCandles)
                .setValue(CandleBlock.WATERLOGGED, waterlogged)
                .setValue(CandleBlock.LIT, lit);
    }
}
