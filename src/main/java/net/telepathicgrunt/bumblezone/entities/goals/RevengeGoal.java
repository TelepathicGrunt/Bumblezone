package net.telepathicgrunt.bumblezone.entities.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.telepathicgrunt.bumblezone.entities.controllers.HoneySlimeMoveHelperController;
import net.telepathicgrunt.bumblezone.entities.mobs.HoneySlimeEntity;

import java.util.EnumSet;

public class RevengeGoal extends Goal {
    private final HoneySlimeEntity slime;
    private int growTieredTimer;

    public RevengeGoal(HoneySlimeEntity slimeIn) {
        this.slime = slimeIn;
        this.setControls(EnumSet.of(Goal.Control.LOOK));
    }

    public boolean canStart() {
        LivingEntity livingentity = this.slime.getAttacker();
        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else {
            return (!(livingentity instanceof PlayerEntity) || !((PlayerEntity) livingentity).abilities.invulnerable) && this.slime.getMoveControl() instanceof HoneySlimeMoveHelperController;
        }
    }

    public void start() {
        this.growTieredTimer = 600;
        super.start();
    }

    public boolean shouldContinue() {
        LivingEntity livingentity = this.slime.getAttacker();
        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else if (livingentity instanceof PlayerEntity && ((PlayerEntity) livingentity).abilities.invulnerable) {
            return false;
        } else {
            return --this.growTieredTimer > 0;
        }
    }

    public void tick() {
        this.slime.lookAtEntity(this.slime.getAttacker(), 10.0F, 10.0F);
        ((HoneySlimeMoveHelperController) this.slime.getMoveControl()).setDirection(this.slime.yaw, this.slime.canDamagePlayer());
    }
}