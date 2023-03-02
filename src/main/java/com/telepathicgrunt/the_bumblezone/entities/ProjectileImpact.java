package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.TwilightForestCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ProjectileImpact {

    public static boolean projectileImpactNotHandledByBz(HitResult hitResult, Projectile projectile) {
        if (projectile.getOwner() == null) {
            return false;
        }

        if (ModChecker.twilightForestPresent && hitResult instanceof EntityHitResult entityHitResult) {
            if (TwilightForestCompat.isTeleportHandled(entityHitResult, projectile)) {
                return false;
            }
        }

        if (projectile.getType().is(BzTags.TELEPORT_PROJECTILES) && projectile.getOwner() != null) {
            if (hitResult instanceof BlockHitResult blockHitResult) {
                return !EntityTeleportationHookup.runTeleportProjectileImpact(blockHitResult, projectile.getOwner(), projectile);
            }
            else if (hitResult instanceof EntityHitResult entityHitResult) {
                return !EntityTeleportationHookup.runEntityHitCheck(entityHitResult, projectile.getOwner(), projectile);
            }
        }
        return true;
    }
}
