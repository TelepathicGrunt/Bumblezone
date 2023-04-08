package com.telepathicgrunt.the_bumblezone.modcompat;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

public class BackpackedCompat implements ModCompat {
	public BackpackedCompat() {
		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.backpackedPresent = true;
	}

	public static boolean isBackpackedHoneyThemedOrOtherItem(ItemStack itemStack) {
		if (BuiltInRegistries.ITEM.getKey(itemStack.getItem()).getNamespace().equals("backpacked")) {
			return itemStack.hasTag() && itemStack.getOrCreateTag().getString("BackpackModel").equals("backpacked:honey_jar");
		}

		return true;
	}
}
