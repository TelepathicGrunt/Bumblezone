package net.telepathicgrunt.bumblezone.modcompatibility;

import net.minecraft.block.BlockState;

/**
 * This class is used so java wont load BeesourcefulCompat class and crash
 * if the mod isn't on. This is because java will load classes if their method
 * is present even though it isn't called when going through some code. However,
 * java won't load classes referenced in the method ahead of time so the redirection
 * works to keep BeesourcefulCompat unloaded by "nesting" the method dependant on the mod.
 */
public class ProductiveBeesRedirection
{
	public static boolean PBIsAdvancedBeehiveAbstractBlock(BlockState block)
	{
	    return ProductiveBeesCompat.PBIsAdvancedBeehiveAbstractBlock(block);
	}
}
