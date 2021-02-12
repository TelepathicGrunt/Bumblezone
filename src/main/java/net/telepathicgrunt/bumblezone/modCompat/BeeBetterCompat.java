package net.telepathicgrunt.bumblezone.modCompat;

import com.github.draylar.beebetter.BeeBetter;
import com.github.draylar.beebetter.registry.BeeBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.blocks.HoneycombBrood;
import net.telepathicgrunt.bumblezone.configs.BZModCompatibilityConfig;
import net.telepathicgrunt.bumblezone.mixin.blocks.DispenserAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BeeBetterCompat
{
	private static List<Block> BEE_DUNGEON_BLOCKS;
	private static List<Block> SPIDER_BEE_DUNGEON_BLOCKS;
	private static List<Block> CANDLES_VARIANTS;

	public static void setupBeeBetter()
	{
		if(!Bumblezone.BZ_CONFIG.BZModCompatibilityConfig.allowBeeBetterCompat) return;

		BEE_DUNGEON_BLOCKS = new ArrayList<>();
		SPIDER_BEE_DUNGEON_BLOCKS = new ArrayList<>();
		CANDLES_VARIANTS = new ArrayList<>();

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

		ModChecker.beeBetterPresent = true;
	}

	public static BlockState getCandle(Random random){
		return CANDLES_VARIANTS.get(random.nextInt(CANDLES_VARIANTS.size())).getDefaultState();
	}

	public static BlockState getBeeDungeonBlock(Random random){
		return BEE_DUNGEON_BLOCKS.get(random.nextInt(random.nextInt(BEE_DUNGEON_BLOCKS.size()) + 1)).getDefaultState();
	}

	public static BlockState getSpiderDungeonBlock(Random random){
		return SPIDER_BEE_DUNGEON_BLOCKS.get(random.nextInt(random.nextInt(SPIDER_BEE_DUNGEON_BLOCKS.size()) + 1)).getDefaultState();
	}

	public static BlockState getBeeswaxBlock(){
		return BeeBlocks.BEESWAX_BLOCK.getDefaultState();
	}
}
