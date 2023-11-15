package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Predicate;

public class RootminNearestAttackableTargetGoal  extends TargetGoal {
    private static final int DEFAULT_RANDOM_INTERVAL = 10;
    protected final TargetingConditions targetConditions;
    protected final int randomInterval;
    @Nullable
    protected LivingEntity target;

    public RootminNearestAttackableTargetGoal(Mob mob, boolean mustSee) {
        this(mob, DEFAULT_RANDOM_INTERVAL, mustSee, null);
    }

    public RootminNearestAttackableTargetGoal(Mob mob, boolean mustSee, Predicate<LivingEntity> targetPredicate) {
        this(mob, DEFAULT_RANDOM_INTERVAL, mustSee, targetPredicate);
    }

    public RootminNearestAttackableTargetGoal(Mob mob, int randomInterval, boolean mustSee, @Nullable Predicate<LivingEntity> targetPredicate) {
        super(mob, mustSee, false);
        this.randomInterval = reducedTickDelay(randomInterval);
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        this.targetConditions = TargetingConditions.forCombat().range(this.getFollowDistance()).selector(targetPredicate);
    }

    @Override
    public boolean canUse() {
        if ((this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0) ||
            ((this.mob instanceof RootminEntity rootminEntity && rootminEntity.disableAttackGoals)))
        {
            return false;
        }

        this.findTarget();
        return this.target != null;
    }

    protected AABB getTargetSearchArea(double d) {
        return this.mob.getBoundingBox().inflate(d, 4.0, d);
    }

    protected void findTarget() {
        this.target = this.mob.level().getNearestEntity(this.mob.level().getEntitiesOfClass(
                        LivingEntity.class,
                        this.getTargetSearchArea(this.getFollowDistance()),
                        livingEntity -> {
                            if (this.mob instanceof RootminEntity rootminEntity) {
                                return rootminEntity.canTarget(livingEntity);
                            }
                            return false;
                        }),
                this.targetConditions,
                this.mob,
                this.mob.getX(),
                this.mob.getEyeY(),
                this.mob.getZ());
    }


    @Override
    public void start() {
        this.mob.setTarget(this.target);
        super.start();
    }

    public void setTarget(@Nullable LivingEntity livingEntity) {
        this.target = livingEntity;
    }
}