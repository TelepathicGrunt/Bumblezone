package com.telepathicgrunt.bumblezone.modcompat;

import net.minecraft.block.BlockState;

/**
 * This class is used so java wont load CharmCompat class and crash if
 * the mod isn't on. This is because java will load classes if their method is
 * present even though it isn't called when going through some code. However,
 * java won't load classes referenced in the method ahead of time so the
 * redirection works to keep CharmCompat unloaded by "nesting" the method
 * dependent on the mod.
 */
public class CharmRedirection {
	public static BlockState CGetCandle(boolean waterlogged, boolean lit) {
		return CharmCompat.CGetCandle(waterlogged, lit);
	}
}
