package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class RootminRangedAttackGoal extends Goal {
    private final RootminEntity rootminEntity;
    @Nullable
    private LivingEntity target;
    private int attackTime = -1;
    private final double speedModifier;
    private int seeTime;
    private final int attackIntervalMin;
    private final int attackIntervalMax;
    private final float attackRadius;
    private final float attackRadiusSqr;

    public RootminRangedAttackGoal(RootminEntity rootminEntity, double speedModifier, int attackIntervalMax, int attackIntervalMin, float attackRadius) {
        this.rootminEntity = rootminEntity;
        this.speedModifier = speedModifier;
        this.attackIntervalMin = attackIntervalMax;
        this.attackIntervalMax = attackIntervalMin;
        this.attackRadius = attackRadius;
        this.attackRadiusSqr = attackRadius * attackRadius;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity livingEntity = this.rootminEntity.getTarget();
        if (livingEntity == null || !livingEntity.isAlive() || rootminEntity.disableAttackGoals) {
            return false;
        }

        if (!rootminEntity.canTarget(livingEntity)) {
            rootminEntity.setTarget(null);
            rootminEntity.setAggressive(false);
            return false;
        }

        this.target = livingEntity;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse() || (this.target != null && this.target.isAlive()) && !this.rootminEntity.getNavigation().isDone();
    }

    @Override
    public void stop() {
        this.target = null;
        this.seeTime = 0;
        this.attackTime = -1;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.target == null) {
            return;
        }

        Level level = this.rootminEntity.level();
        BlockPos posInFront = this.rootminEntity.blockPosition().relative(this.rootminEntity.getDirection());

        double distToTarget = this.rootminEntity.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        boolean blockBlockingInFront = level.getBlockState(posInFront).isCollisionShapeFullBlock(level, posInFront);
        boolean lineOfSight = !blockBlockingInFront && this.rootminEntity.getSensing().hasLineOfSight(this.target);

        this.seeTime = lineOfSight ? ++this.seeTime : 0;
        if (distToTarget > (double) this.attackRadiusSqr || this.seeTime < 5) {
            this.rootminEntity.getNavigation().moveTo(this.target, this.speedModifier);
        }
        else {
            this.rootminEntity.getNavigation().stop();
        }

        this.rootminEntity.getLookControl().setLookAt(this.target, 30.0f, 30.0f);
        if (--this.attackTime == 0) {
            if (!lineOfSight) {
                return;
            }
            float f = (float) Math.sqrt(distToTarget) / this.attackRadius;
            this.rootminEntity.runShoot(this.target, 1, false);
            this.rootminEntity.exposedTimer = 0;
            this.attackTime = Mth.floor(f * (float) (this.attackIntervalMax - this.attackIntervalMin) + (float) this.attackIntervalMin);
        }
        else if (this.attackTime < 0) {
            this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(distToTarget) / (double) this.attackRadius, this.attackIntervalMin, this.attackIntervalMax));
        }
    }
}