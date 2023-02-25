package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.events.ProjectileHitEvent;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import com.telepathicgrunt.the_bumblezone.modcompat.TwilightForestCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ProjectileImpact {
    public static boolean onProjectileImpact(ProjectileHitEvent event) {
        Projectile projectile = event.projectile();

        if (!ModChecker.PROJECTILE_IMPACT_HANDLED_COMPATS.isEmpty()) {
            for (ModCompat compat : ModChecker.PROJECTILE_IMPACT_HANDLED_COMPATS) {
                if (compat.isTeleportHandled(event.hitResult(), projectile.getOwner(), projectile)) {
                    return true;
                }
            }
        }

        if (projectile.getType().is(BzTags.TELEPORT_PROJECTILES) && projectile.getOwner() != null) {
            if (event.hitResult() != null && event.hitResult() instanceof BlockHitResult blockHitResult) {
                return EntityTeleportationHookup.runTeleportProjectileImpact(blockHitResult, projectile.getOwner(), projectile);
            }
            else if (event.hitResult() != null && event.hitResult() instanceof EntityHitResult entityHitResult) {
                return EntityTeleportationHookup.runEntityHitCheck(entityHitResult, projectile.getOwner(), projectile);
            }
        }
        return false;
    }
}
