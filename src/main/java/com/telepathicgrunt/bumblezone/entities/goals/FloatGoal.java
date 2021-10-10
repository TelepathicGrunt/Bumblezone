package com.telepathicgrunt.bumblezone.entities.goals;

import com.telepathicgrunt.bumblezone.entities.controllers.HoneySlimeMoveHelperController;
import com.telepathicgrunt.bumblezone.entities.mobs.HoneySlimeEntity;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;

public class FloatGoal extends Goal {
    private final HoneySlimeEntity slime;

    public FloatGoal(HoneySlimeEntity slimeIn) {
        this.slime = slimeIn;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        slimeIn.getNavigation().setCanFloat(true);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean canUse() {
        return (this.slime.isInWater() || this.slime.isInLava()) && this.slime.getMoveControl() instanceof HoneySlimeMoveHelperController;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        if (this.slime.getRandom().nextFloat() < 0.8F) {
            this.slime.getJumpControl().jump();
        }

        ((HoneySlimeMoveHelperController) this.slime.getMoveControl()).setSpeed(1.2D);
    }
}
