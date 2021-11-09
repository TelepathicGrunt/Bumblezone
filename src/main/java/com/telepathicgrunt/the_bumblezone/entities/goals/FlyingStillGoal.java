package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.mixin.entities.LivingEntityAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;

public class FlyingStillGoal extends Goal {
    private final TameableEntity mob;

    public FlyingStillGoal(TameableEntity p_i1654_1_) {
        this.mob = p_i1654_1_;
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
        MovementController moveControl = this.mob.getMoveControl();
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