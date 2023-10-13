
package com.telepathicgrunt.the_bumblezone.modcompat.forge;

import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.pneumatic_armor.BuiltinArmorUpgrades;
import me.desht.pneumaticcraft.api.pneumatic_armor.IArmorUpgradeHandler;
import me.desht.pneumaticcraft.api.pneumatic_armor.ICommonArmorRegistry;
import me.desht.pneumaticcraft.common.pneumatic_armor.ArmorUpgradeRegistry;
import me.desht.pneumaticcraft.common.pneumatic_armor.CommonArmorHandler;
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

public class PneumaticCraftCompat implements ModCompat {
	public static Item PNEUMATIC_BOOTS;

	public PneumaticCraftCompat() {
		PNEUMATIC_BOOTS = BuiltInRegistries.ITEM.get(new ResourceLocation("pneumaticcraft", "pneumatic_boots"));

		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.pneumaticCraftPresent = true;
	}

	@Override
	public EnumSet<Type> compatTypes() {
		return EnumSet.of(Type.HEAVY_AIR_RESTRICTED);
	}

	@Override
	public void restrictFlight(Entity entity, double extraGravity) {
		if (entity instanceof ServerPlayer player &&
			PNEUMATIC_BOOTS != null &&
			player.getItemBySlot(EquipmentSlot.FEET).is(PNEUMATIC_BOOTS))
		{
			ICommonArmorRegistry reg = PneumaticRegistry.getInstance().getCommonArmorRegistry();
			reg.getArmorUpgradeHandler(BuiltinArmorUpgrades.JET_BOOTS)
					.ifPresent(jetBoots -> reg.getCommonArmorHandler(player)
							.setUpgradeEnabled(jetBoots, false));
		}
	}
}
