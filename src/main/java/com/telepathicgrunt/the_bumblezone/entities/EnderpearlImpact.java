package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.eventbus.api.Event;

public class EnderpearlImpact {

    public static void onPearlHit(EnderTeleportEvent event) {
        if(PlayerTeleportation.runEnderpearlImpact(new Vector3d(event.getTargetX(), event.getTargetY(), event.getTargetZ()), event.getEntity()))
            event.setResult(Event.Result.DENY);
    }
}
