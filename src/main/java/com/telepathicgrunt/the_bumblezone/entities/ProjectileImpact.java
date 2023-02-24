package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ProjectileImpact {

    public static boolean projectileImpactNotHandledByBz(HitResult hitResult, Projectile projectile) {
        if (projectile.getType().is(BzTags.TELEPORT_PROJECTILES) && projectile.getOwner() != null) {
            if (hitResult instanceof BlockHitResult) {
                return !EntityTeleportationHookup.runTeleportProjectileImpact(hitResult, projectile);
            }
            else if (hitResult instanceof EntityHitResult entityHitResult) {
                return !EntityTeleportationHookup.runEntityHitCheck(entityHitResult, projectile);
            }
        }
        return true;
    }
}
