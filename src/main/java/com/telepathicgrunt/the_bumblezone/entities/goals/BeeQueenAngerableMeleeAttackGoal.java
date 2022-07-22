package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.client.rendering.beequeen.BeeQueenPose;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class BeeQueenAngerableMeleeAttackGoal extends Goal {
    protected final BeeQueenEntity mob;
    private int ticksUntilNextAttack;

    public BeeQueenAngerableMeleeAttackGoal(BeeQueenEntity mob) {
        this.mob = mob;
    }

    public boolean canUse() {
        return this.mob.getTarget() != null;
    }

    public boolean canContinueToUse() {
        return mob.getTarget() != null && mob.getTarget().isAlive();
    }

    public void start() {
        this.ticksUntilNextAttack = 0;
    }

    public void stop() {}

    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target != null && target.isAlive()) {
            double distance = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            this.checkAndPerformAttack(this.mob.getTarget(), distance);
        }
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(20);
    }

    protected void checkAndPerformAttack(LivingEntity target, double distance) {
        double attackReachSqr1 = this.getAttackReachSqr(target);
        if (distance <= attackReachSqr1 && this.ticksUntilNextAttack <= 0) {
            this.resetAttackCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(target);
            this.mob.spawnAngryParticles(4);
            this.mob.setQueenPose(BeeQueenPose.ATTACKING);
        }
    }

    protected double getAttackReachSqr(LivingEntity livingEntity) {
        return this.mob.getBbWidth() * 1.2f;
    }
}