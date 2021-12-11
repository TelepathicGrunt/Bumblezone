package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.entities.controllers.HoneySlimeMoveHelperController;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumSet;

public class FacingRevengeGoal extends HurtByTargetGoal {
    private final HoneySlimeEntity slime;

    public FacingRevengeGoal(HoneySlimeEntity slimeIn) {
        super(slimeIn);
        this.slime = slimeIn;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }
    public boolean canContinueToUse() {
        return slime.getTarget() != null && super.canContinueToUse();
    }

    public void tick() {
        if(this.slime.getTarget() != null) {
            this.slime.lookAt(this.slime.getTarget(), 10.0F, 10.0F);
        }
        ((HoneySlimeMoveHelperController) this.slime.getMoveControl()).setDirection(this.slime.getYRot(), this.slime.canDamagePlayer());
    }
}