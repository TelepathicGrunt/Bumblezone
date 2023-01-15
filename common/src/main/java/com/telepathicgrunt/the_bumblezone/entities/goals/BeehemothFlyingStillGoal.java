package com.telepathicgrunt.the_bumblezone.entities.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class BeehemothFlyingStillGoal extends Goal {
    private final TamableAnimal mob;

    public BeehemothFlyingStillGoal(TamableAnimal tamableAnimal) {
        this.mob = tamableAnimal;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    public boolean canContinueToUse() {
        return this.mob.isOrderedToSit();
    }

    public boolean canUse() {
        if (!this.mob.isTame()) {
            return false;
        }
        else if (this.mob.isInWaterOrBubble()) {
            return false;
        }
        else {
            LivingEntity livingentity = this.mob.getOwner();
            if (livingentity == null) {
                return true;
            }
            else {
                return (!(this.mob.distanceToSqr(livingentity) < 224.0D) || livingentity.getLastHurtByMob() == null) && this.mob.isOrderedToSit();
            }
        }
    }

    @Override
    public void tick() {
        this.mob.setOnGround(false);
        MoveControl moveControl = this.mob.getMoveControl();
        moveControl.setWantedPosition(
            moveControl.getWantedX(),
            moveControl.getWantedY(),
            moveControl.getWantedZ(),
            moveControl.getSpeedModifier() * 0.5D
        );
    }

    public void start() {
        this.mob.getNavigation().stop();
        this.mob.setInSittingPose(true);
    }

    public void stop() {
        this.mob.setInSittingPose(false);
    }
}