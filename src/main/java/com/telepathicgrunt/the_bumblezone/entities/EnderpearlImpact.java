package com.telepathicgrunt.the_bumblezone.entities;

import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.EntityTeleportEvent;
import net.minecraftforge.eventbus.api.Event;

public class EnderpearlImpact {

    public static void onPearlHit(EntityTeleportEvent.EnderPearl event) {
        if(PlayerTeleportation.runEnderpearlImpact(new Vector3d(event.getTargetX(), event.getTargetY(), event.getTargetZ()), event.getEntity()))
            event.setResult(Event.Result.DENY);
    }
}
