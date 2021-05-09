package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
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
						Bumblezone.BzModCompatibilityConfig.spawnProductiveBeesBeesMob.get() &&
						entity.level.getRandom().nextInt(15) == 0)
				{
					ProductiveBeesRedirection.PBMobSpawnEvent(event, entity.isBaby());
				}

				if (ModChecker.resourcefulBeesPresent &&
						Bumblezone.BzModCompatibilityConfig.spawnResourcefulBeesBeesMob.get() &&
						entity.level.getRandom().nextInt(15) == 0)
				{
					ResourcefulBeesRedirection.RBMobSpawnEvent(event, entity.isBaby());
				}

				if (ModChecker.pokecubePresent &&
						Bumblezone.BzModCompatibilityConfig.spawnPokecubeBeePokemon.get() &&
						entity.level.getRandom().nextInt(8) == 0)
				{
					PokecubeCompat.PCMobSpawnEvent(event, entity.isBaby());
				}
			}
		}
	}
}
