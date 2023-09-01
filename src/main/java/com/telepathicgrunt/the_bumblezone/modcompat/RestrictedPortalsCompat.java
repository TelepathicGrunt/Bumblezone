package com.telepathicgrunt.the_bumblezone.modcompat;

import morethanhidden.restrictedportals.RPCommon;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class RestrictedPortalsCompat {
	public static void setupCompat() {

		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.restrictedPortals = true;
	}

	public static boolean isDimensionDisallowed(ServerPlayer serverPlayer, ResourceKey<Level> dimension) {
		return RPCommon.blockPlayerFromTransit(serverPlayer, dimension);
	}
}
