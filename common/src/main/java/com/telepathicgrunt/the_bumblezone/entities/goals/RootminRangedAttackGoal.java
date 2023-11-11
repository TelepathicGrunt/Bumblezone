package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class RootminRangedAttackGoal extends Goal {
    private final Mob mob;
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

    public RootminRangedAttackGoal(RootminEntity rootminEntity, double speedModifier, int attackInterval, float attackRadius) {
        this(rootminEntity, speedModifier, attackInterval, attackInterval, attackRadius);
    }

    public RootminRangedAttackGoal(RootminEntity rootminEntity, double speedModifier, int attackIntervalMax, int attackIntervalMin, float attackRadius) {
        this.rootminEntity = rootminEntity;
        this.mob = rootminEntity;
        this.speedModifier = speedModifier;
        this.attackIntervalMin = attackIntervalMax;
        this.attackIntervalMax = attackIntervalMin;
        this.attackRadius = attackRadius;
        this.attackRadiusSqr = attackRadius * attackRadius;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity livingEntity = this.mob.getTarget();
        if (livingEntity == null ||
            !livingEntity.isAlive() ||
            (this.mob instanceof RootminEntity rootminEntity && rootminEntity.disableAttackGoals))
        {
            return false;
        }

        if (this.mob instanceof RootminEntity rootminEntity && !rootminEntity.canTarget(livingEntity)) {
            rootminEntity.setTarget(null);
            rootminEntity.setAggressive(false);
            return false;
        }

        this.target = livingEntity;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse() || this.target.isAlive() && !this.mob.getNavigation().isDone();
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
        double d = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        boolean bl = this.mob.getSensing().hasLineOfSight(this.target);

        Level level = this.mob.level();
        BlockPos posInFront = this.mob.blockPosition().relative(this.mob.getDirection());
        boolean blockBlockingInFront = level.getBlockState(posInFront).isCollisionShapeFullBlock(level, posInFront);

        this.seeTime = (bl && !blockBlockingInFront) ? ++this.seeTime : 0;
        if (d > (double) this.attackRadiusSqr || this.seeTime < 5) {
            this.mob.getNavigation().moveTo(this.target, this.speedModifier);
        }
        else {
            this.mob.getNavigation().stop();
        }

        this.mob.getLookControl().setLookAt(this.target, 30.0f, 30.0f);
        if (--this.attackTime == 0) {
            if (!bl || blockBlockingInFront) {
                return;
            }
            float f = (float) Math.sqrt(d) / this.attackRadius;
            this.rootminEntity.runShoot(this.target, 1, false);
            this.rootminEntity.exposedTimer = 0;
            this.attackTime = Mth.floor(f * (float) (this.attackIntervalMax - this.attackIntervalMin) + (float) this.attackIntervalMin);
        }
        else if (this.attackTime < 0) {
            this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(d) / (double) this.attackRadius, this.attackIntervalMin, this.attackIntervalMax));
        }
    }
}