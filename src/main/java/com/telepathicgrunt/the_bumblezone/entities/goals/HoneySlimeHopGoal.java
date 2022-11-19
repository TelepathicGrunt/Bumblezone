package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.entities.controllers.HoneySlimeMoveHelperController;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class HoneySlimeHopGoal extends Goal {
    private final HoneySlimeEntity slime;

    public HoneySlimeHopGoal(HoneySlimeEntity slimeIn) {
        this.slime = slimeIn;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean canUse() {
        return !this.slime.isPassenger() && this.slime.getMoveControl() instanceof HoneySlimeMoveHelperController;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        ((HoneySlimeMoveHelperController) this.slime.getMoveControl()).setSpeed(1.0D);
    }
}