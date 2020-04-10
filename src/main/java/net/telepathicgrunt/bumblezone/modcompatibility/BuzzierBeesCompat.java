package net.telepathicgrunt.bumblezone.modcompatibility;

import com.bagel.buzzierbees.core.registry.BBEntities;

import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.telepathicgrunt.bumblezone.biome.BzBaseBiome;
import net.telepathicgrunt.bumblezone.biome.BzBiomes;
import net.telepathicgrunt.bumblezone.generation.BzChunkGenerator;

public class BuzzierBeesCompat
{
	
	
	public static void setupBuzzierBees() 
	{
		ModChecking.buzzierBeesPresent = true;
		BzChunkGenerator.MOBS_SLIME_ENTRY = new Biome.SpawnListEntry(BBEntities.HONEY_SLIME.get(), 1, 1, 1);
		BzBiomes.biomes.forEach(biome -> ((BzBaseBiome)biome).addModMobs(EntityClassification.CREATURE, BBEntities.HONEY_SLIME.get(), 1, 4, 8));
		
		
	}
}
