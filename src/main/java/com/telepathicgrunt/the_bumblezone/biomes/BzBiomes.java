package com.telepathicgrunt.the_bumblezone.biomes;

import java.util.function.Supplier;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.generation.BzBiomeProvider;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeMaker;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BzBiomes
{
	public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, Bumblezone.MODID);
	
	public static final RegistryObject<Biome> HIVE_WALL = createBiome("hive_wall", () -> BiomeMaker.createTheVoid().setRegistryName(BzBiomeProvider.HIVE_WALL));
	public static final RegistryObject<Biome> HIVE_PILLAR = createBiome("hive_pillar", () -> BiomeMaker.createTheVoid().setRegistryName(BzBiomeProvider.HIVE_PILLAR));
	public static final RegistryObject<Biome> SUGAR_WATER_FLOOR = createBiome("sugar_water_floor", () -> BiomeMaker.createTheVoid().setRegistryName(BzBiomeProvider.SUGAR_WATER_FLOOR));

    public static RegistryObject<Biome> createBiome(String name, Supplier<Biome> biome)
 	{
 		return BIOMES.register(name, biome);
 	}
}
