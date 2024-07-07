package com.telepathicgrunt.the_bumblezone.events.entity;

import com.telepathicgrunt.the_bumblezone.events.base.CancellableEventHandler;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;

public record BzProjectileHitEvent(Projectile projectile, HitResult hitResult) {

    public static final CancellableEventHandler<BzProjectileHitEvent> EVENT = new CancellableEventHandler<>();
    public static final CancellableEventHandler<BzProjectileHitEvent> EVENT_HIGH = new CancellableEventHandler<>();

}
