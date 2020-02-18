package net.telepathicgrunt.bumblezone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.telepathicgrunt.bumblezone.biome.BzBiomesInit;
import net.telepathicgrunt.bumblezone.blocks.BzBlocksInit;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionType;
import net.telepathicgrunt.bumblezone.effects.BzEffectsInit;


public class Bumblezone implements ModInitializer
{
	public static final String MODID = "the_bumblezone";
	public static final Logger LOGGER = LogManager.getLogger(MODID);


	@Override
	public void onInitialize()
	{
		BzBlocksInit.registerBlocks();
		BzBlocksInit.registerItems();
		BzEffectsInit.registerEffects();
		BzBiomesInit.registerBiomes();
		BzDimensionType.registerChunkGenerator();
		BzDimensionType.registerDimension();
	}
}
