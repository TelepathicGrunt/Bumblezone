package com.telepathicgrunt.the_bumblezone.modcompat;

import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;

public class BackpackedCompat {
	public static void setupCompat() {

		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.backpackedPresent = true;
	}

	public static boolean isBackpackedHoneyThemedOrOtherItem(ItemStack itemStack) {
		if (Registry.ITEM.getKey(itemStack.getItem()).getNamespace().equals("backpacked")) {
			return itemStack.hasTag() && itemStack.getOrCreateTag().getString("BackpackModel").equals("backpacked:honey_jar");
		}

		return true;
	}
}
