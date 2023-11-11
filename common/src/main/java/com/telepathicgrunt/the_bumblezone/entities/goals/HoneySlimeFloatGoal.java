package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.entities.controllers.HoneySlimeMoveController;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class HoneySlimeFloatGoal extends Goal {
    private final HoneySlimeEntity slime;

    public HoneySlimeFloatGoal(HoneySlimeEntity slimeIn) {
        this.slime = slimeIn;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        slimeIn.getNavigation().setCanFloat(true);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean canUse() {
        return (this.slime.isInWater() || this.slime.isInLava()) && this.slime.getMoveControl() instanceof HoneySlimeMoveController;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        if (this.slime.getRandom().nextFloat() < 0.8F) {
            this.slime.getJumpControl().jump();
        }

        ((HoneySlimeMoveController) this.slime.getMoveControl()).setSpeed(1.2D);
    }
}
