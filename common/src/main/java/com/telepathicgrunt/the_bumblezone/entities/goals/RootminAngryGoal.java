package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminState;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class RootminAngryGoal extends Goal {
    protected final RootminEntity mob;

    public RootminAngryGoal(RootminEntity pathfinderMob) {
        this.mob = pathfinderMob;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.mob.getRootminPose() == RootminState.ANGRY;
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.getRootminPose() == RootminState.ANGRY;
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void tick() {
        if (this.mob.getTarget() != null) {
            this.mob.lookAt(this.mob.getTarget(), 60, 30);
        }
    }
}