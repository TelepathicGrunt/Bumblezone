package net.telepathicgrunt.bumblezone.modcompatibility;

import net.minecraftforge.event.entity.living.LivingSpawnEvent;

/**
 * This class is used so java wont load BuzzierBeesCompat class and crash
 * if the mod isn't on as java will load classes if their method is present
 * even though it isn't called when going through a method but it only loads
 * so many classes deep ahead of time so the redirection works to keep 
 * BuzzierBeesCompat unloaded.
 */
public class BeesourcefulRedirection
{
	public static void BSMobSpawnEvent(LivingSpawnEvent.CheckSpawn event)
	{
		BeesourcefulCompat.BSMobSpawnEvent(event);
	}
}
