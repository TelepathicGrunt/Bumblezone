package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.events.entity.EntitySpawnEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;

public class ModdedBeesBeesSpawning {
	/*
	 * Manual spawning of modded Bees so it can be disabled real time by config.
	 * works by making a chance of bees spawning also spawn modded bees
	 */
	public static boolean MobSpawnEvent(EntitySpawnEvent event) {

		if (event.spawnType() == MobSpawnType.NATURAL ||
			event.spawnType() == MobSpawnType.SPAWNER ||
			event.spawnType() == MobSpawnType.CHUNK_GENERATION ||
			event.spawnType() == MobSpawnType.STRUCTURE)
		{
			Mob entity = event.entity();
			ResourceLocation worldRL = entity.level.dimension().location();

			if (worldRL.equals(Bumblezone.MOD_DIMENSION_ID) && entity.getType() == EntityType.BEE) {

				for (ModCompat compat : ModChecker.SPAWNING_COMPATS) {
					if (compat.onBeeSpawn(event, entity.isBaby())) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
