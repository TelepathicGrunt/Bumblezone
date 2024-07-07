package com.telepathicgrunt.the_bumblezone.entities.teleportation;

import com.telepathicgrunt.the_bumblezone.events.entity.BzProjectileHitEvent;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class ProjectileImpact {
    public static boolean onProjectileImpact(BzProjectileHitEvent event) {
        Projectile projectile = event.projectile();
        if (projectile.getOwner() == null) {
            return false;
        }

        if (!ModChecker.PROJECTILE_IMPACT_HANDLED_COMPATS.isEmpty()) {
            for (ModCompat compat : ModChecker.PROJECTILE_IMPACT_HANDLED_COMPATS) {
                InteractionResult result = compat.isProjectileTeleportHandled(event.hitResult(), projectile.getOwner(), projectile);
                if (result != InteractionResult.PASS) {
                    return result == InteractionResult.SUCCESS;
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
