
package com.telepathicgrunt.the_bumblezone.modcompat.forge;

import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.cyclops.evilcraft.entity.item.EntityBroom;

import java.util.EnumSet;

public class EvilCraftCompat implements ModCompat {
	public EvilCraftCompat() {
		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.evilCraftPresent = true;
	}

	@Override
	public EnumSet<Type> compatTypes() {
		return EnumSet.of(Type.HEAVY_AIR_RESTRICTED);
	}

	public void restrictFlight(Entity entity, double extraGravity) {
		if (entity.getControlledVehicle() instanceof EntityBroom entityBroom) {
			entityBroom.setDeltaMovement(
				entityBroom.getDeltaMovement().x,
				entityBroom.getDeltaMovement().y + extraGravity,
				entityBroom.getDeltaMovement().z
			);
		}
	}
}
