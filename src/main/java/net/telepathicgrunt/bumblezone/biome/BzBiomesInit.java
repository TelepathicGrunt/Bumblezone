package net.telepathicgrunt.bumblezone.biome;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.biome.biomes.HivePillarBiome;
import net.telepathicgrunt.bumblezone.biome.biomes.HiveWallBiome;
import net.telepathicgrunt.bumblezone.biome.biomes.SugarWaterBiome;

import java.util.HashSet;
import java.util.Set;


public class BzBiomesInit
{

	//list of all biomes we registered
	public static Set<Biome> biomes = new HashSet<Biome>();

	//biome instances
	public static Biome SUGAR_WATER = new SugarWaterBiome();
	public static Biome HIVE_WALL = new HiveWallBiome();
	public static Biome HIVE_PILLAR = new HivePillarBiome();


	/**
	 * Registers the biomes so they now exist in the registry along with their types.
	 * And adds biome to our list of biomes. 
	 */
	public static void registerBiomes()
	{
		biomes.add(Registry.register(Registry.BIOME, new Identifier(Bumblezone.MODID, "sugar_water_floor"), SUGAR_WATER));
		biomes.add(Registry.register(Registry.BIOME, new Identifier(Bumblezone.MODID, "hive_wall"), HIVE_WALL));
		biomes.add(Registry.register(Registry.BIOME, new Identifier(Bumblezone.MODID, "hive_pillar"), HIVE_PILLAR));
	}
}
