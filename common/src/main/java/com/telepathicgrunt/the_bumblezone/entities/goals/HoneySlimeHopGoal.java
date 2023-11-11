package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.entities.controllers.HoneySlimeMoveController;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class HoneySlimeHopGoal extends Goal {
    private final HoneySlimeEntity slime;

    public HoneySlimeHopGoal(HoneySlimeEntity slimeIn) {
        this.slime = slimeIn;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean canUse() {
        return !this.slime.isPassenger() && this.slime.getMoveControl() instanceof HoneySlimeMoveController;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        ((HoneySlimeMoveController) this.slime.getMoveControl()).setSpeed(1.0D);
    }
}