package net.telepathicgrunt.bumblezone.modCompat;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.math.Vec3d;

/**
 * This class is used so java wont load PotionOfBeesCompat class and crash
 * if the mod isn't on. This is because java will load classes if their method
 * is present even though it isn't called when going through some code. However,
 * java won't load classes referenced in the method ahead of time so the redirection
 * works to keep PotionOfBeesCompat unloaded by "nesting" the method dependant on the mod.
 */
public class PotionOfBeesRedirection
{
	public static boolean POBIsPotionOfBeesItem(Item item)
	{
	     return PotionOfBeesCompat.POBIsPotionOfBeesItem(item);
	}

	public static boolean POBReviveLarvaBlockEvent(Entity thrownEntity, Vec3d hitBlockPos)
	{
	    return PotionOfBeesCompat.POBReviveLarvaBlockEvent(thrownEntity, hitBlockPos);
    }
}
