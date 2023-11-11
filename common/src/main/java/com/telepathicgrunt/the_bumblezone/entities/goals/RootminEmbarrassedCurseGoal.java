package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.client.rendering.rootmin.RootminPose;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class RootminEmbarrassedCurseGoal extends Goal {
    protected final RootminEntity mob;
    protected int timer = 0;

    public RootminEmbarrassedCurseGoal(RootminEntity pathfinderMob) {
        this.mob = pathfinderMob;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        boolean isCursing = this.mob.getRootminPose() == RootminPose.CURSE;
        boolean isEmbarrassed = this.mob.getRootminPose() == RootminPose.EMBARRASSED;
        return this.mob.rootminToLookAt != null && (isCursing || isEmbarrassed);
    }

    @Override
    public boolean canContinueToUse() {
        return this.timer > 0 &&
                this.mob.rootminToLookAt != null &&
                !this.mob.isDeadOrDying() &&
                (this.mob.getRootminPose() == RootminPose.CURSE || this.mob.getRootminPose() == RootminPose.EMBARRASSED);
    }

    @Override
    public void start() {
        this.timer = 40;
    }

    @Override
    public void stop() {
        this.timer = 0;
        this.mob.disableAttackGoals = false;
        this.mob.rootminToLookAt = null;
        this.mob.exposedTimer = 0;
        this.mob.setRootminPose(RootminPose.NONE);
    }

    @Override
    public void tick() {
        this.mob.getNavigation().stop();
        if (this.mob.rootminToLookAt == null) {
            this.timer = 0;
        }
        else {
            this.mob.lookAt(this.mob.rootminToLookAt, 30, 30);
            this.timer--;
        }
    }
}