package com.telepathicgrunt.bumblezone.entities.goals;

import com.telepathicgrunt.bumblezone.entities.controllers.HoneySlimeMoveHelperController;
import com.telepathicgrunt.bumblezone.entities.mobs.HoneySlimeEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.RevengeGoal;

import java.util.EnumSet;

public class FacingRevengeGoal extends RevengeGoal {
    private final HoneySlimeEntity slime;

    public FacingRevengeGoal(HoneySlimeEntity slimeIn) {
        super(slimeIn);
        this.slime = slimeIn;
        this.setControls(EnumSet.of(Goal.Control.LOOK));
    }
    public boolean shouldContinue() {
        return slime.getTarget() != null && super.shouldContinue();
    }

    public void tick() {
        this.slime.lookAtEntity(this.slime.getTarget(), 10.0F, 10.0F);
        ((HoneySlimeMoveHelperController) this.slime.getMoveControl()).setDirection(this.slime.getYaw(), this.slime.canDamagePlayer());
    }
}