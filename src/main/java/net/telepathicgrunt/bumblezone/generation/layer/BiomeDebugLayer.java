package net.telepathicgrunt.bumblezone.generation.layer;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;
import net.telepathicgrunt.bumblezone.world.biome.BiomeInit;


public enum BiomeDebugLayer implements IAreaTransformer0
{
	INSTANCE;

	@SuppressWarnings("deprecation")
	private static final int TESTING_BIOME = Registry.BIOME.getId(BiomeInit.HONEY);


	public int apply(INoiseRandom p_215735_1_, int p_215735_2_, int p_215735_3_)
	{
		return TESTING_BIOME;
	}

}