package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationHookup;
import mekanism.api.event.MekanismTeleportEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

public class MekanismCompat {

	public static void setupCompat() {
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		forgeBus.addListener(MekanismCompat::isMekaToolTeleporting);

		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.mekanismPresent = true;
	}

	private static void isMekaToolTeleporting(MekanismTeleportEvent.MekaTool event) {
		Player player = event.getEntity();
		BlockHitResult blockHitResult = event.getTargetBlock();
		if (blockHitResult != null && EntityTeleportationHookup.runGenericTeleport(player, blockHitResult.getBlockPos())) {
			event.setCanceled(true);
		}
	}
}
