package com.telepathicgrunt.the_bumblezone.entities.navigation;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;

public class DirectPathNavigator extends GroundPathNavigation {

    private final Mob mob;

    public DirectPathNavigator(Mob mob, Level world) {
        super(mob, world);
        this.mob = mob;
    }

    @Override
    public void tick() {
        ++this.tick;
    }

    @Override
    public boolean moveTo(double x, double y, double z, double speedIn) {
        mob.getMoveControl().setWantedPosition(x, y, z, speedIn);
        return true;
    }

    @Override
    public boolean moveTo(Entity entityIn, double speedIn) {
        mob.getMoveControl().setWantedPosition(entityIn.getX(), entityIn.getY(), entityIn.getZ(), speedIn);
        return true;
    }
}