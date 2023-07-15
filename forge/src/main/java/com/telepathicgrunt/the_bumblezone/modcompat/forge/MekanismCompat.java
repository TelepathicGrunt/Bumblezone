package com.telepathicgrunt.the_bumblezone.modcompat.forge;

import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationHookup;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import mekanism.api.event.MekanismTeleportEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

public class MekanismCompat implements ModCompat {

	public MekanismCompat() {
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		forgeBus.addListener(MekanismCompat::isMekaToolTeleporting);

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
