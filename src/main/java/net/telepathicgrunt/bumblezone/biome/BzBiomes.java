package net.telepathicgrunt.bumblezone.biome;


import java.util.HashSet;
import java.util.Set;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.telepathicgrunt.bumblezone.biome.biomes.HivePillarBiome;
import net.telepathicgrunt.bumblezone.biome.biomes.HiveWallBiome;
import net.telepathicgrunt.bumblezone.biome.biomes.SugarWaterBiome;
import net.telepathicgrunt.bumblezone.utils.RegUtils;

public class BzBiomes {


	//list of all biomes we registered
	public static Set<Biome> biomes = new HashSet<Biome>();
	
	//biome instances
	public static Biome SUGAR_WATER = new SugarWaterBiome();
	public static Biome HIVE_WALL = new HiveWallBiome();
	public static Biome HIVE_PILLAR = new HivePillarBiome();
	
	
	//registers the biomes so they now exist in the registry along with their types
	public static void registerBiomes(RegistryEvent.Register<Biome> event) {

   	    IForgeRegistry<Biome> registry = event.getRegistry();
		
		initBiome(registry, SUGAR_WATER, "sugar_water_floor", BiomeType.WARM, Type.PLAINS);
		initBiome(registry, HIVE_WALL, "hive_wall", BiomeType.WARM, Type.PLAINS);
		initBiome(registry, HIVE_PILLAR, "hive_pillar", BiomeType.WARM, Type.PLAINS);
	}


	//adds biome to registry with their type to the registry and to the biome dictionary
	private static Biome initBiome(IForgeRegistry<Biome> registry, Biome biome, String name, BiomeType biomeType, Type... types) {
		RegUtils.register(registry, biome, name);
		BiomeDictionary.addTypes(biome, types);
		biomes.add(biome);
		return biome;
	}
}
