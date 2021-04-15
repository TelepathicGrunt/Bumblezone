package com.telepathicgrunt.the_bumblezone.modcompat;

import net.minecraftforge.event.entity.living.LivingSpawnEvent;

/**
 * This class is used so java wont load PokecubeRedirection class and crash if
 * the mod isn't on. This is because java will load classes if their method is
 * present even though it isn't called when going through some code. However,
 * java won't load classes referenced in the method ahead of time so the
 * redirection works to keep PokecubeCompat unloaded by "nesting" the method
 * dependent on the mod.
 */
public class PokecubeRedirection {
	public static void PCMobSpawnEvent(LivingSpawnEvent.CheckSpawn event, boolean isChild) {
		PokecubeCompat.PCMobSpawnEvent(event, isChild);
	}
}
