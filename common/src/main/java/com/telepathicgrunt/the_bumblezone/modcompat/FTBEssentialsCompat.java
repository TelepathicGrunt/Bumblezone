
package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.modinit.BzPOI;
import dev.architectury.event.CompoundEventResult;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;

import java.util.List;

public class FTBEssentialsCompat implements ModCompat {
	// TODO: Wait for 2001.1.3 FTB Essentials release
	public FTBEssentialsCompat() {
//		// registering during mod construction...
//		TeleportEvent.TELEPORT.register(this::onTeleportAttempt);

		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.FTBEssentialsPresent = true;
	}

	// event handler
	private CompoundEventResult<Component> onTeleportAttempt(ServerPlayer serverPlayer) {
		PoiManager poiManager = serverPlayer.serverLevel().getPoiManager();
		List<PoiRecord> poiInRange = poiManager.getInSquare(
				(pointOfInterestType) -> pointOfInterestType.value() == BzPOI.ESSENCE_BLOCK_POI.get(),
				serverPlayer.blockPosition(),
				20,
				PoiManager.Occupancy.ANY).toList();

		if (!poiInRange.isEmpty()) {
			return CompoundEventResult.interruptFalse(Component.literal("You cannot escape an Essence event!"));
		}

		return CompoundEventResult.pass();
	}
}
