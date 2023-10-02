package com.telepathicgrunt.the_bumblezone.modcompat.forge;

import com.telepathicgrunt.the_bumblezone.blocks.HeavyAir;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class BloodMagicCompat implements ModCompat {

	public static Item AIR_SIGIL;

	public BloodMagicCompat() {
		AIR_SIGIL = BuiltInRegistries.ITEM.get(new ResourceLocation("bloodmagic", "airsigil"));

		IEventBus eventBus = MinecraftForge.EVENT_BUS;
		eventBus.addListener(BloodMagicCompat::onAirSigilItemUse);

		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.bloodMagicPresent = true;
	}

	public static void onAirSigilItemUse(PlayerInteractEvent.RightClickItem event) {
		if (AIR_SIGIL != null && event.getItemStack().is(AIR_SIGIL)) {
			Entity entity = event.getEntity();
			if (entity != null && HeavyAir.isInHeavyAir(entity.level(), entity.getBoundingBox())) {
				if (entity instanceof ServerPlayer serverPlayer) {
					serverPlayer.displayClientMessage(Component.translatable("system.the_bumblezone.denied_magic")
							.withStyle(ChatFormatting.ITALIC)
							.withStyle(ChatFormatting.RED), true);

					serverPlayer.swing(event.getHand());
				}

				event.getEntity().swing(event.getHand());
				event.setCanceled(true);
			}
		}
	}
}
