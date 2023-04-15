package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.projectile_damage.api.IProjectileWeapon;
import net.projectile_damage.api.RangedWeaponKind;

public class ProjectileDamageAttributeCompat {
	private static final RangedWeaponKind CRYSTAL_CANNON = RangedWeaponKind.custom(8, 1.9D);

	public static void setupCompat() {
		((IProjectileWeapon) BzItems.CRYSTAL_CANNON).setRangedWeaponKind(CRYSTAL_CANNON);

		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.projectileDamageAttributePresent = true;
	}
}
