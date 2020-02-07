package net.telepathicgrunt.bumblezone.world.biome;


import java.util.HashSet;
import java.util.Set;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.world.biomes.HiveWallBiome;
import net.telepathicgrunt.bumblezone.world.biomes.SugarWaterBiome;

public class BiomeInit {


	//list of all biomes we registered
	public static Set<Biome> biomes = new HashSet<Biome>();
	
	//biome instances
	public static Biome SUGAR_WATER = new SugarWaterBiome();
	public static Biome HIVE_WALL = new HiveWallBiome();
	
	
	//registers the biomes so they now exist in the registry along with their types
	public static void registerBiomes(RegistryEvent.Register<Biome> event) {

   	    IForgeRegistry<Biome> registry = event.getRegistry();
		
		initBiome(registry, SUGAR_WATER, "Sugar Water Canal", BiomeType.WARM, Type.PLAINS);
		initBiome(registry, HIVE_WALL, "Hive Wall", BiomeType.WARM, Type.PLAINS);
	}


	//adds biome to registry with their type to the registry and to the biome dictionary
	private static Biome initBiome(IForgeRegistry<Biome> registry, Biome biome, String name, BiomeType biomeType, Type... types) {
		Bumblezone.register(registry, biome, name);
		BiomeDictionary.addTypes(biome, types);
		biomes.add(biome);
		return biome;
	}
}
