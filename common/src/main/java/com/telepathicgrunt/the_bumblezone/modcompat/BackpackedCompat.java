package com.telepathicgrunt.the_bumblezone.modcompat;

import com.mrcrayfish.backpacked.common.backpack.CosmeticProperties;
import com.mrcrayfish.backpacked.core.ModDataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class BackpackedCompat implements ModCompat {
	public BackpackedCompat() {
		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.backpackedPresent = true;
	}

	private static Identifier BEE_THEMED = Identifier.fromNamespaceAndPath("backpacked", "honey_jar");

	public static boolean isBackpackedHoneyThemedOrOtherItem(ItemStack itemStack) {
		if (BuiltInRegistries.ITEM.getKey(itemStack.getItem()).getNamespace().equals("backpacked")) {
			CosmeticProperties cosmeticProperties = itemStack.get(ModDataComponents.COSMETIC_PROPERTIES.get());
			if (cosmeticProperties.cosmetic().isPresent()) {
				return cosmeticProperties.cosmetic().get().equals(BEE_THEMED);
			}
		}

		return true;
	}
}
