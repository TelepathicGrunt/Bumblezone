package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;

public class ModdedBeesBeesSpawning {
	/*
	 * Manual spawning of modded Bees so it can be disabled real time by config.
	 * works by making a chance of bees spawning also spawn modded bees
	 */
	public static void MobSpawnEvent(LivingSpawnEvent.CheckSpawn event) {

		if (event.getSpawnReason() == MobSpawnType.NATURAL ||
			event.getSpawnReason() == MobSpawnType.SPAWNER ||
			event.getSpawnReason() == MobSpawnType.CHUNK_GENERATION ||
			event.getSpawnReason() == MobSpawnType.STRUCTURE)
		{
			Mob entity = event.getEntity();
			ResourceLocation worldRL = entity.level.dimension().location();

			if (worldRL.equals(Bumblezone.MOD_DIMENSION_ID) && entity.getType() == EntityType.BEE) {
				if (ModChecker.productiveBeesPresent &&
					BzModCompatibilityConfigs.spawnProductiveBeesBeesMob.get() &&
					entity.getRandom().nextFloat() < BzModCompatibilityConfigs.spawnrateOfProductiveBeesMobs.get())
				{
					if(ProductiveBeesCompat.PBMobSpawnEvent(event, entity.isBaby())) {
						event.setResult(Event.Result.DENY);
						return;
					}
				}

				if (ModChecker.resourcefulBeesPresent &&
					BzModCompatibilityConfigs.spawnResourcefulBeesBeesMob.get() &&
					entity.getRandom().nextFloat() < (event.getSpawnReason() == MobSpawnType.SPAWNER ?
							BzModCompatibilityConfigs.spawnrateOfResourcefulBeesMobsBrood.get() :
							BzModCompatibilityConfigs.spawnrateOfResourcefulBeesMobsOther.get()))
				{
					if(ResourcefulBeesCompat.RBMobSpawnEvent(event, entity.isBaby(), event.getSpawnReason())) {
						event.setResult(Event.Result.DENY);
						return;
					}
				}

				if (ModChecker.pokecubePresent &&
					BzModCompatibilityConfigs.spawnPokecubeBeePokemon.get() &&
					entity.getRandom().nextFloat() < BzModCompatibilityConfigs.spawnrateOfPokecubeBeePokemon.get() &&
					entity.isBaby())
				{
					if(PokecubeCompat.PCMobSpawnEvent(event, entity.isBaby())) {
						event.setResult(Event.Result.DENY);
						return;
					}
				}
			}
		}
	}
}
