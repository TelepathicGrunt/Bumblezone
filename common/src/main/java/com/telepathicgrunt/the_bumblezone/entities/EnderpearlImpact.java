package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.events.ProjectileHitEvent;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class EnderpearlImpact {

    public static boolean onPearlHit(ProjectileHitEvent event) {
        if (event.projectile() instanceof ThrownEnderpearl thrownEnderpearl && thrownEnderpearl.getOwner() != null) {
            if(EntityTeleportationHookup.runEnderpearlImpact(new Vec3(thrownEnderpearl.getX(), thrownEnderpearl.getY(), thrownEnderpearl.getZ()), thrownEnderpearl.getOwner(), thrownEnderpearl)) {
                return true;
            }

            if (event.hitResult() != null && event.hitResult() instanceof EntityHitResult entityHitResult) {
                return EntityTeleportationHookup.runEntityHitCheck(entityHitResult, thrownEnderpearl);
            }
        }
        return false;
    }
}
