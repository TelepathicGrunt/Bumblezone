package com.telepathicgrunt.bumblezone.entities.goals;

import com.telepathicgrunt.bumblezone.entities.controllers.HoneySlimeMoveHelperController;
import com.telepathicgrunt.bumblezone.entities.mobs.HoneySlimeEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class FloatGoal extends Goal {
    private final HoneySlimeEntity slime;

    public FloatGoal(HoneySlimeEntity slimeIn) {
        this.slime = slimeIn;
        this.setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE));
        slimeIn.getNavigation().setCanSwim(true);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean canStart() {
        return (this.slime.isTouchingWater() || this.slime.isInLava()) && this.slime.getMoveControl() instanceof HoneySlimeMoveHelperController;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        if (this.slime.getRandom().nextFloat() < 0.8F) {
            this.slime.getJumpControl().setActive();
        }

        ((HoneySlimeMoveHelperController) this.slime.getMoveControl()).setSpeed(1.2D);
    }
}
