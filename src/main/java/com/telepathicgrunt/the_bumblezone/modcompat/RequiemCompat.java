package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import ladysnake.requiem.api.v1.possession.PossessionComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;

public class RequiemCompat {

    public static void setupCompat() {
       // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.requiemPresent = true;
    }

    public static boolean isEntityUsingHostBee(Entity entity) {
        Entity possessedEntity = PossessionComponent.getHost(entity);
        return (possessedEntity instanceof Bee || possessedEntity.getType().is(BzTags.STRING_CURTAIN_BLOCKS_PATHFINDING_FOR_NON_BEE_MOB)) &&
                !possessedEntity.getType().is(BzTags.STRING_CURTAIN_FORCE_ALLOW_PATHFINDING);
    }
}
