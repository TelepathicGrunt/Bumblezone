
package com.telepathicgrunt.the_bumblezone.modcompat;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;

public class AdAstraCompat implements ModCompat {
	private static Item JET_HELMET = null;
	private static Item JET_CHEST = null;
	private static Item JET_LEGS = null;
	private static Item JET_FEET = null;

	public AdAstraCompat() {
		JET_HELMET = BuiltInRegistries.ITEM.get(new ResourceLocation("ad_astra", "jet_suit_helmet"));
		JET_CHEST = BuiltInRegistries.ITEM.get(new ResourceLocation("ad_astra", "jet_suit"));
		JET_LEGS = BuiltInRegistries.ITEM.get(new ResourceLocation("ad_astra", "jet_suit_pants"));
		JET_FEET = BuiltInRegistries.ITEM.get(new ResourceLocation("ad_astra", "jet_suit_boots"));

		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.adAstraPresent = true;
	}

	@Override
	public EnumSet<Type> compatTypes() {
		return EnumSet.of(Type.HEAVY_AIR_RESTRICTED);
	}

	public void restrictFlight(Entity entity, double extraGravity) {
		if (entity instanceof Player player) {
			if (hasItemEquipped(player, EquipmentSlot.HEAD, JET_HELMET) &&
				hasItemEquipped(player, EquipmentSlot.CHEST, JET_CHEST) &&
				hasItemEquipped(player, EquipmentSlot.LEGS, JET_LEGS) &&
				hasItemEquipped(player, EquipmentSlot.FEET, JET_FEET))
			{
				ItemStack jetpackSuit = player.getItemBySlot(EquipmentSlot.CHEST);
				if (!player.getCooldowns().isOnCooldown(jetpackSuit.getItem())) {
					if (player instanceof ServerPlayer serverPlayer) {
						serverPlayer.displayClientMessage(Component.translatable("system.the_bumblezone.denied_jetpack")
								.withStyle(ChatFormatting.ITALIC)
								.withStyle(ChatFormatting.RED), true);
					}
				}

				player.getCooldowns().addCooldown(jetpackSuit.getItem(), 40);
			}
		}
	}

	private static boolean hasItemEquipped(Player player, EquipmentSlot slot, Item itemToMatch) {
		return player.getItemBySlot(slot).is(itemToMatch);
	}
}
