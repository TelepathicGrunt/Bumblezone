package com.telepathicgrunt.the_bumblezone.modcompat.quilt;

import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import ladysnake.requiem.api.v1.possession.PossessionComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;

import java.util.EnumSet;

public class RequiemCompat implements ModCompat {

    public RequiemCompat() {
        ModChecker.requiemPresent = true;
    }

    @Override
    public EnumSet<Type> compatTypes() {
        return EnumSet.of(Type.HAS_HOST_BEES);
    }

    @Override
    public boolean isHostBee(Entity entity) {
        Entity possessedEntity = PossessionComponent.getHost(entity);
        return possessedEntity != null &&
                (possessedEntity instanceof Bee && possessedEntity.getType().is(BzTags.STRING_CURTAIN_BLOCKS_PATHFINDING_FOR_NON_BEE_MOB)) &&
                !possessedEntity.getType().is(BzTags.STRING_CURTAIN_FORCE_ALLOW_PATHFINDING);
    }
}