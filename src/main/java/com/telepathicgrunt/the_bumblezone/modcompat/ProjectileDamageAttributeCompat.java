package com.telepathicgrunt.the_bumblezone.modcompat;

import net.minecraft.world.item.ProjectileWeaponItem;
import net.projectile_damage.api.IProjectileWeapon;

public class ProjectileDamageAttributeCompat {

	public static void setupCompat() {
		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.projectileDamageAttributePresent = true;
	}

	public static void setUpCrystalCannonStats(ProjectileWeaponItem weaponItem) {
		((IProjectileWeapon)weaponItem).setProjectileDamage(0.5D);
		((IProjectileWeapon)weaponItem).setMaxProjectileVelocity(1.9D);
	}

	public static double getCrystalCannonBasePower(ProjectileWeaponItem weaponItem) {
		return ((IProjectileWeapon)weaponItem).getProjectileDamage();
	}

	public static double getCrystalCannonProjectileSpeed(ProjectileWeaponItem weaponItem) {
		return ((IProjectileWeapon)weaponItem).getMaxProjectileVelocity();
	}
}
