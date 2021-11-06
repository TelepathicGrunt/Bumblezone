package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

public class ModdedBeesBeesSpawning
{
	/*
	 * Manual spawning of modded Bees so it can be disabled real time by config.
	 * works by making 1/15th of bees spawning also spawn modded bees
	 */
	public static void MobSpawnEvent(LivingSpawnEvent.CheckSpawn event) {
		if (ModChecker.productiveBeesPresent || ModChecker.resourcefulBeesPresent || ModChecker.pokecubePresent) {
			MobEntity entity = (MobEntity) event.getEntity();
			ResourceLocation worldRL = entity.level.getServer().registryAccess().dimensionTypes().getKey(entity.level.dimensionType());

			if (worldRL != null && worldRL.equals(Bumblezone.MOD_DIMENSION_ID) &&
				entity.getType() == EntityType.BEE)
			{
				if (ModChecker.productiveBeesPresent &&
						BzModCompatibilityConfigs.spawnProductiveBeesBeesMob.get() &&
						entity.level.getRandom().nextInt(15) == 0)
				{
					ProductiveBeesCompat.PBMobSpawnEvent(event, entity.isBaby());
				}

				if (ModChecker.resourcefulBeesPresent &&
						BzModCompatibilityConfigs.spawnResourcefulBeesBeesMob.get() &&
						entity.level.getRandom().nextInt(15) == 0)
				{
					ResourcefulBeesCompat.RBMobSpawnEvent(event, entity.isBaby());
				}

				if (ModChecker.pokecubePresent &&
						BzModCompatibilityConfigs.spawnPokecubeBeePokemon.get() &&
						entity.level.getRandom().nextInt(8) == 0)
				{
					PokecubeCompat.PCMobSpawnEvent(event, entity.isBaby());
				}
			}
		}
	}
}
