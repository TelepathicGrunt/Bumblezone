package com.telepathicgrunt.the_bumblezone.entities;

import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.Event;

public class EnderpearlImpact {

    public static void onPearlHit(ProjectileImpactEvent event) {
        if (event.getProjectile() instanceof ThrownEnderpearl thrownEnderpearl && thrownEnderpearl.getOwner() != null) {
            if(EntityTeleportationHookup.runEnderpearlImpact(new Vec3(thrownEnderpearl.getX(), thrownEnderpearl.getY(), thrownEnderpearl.getZ()), thrownEnderpearl.getOwner(), thrownEnderpearl)) {
                event.setCanceled(true);
                return;
            }

            if (event.getRayTraceResult() != null && event.getRayTraceResult() instanceof EntityHitResult entityHitResult) {
                if (EntityTeleportationHookup.runEntityHitCheck(entityHitResult, thrownEnderpearl)) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
