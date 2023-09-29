
package com.telepathicgrunt.the_bumblezone.modcompat.forge;

import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import net.minecraft.world.entity.Entity;
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
			// TODO: Wait for PneumaticCraft API to be made instead for proper server -> client sync
//			IArmorUpgradeHandler<?> upgrade = ArmorUpgradeRegistry.getInstance().getUpgradeEntry(new ResourceLocation("pneumaticcraft:jet_boots"));
//			setUpgradeEnabled(player, upgrade, false);
		}
	}

//	public void setUpgradeEnabled(Player player, IArmorUpgradeHandler<?> upgrade, boolean enabled) {
//		CommonArmorHandler handler = (CommonArmorHandler) PneumaticRegistry.getInstance().getCommonArmorRegistry().getCommonArmorHandler(player);
//		EquipmentSlot equipmentSlot = upgrade.getEquipmentSlot();
//		byte slotIndex = (byte) upgrade.getIndex();
//		if (handler.isUpgradeInserted(equipmentSlot, slotIndex) && handler.isUpgradeEnabled(equipmentSlot, slotIndex)) {
//			handler.setUpgradeEnabled(equipmentSlot, slotIndex, enabled);
//		}
//	}
}
