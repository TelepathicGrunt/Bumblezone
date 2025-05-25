
package com.telepathicgrunt.the_bumblezone.modcompat.fabricbase;

import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import dev.shadowsoffire.apotheosis.ench.asm.EnchHooks;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.EnumSet;

public class ZenithCompat implements ModCompat {
	public ZenithCompat() {
		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.zenithPresent = true;
	}

	@Override
	public EnumSet<Type> compatTypes() {
		return EnumSet.of(Type.ENCHANTMENT_MAX_LEVEL);
	}

	public int maxLevelForEnchantment(Enchantment enchantment) {
		return EnchHooks.getMaxLevel(enchantment);
	}
}
