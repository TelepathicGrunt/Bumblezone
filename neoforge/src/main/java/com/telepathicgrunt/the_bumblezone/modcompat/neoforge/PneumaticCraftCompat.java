
package com.telepathicgrunt.the_bumblezone.modcompat.neoforge;

import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;

public class PneumaticCraftCompat implements ModCompat {
//	public static Optional<Holder.Reference<Item>> PNEUMATIC_BOOTS;
//
//	public PneumaticCraftCompat() {
//		PNEUMATIC_BOOTS = BuiltInRegistries.ITEM.get(Identifier.fromNamespaceAndPath("pneumaticcraft", "pneumatic_boots"));
//
//		// Keep at end so it is only set to true if no exceptions was thrown during setup
//		ModChecker.pneumaticCraftPresent = true;
//	}
//
//	@Override
//	public EnumSet<Type> compatTypes() {
//		return EnumSet.of(Type.HEAVY_AIR_RESTRICTED);
//	}
//
//	@Override
//	public void restrictFlight(Entity entity, double extraGravity) {
//		if (entity instanceof ServerPlayer player &&
//			PNEUMATIC_BOOTS.isPresent() &&
//			player.getItemBySlot(EquipmentSlot.FEET).is(PNEUMATIC_BOOTS.get()))
//		{
//			ICommonArmorRegistry reg = PneumaticRegistry.getInstance().getCommonArmorRegistry();
//			reg.getArmorUpgradeHandler(BuiltinArmorUpgrades.JET_BOOTS)
//					.ifPresent(jetBoots -> reg.getCommonArmorHandler(player)
//							.setUpgradeEnabled(jetBoots, false));
//		}
//	}
}
