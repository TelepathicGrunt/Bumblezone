package com.telepathicgrunt.the_bumblezone.modcompat;

import com.github.draylar.beebetter.registry.BeeBlocks;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BeeBetterCompat {
    private static List<Block> BEE_DUNGEON_BLOCKS;
    private static List<Block> SPIDER_BEE_DUNGEON_BLOCKS;
    private static List<Block> CANDLES_VARIANTS;
    private static List<Block> CAVE_VARIANTS;

    public static void setupCompat() {
        if(!Bumblezone.BZ_CONFIG.BZModCompatibilityConfig.allowBeeBetterCompat) return;

        BEE_DUNGEON_BLOCKS = new ArrayList<>();
        SPIDER_BEE_DUNGEON_BLOCKS = new ArrayList<>();
        CANDLES_VARIANTS = new ArrayList<>();
        CAVE_VARIANTS = new ArrayList<>();

        BEE_DUNGEON_BLOCKS.add(BeeBlocks.BUBBLY_BEESWAX_BRICKS);
        BEE_DUNGEON_BLOCKS.add(BeeBlocks.ANTIQUE_BEESWAX_BRICKS);
        BEE_DUNGEON_BLOCKS.add(BeeBlocks.ANTIQUE_CHISELED_BEESWAX);
        BEE_DUNGEON_BLOCKS.add(BeeBlocks.CHISELED_BEESWAX_PIMPLE);

        SPIDER_BEE_DUNGEON_BLOCKS.add(BeeBlocks.ANCIENT_CRUMBLING_BEESWAX);
        SPIDER_BEE_DUNGEON_BLOCKS.add(BeeBlocks.ANCIENT_GRAVELLED_BEESWAX);
        SPIDER_BEE_DUNGEON_BLOCKS.add(BeeBlocks.ANCIENT_MOSSY_CRUMBLING_BEESWAX);
        SPIDER_BEE_DUNGEON_BLOCKS.add(BeeBlocks.ANTIQUE_MOSSY_BEESWAX_BRICKS);
        SPIDER_BEE_DUNGEON_BLOCKS.add(BeeBlocks.MOSSY_BEESWAX_BRICKS);

        CANDLES_VARIANTS.add(BeeBlocks.CANDLE);
        CANDLES_VARIANTS.add(BeeBlocks.BLACK_CANDLE);
        CANDLES_VARIANTS.add(BeeBlocks.BLUE_CANDLE);
        CANDLES_VARIANTS.add(BeeBlocks.BROWN_CANDLE);
        CANDLES_VARIANTS.add(BeeBlocks.CYAN_CANDLE);
        CANDLES_VARIANTS.add(BeeBlocks.GRAY_CANDLE);
        CANDLES_VARIANTS.add(BeeBlocks.GREEN_CANDLE);
        CANDLES_VARIANTS.add(BeeBlocks.LIME_CANDLE);
        CANDLES_VARIANTS.add(BeeBlocks.MAGENTA_CANDLE);
        CANDLES_VARIANTS.add(BeeBlocks.ORANGE_CANDLE);
        CANDLES_VARIANTS.add(BeeBlocks.PINK_CANDLE);
        CANDLES_VARIANTS.add(BeeBlocks.PURPLE_CANDLE);
        CANDLES_VARIANTS.add(BeeBlocks.RED_CANDLE);
        CANDLES_VARIANTS.add(BeeBlocks.WHITE_CANDLE);
        CANDLES_VARIANTS.add(BeeBlocks.YELLOW_CANDLE);
        CANDLES_VARIANTS.add(BeeBlocks.LIGHT_BLUE_CANDLE);
        CANDLES_VARIANTS.add(BeeBlocks.LIGHT_GRAY_CANDLE);

        CAVE_VARIANTS.add(BeeBlocks.BEESWAX_BLOCK);
        CAVE_VARIANTS.add(BeeBlocks.BUBBLY_BEESWAX_BRICKS);
        CAVE_VARIANTS.add(BeeBlocks.ANTIQUE_POLISHED_BEESWAX);
        CAVE_VARIANTS.add(BeeBlocks.FLAKY_BEESWAX_BRICKS);
        CAVE_VARIANTS.add(BeeBlocks.ANTIQUE_FLAKY_BEESWAX_BRICKS);
        CAVE_VARIANTS.add(BeeBlocks.ANCIENT_CRUMBLING_BEESWAX);
        CAVE_VARIANTS.add(BeeBlocks.ANCIENT_GRAVELLED_BEESWAX);
        CAVE_VARIANTS.add(BeeBlocks.TINY_BEESWAX_BRICKS);
        CAVE_VARIANTS.add(BeeBlocks.ROOF_BEESWAX_TILES);

        ModChecker.beeBetterPresent = true;
    }

    public static BlockState getCandle(Random random){
        return CANDLES_VARIANTS.get(random.nextInt(CANDLES_VARIANTS.size())).defaultBlockState();
    }

    public static BlockState getBeeDungeonBlock(Random random){
        return BEE_DUNGEON_BLOCKS.get(random.nextInt(random.nextInt(BEE_DUNGEON_BLOCKS.size()) + 1)).defaultBlockState();
    }

    public static BlockState getSpiderDungeonBlock(Random random){
        return SPIDER_BEE_DUNGEON_BLOCKS.get(random.nextInt(random.nextInt(SPIDER_BEE_DUNGEON_BLOCKS.size()) + 1)).defaultBlockState();
    }

    public static BlockState getBeeswaxBlock(int index){
        return CAVE_VARIANTS.get(index % CAVE_VARIANTS.size()).defaultBlockState();
    }
}
