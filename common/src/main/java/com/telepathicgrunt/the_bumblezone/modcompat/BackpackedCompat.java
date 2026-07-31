package com.telepathicgrunt.the_bumblezone.modcompat;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class BackpackedCompat implements ModCompat {
	public BackpackedCompat() {
		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.backpackedPresent = true;
	}

	private static Identifier BEE_THEMED = Identifier.fromNamespaceAndPath("backpacked", "honey_jar");

	public static boolean isBackpackedHoneyThemedOrOtherItem(ItemStack itemStack) {
		// TODO: Compile error due to needing source. Ugh. And is only on github maven which requires username and password.
//		if (BuiltInRegistries.ITEM.getKey(itemStack.getItem()).getNamespace().equals("backpacked")) {
//			CosmeticProperties cosmeticProperties = itemStack.get(ModDataComponents.COSMETIC_PROPERTIES);
//			if (cosmeticProperties.cosmetic().isPresent()) {
//				return cosmeticProperties.cosmetic().get().equals(BEE_THEMED);
//			}
//		}

		return true;
	}
}
