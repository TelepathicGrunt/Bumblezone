package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.events.ProjectileHitEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ProjectileImpact {
    public static boolean onProjectileImpact(ProjectileHitEvent event) {
        Projectile projectile = event.projectile();
        if (projectile.getType().is(BzTags.TELEPORT_PROJECTILES) && projectile.getOwner() != null) {
            if(EntityTeleportationHookup.runTeleportProjectileImpact(new Vec3(projectile.getX(), projectile.getY(), projectile.getZ()), projectile.getOwner(), projectile)) {
                return true;
            }

            if (event.hitResult() != null && event.hitResult() instanceof EntityHitResult entityHitResult) {
                if (EntityTeleportationHookup.runEntityHitCheck(entityHitResult, projectile)) {
                    return true;
                }
            }
        }
        return false;
    }
}
