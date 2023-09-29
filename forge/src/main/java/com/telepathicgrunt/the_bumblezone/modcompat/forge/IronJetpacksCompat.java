
package com.telepathicgrunt.the_bumblezone.modcompat.forge;

import com.blakebr0.ironjetpacks.util.JetpackUtils;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class IronJetpacksCompat implements ModCompat {
	public IronJetpacksCompat() {
		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.ironjetpacksPresent = true;
	}

	@Override
	public EnumSet<Type> compatTypes() {
		return EnumSet.of(Type.HEAVY_AIR_RESTRICTED);
	}

	public void restrictFlight(Entity entity) {
		if (entity instanceof Player player) {
			var jetpack = JetpackUtils.getEquippedJetpack(player);
			if (!jetpack.isEmpty() && JetpackUtils.isEngineOn(jetpack)) {
				JetpackUtils.toggleEngine(jetpack);
			}
		}
	}
}
