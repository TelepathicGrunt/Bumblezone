package com.telepathicgrunt.bumblezone.entities.goals;

import net.minecraft.entity.ai.goal.Goal;
import com.telepathicgrunt.bumblezone.entities.controllers.HoneySlimeMoveHelperController;
import com.telepathicgrunt.bumblezone.entities.mobs.HoneySlimeEntity;

import java.util.EnumSet;

public class HopGoal extends Goal {
    private final HoneySlimeEntity slime;

    public HopGoal(HoneySlimeEntity slimeIn) {
        this.slime = slimeIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        return !this.slime.isPassenger() && this.slime.getMoveHelper() instanceof HoneySlimeMoveHelperController;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        ((HoneySlimeMoveHelperController) this.slime.getMoveHelper()).setSpeed(1.0D);
    }
}