
package com.telepathicgrunt.the_bumblezone.modcompat.forge;

import com.blakebr0.ironjetpacks.util.JetpackUtils;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;

public class IronJetpacksCompat implements ModCompat {
	public IronJetpacksCompat() {
		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.ironJetpacksPresent = true;
	}

	@Override
	public EnumSet<Type> compatTypes() {
		return EnumSet.of(Type.HEAVY_AIR_RESTRICTED);
	}

	public void restrictFlight(Entity entity, double extraGravity) {
		if (entity instanceof Player player) {
			ItemStack jetpack = JetpackUtils.getEquippedJetpack(player);
			if (!jetpack.isEmpty() && JetpackUtils.isEngineOn(jetpack)) {
				JetpackUtils.toggleEngine(jetpack);

				if (player instanceof ServerPlayer serverPlayer) {
					serverPlayer.displayClientMessage(Component.translatable("system.the_bumblezone.denied_jetpack")
							.withStyle(ChatFormatting.ITALIC)
							.withStyle(ChatFormatting.RED), true);
				}
			}
		}
	}
}
