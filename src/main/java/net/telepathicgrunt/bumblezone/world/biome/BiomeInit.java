package net.telepathicgrunt.bumblezone.world.biome;


import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.world.biomes.HoneyBiome;

public class BiomeInit {

    
	public static Biome HONEY = new HoneyBiome();
	
	
	//registers the biomes so they now exist in the registry along with their types
	public static void registerBiomes(RegistryEvent.Register<Biome> event) {

   	    IForgeRegistry<Biome> registry = event.getRegistry();
		
		initBiome(registry, HONEY, "Honey", BiomeType.WARM, Type.PLAINS);
		
	}



	//adds biome to registry with their type to the registry and to the biome dictionary
	private static Biome initBiome(IForgeRegistry<Biome> registry, Biome biome, String name, BiomeType biomeType, Type... types) {
		Bumblezone.register(registry, biome, name);
		BiomeDictionary.addTypes(biome, types);
		return biome;
	}
}
