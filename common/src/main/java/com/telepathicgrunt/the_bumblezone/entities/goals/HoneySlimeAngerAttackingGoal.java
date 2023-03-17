package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.entities.controllers.HoneySlimeMoveHelperController;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.level.GameRules;

import java.util.EnumSet;

public class HoneySlimeAngerAttackingGoal extends TargetGoal {
    private final HoneySlimeEntity slime;

    public HoneySlimeAngerAttackingGoal(HoneySlimeEntity slimeIn) {
        super(slimeIn, true);
        this.slime = slimeIn;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    public boolean canUse() {
        LivingEntity livingEntity = this.slime.getTarget();
        if (livingEntity == null) {
            this.slime.setRemainingPersistentAngerTime(0);
            return false;
        }
        return true;
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