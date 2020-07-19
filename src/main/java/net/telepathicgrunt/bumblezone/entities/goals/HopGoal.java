package net.telepathicgrunt.bumblezone.entities.goals;

import net.minecraft.entity.ai.goal.Goal;
import net.telepathicgrunt.bumblezone.entities.controllers.HoneySlimeMoveHelperController;
import net.telepathicgrunt.bumblezone.entities.mobs.HoneySlimeEntity;

import java.util.EnumSet;

public class HopGoal extends Goal {
    private final HoneySlimeEntity slime;

    public HopGoal(HoneySlimeEntity slimeIn) {
        this.slime = slimeIn;
        this.setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean canStart() {
        return !this.slime.hasVehicle() && this.slime.getMoveControl() instanceof HoneySlimeMoveHelperController;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        ((HoneySlimeMoveHelperController) this.slime.getMoveControl()).setSpeed(1.0D);
    }
}