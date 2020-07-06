package net.telepathicgrunt.bumblezone.entities.goals;

import com.bagel.buzzierbees.common.entities.HoneySlimeEntity;
import com.bagel.buzzierbees.common.entities.controllers.HoneySlimeMoveHelperController;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;

public class RevengeGoal extends Goal {
    private final HoneySlimeEntity slime;
    private int growTieredTimer;

    public RevengeGoal(HoneySlimeEntity slimeIn) {
        this.slime = slimeIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    public boolean shouldExecute() {
        LivingEntity livingentity = this.slime.getRevengeTarget();
        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else {
            return livingentity instanceof PlayerEntity && ((PlayerEntity) livingentity).abilities.disableDamage ? false : this.slime.getMoveHelper() instanceof HoneySlimeMoveHelperController;
        }
    }

    public void startExecuting() {
        this.growTieredTimer = 600;
        super.startExecuting();
    }

    public boolean shouldContinueExecuting() {
        LivingEntity livingentity = this.slime.getRevengeTarget();
        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else if (livingentity instanceof PlayerEntity && ((PlayerEntity) livingentity).abilities.disableDamage) {
            return false;
        } else {
            if (--this.growTieredTimer > 0) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    public void tick() {
        this.slime.faceEntity(this.slime.getRevengeTarget(), 10.0F, 10.0F);
        ((HoneySlimeMoveHelperController) this.slime.getMoveHelper()).setDirection(this.slime.rotationYaw, this.slime.canDamagePlayer());
    }
}