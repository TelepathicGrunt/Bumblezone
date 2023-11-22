package com.telepathicgrunt.the_bumblezone.modcompat.neoforge;

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

public class ReliquaryCompat implements ModCompat {

	public static Item RENDING_GALE;

	public ReliquaryCompat() {
		RENDING_GALE = BuiltInRegistries.ITEM.get(new ResourceLocation("reliquary", "rending_gale"));

		IEventBus eventBus = MinecraftForge.EVENT_BUS;
		eventBus.addListener(ReliquaryCompat::onRendingGaleItemUse);

		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.reliquaryPresent = true;
	}

	public static void onRendingGaleItemUse(PlayerInteractEvent.RightClickItem event) {
		if (RENDING_GALE != null && event.getItemStack().is(RENDING_GALE)) {
			Entity entity = event.getEntity();
			if (entity != null && HeavyAir.isInHeavyAir(entity.level(), entity.getBoundingBox())) {
				if (entity instanceof ServerPlayer serverPlayer) {
					serverPlayer.displayClientMessage(Component.translatable("system.the_bumblezone.denied_magic")
							.withStyle(ChatFormatting.ITALIC)
							.withStyle(ChatFormatting.RED), true);

				}

				event.getEntity().swing(event.getHand());
				event.setCanceled(true);
			}
		}
	}
}
