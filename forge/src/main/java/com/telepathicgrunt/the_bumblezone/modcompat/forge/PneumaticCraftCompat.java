
package com.telepathicgrunt.the_bumblezone.modcompat.forge;

import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.pneumatic_armor.IArmorUpgradeHandler;
import me.desht.pneumaticcraft.common.pneumatic_armor.ArmorUpgradeRegistry;
import me.desht.pneumaticcraft.common.pneumatic_armor.CommonArmorHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class PneumaticCraftCompat implements ModCompat {
	public PneumaticCraftCompat() {
		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.pneumaticCraftPresent = true;
	}

	@Override
	public EnumSet<Type> compatTypes() {
		return EnumSet.of(Type.HEAVY_AIR_RESTRICTED);
	}

	public void restrictFlight(Entity entity, double extraGravity) {
		if (entity instanceof Player player) {
			IArmorUpgradeHandler<?> upgrade = ArmorUpgradeRegistry.getInstance().getUpgradeEntry(new ResourceLocation("pneumaticcraft:jet_boots"));
			disableFlyBoots(player, upgrade);
		}
	}

	public void disableFlyBoots(Player player, IArmorUpgradeHandler<?> upgrade) {
		CommonArmorHandler handler = (CommonArmorHandler) PneumaticRegistry.getInstance().getCommonArmorRegistry().getCommonArmorHandler(player);
		EquipmentSlot equipmentSlot = upgrade.getEquipmentSlot();
		byte slotIndex = (byte) upgrade.getIndex();
		if (handler.isUpgradeInserted(equipmentSlot, slotIndex) && handler.isUpgradeEnabled(equipmentSlot, slotIndex)) {
			player.getCooldowns().addCooldown(player.getItemBySlot(equipmentSlot).getItem(), 20);
		}
	}
}
