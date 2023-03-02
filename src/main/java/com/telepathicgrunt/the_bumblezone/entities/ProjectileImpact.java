package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.modcompat.ArsNouveauCompat;
import com.telepathicgrunt.the_bumblezone.modcompat.DragonEnchantsCompat;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.TwilightForestCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;

public class ProjectileImpact {

    public static void onProjectileImpact(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();
        if (projectile.getOwner() == null) {
            return;
        }

        if (ModChecker.arsNouveauPresent) {
            if (ArsNouveauCompat.isArsSpellProjectile(projectile)) {
                return;
            }
        }

        if (ModChecker.twilightForestPresent && event.getRayTraceResult() instanceof EntityHitResult entityHitResult) {
            if (TwilightForestCompat.isTeleportHandled(entityHitResult, projectile.getOwner(), projectile)) {
                event.setCanceled(true);
                return;
            }
        }

        if (ModChecker.dragonEnchantsPresent && event.getRayTraceResult() instanceof BlockHitResult blockHitResult) {
            if (DragonEnchantsCompat.isTeleportHandled(blockHitResult, projectile.getOwner(), projectile)) {
                event.setCanceled(true);
                return;
            }
        }

        if (projectile.getType().is(BzTags.TELEPORT_PROJECTILES) && projectile.getOwner() != null) {
            if(event.getRayTraceResult() instanceof BlockHitResult blockHitResult) {
                if (EntityTeleportationHookup.runTeleportProjectileImpact(blockHitResult, projectile.getOwner(), projectile)) {
                    event.setCanceled(true);
                }
            }
            else if (event.getRayTraceResult() instanceof EntityHitResult entityHitResult) {
                if (EntityTeleportationHookup.runEntityHitCheck(entityHitResult, projectile.getOwner(), projectile)) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
