package com.telepathicgrunt.the_bumblezone.modcompat.fabric;

import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import morethanhidden.restrictedportals.RPCommon;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class RestrictedPortalsCompat implements ModCompat {
	public RestrictedPortalsCompat() {
		ModChecker.restrictedPortalsPresent = true;
	}

	public static boolean isDimensionDisallowed(ServerPlayer serverPlayer, ResourceKey<Level> dimension) {
		return RPCommon.blockPlayerFromTransit(serverPlayer, dimension);
	}
}
