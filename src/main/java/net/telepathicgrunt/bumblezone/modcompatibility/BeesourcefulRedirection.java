package net.telepathicgrunt.bumblezone.modcompatibility;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

/**
 * This class is used so java wont load BeesourcefulCompat class and crash
 * if the mod isn't on. This is because java will load classes if their method
 * is present even though it isn't called when going through some code. However,
 * java won't load classes referenced in the method ahead of time so the redirection
 * works to keep BeesourcefulCompat unloaded by "nesting" the method dependant on the mod.
 */
public class BeesourcefulRedirection
{
	public static void BSMobSpawnEvent(LivingSpawnEvent.CheckSpawn event)
	{
		BeesourcefulCompat.BSMobSpawnEvent(event);
	}
	
	public static Block BSGetRandomHoneycomb(Random random)
	{
		return BeesourcefulCompat.BSGetRandomHoneycomb(random);
	}
}
