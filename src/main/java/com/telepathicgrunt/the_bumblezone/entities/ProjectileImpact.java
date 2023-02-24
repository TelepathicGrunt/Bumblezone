package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.ProjectileImpactEvent;

public class ProjectileImpact {

    public static void onProjectileImpact(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();
        if (projectile.getType().is(BzTags.TELEPORT_PROJECTILES) && projectile.getOwner() != null) {
            if(EntityTeleportationHookup.runTeleportProjectileImpact(new Vec3(projectile.getX(), projectile.getY(), projectile.getZ()), projectile.getOwner(), projectile)) {
                event.setCanceled(true);
                return;
            }

            if (event.getRayTraceResult() instanceof EntityHitResult entityHitResult) {
                if (EntityTeleportationHookup.runEntityHitCheck(entityHitResult, projectile)) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
