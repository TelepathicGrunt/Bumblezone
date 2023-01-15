package com.telepathicgrunt.the_bumblezone.entities.goals;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class BeeQueenAlwaysLookAtPlayerGoal extends Goal {
    protected final Mob mob;
    protected Entity lookAt;
    protected final float lookDistance;
    private final boolean onlyHorizontal;
    protected final Class<? extends LivingEntity> lookAtType;
    protected final TargetingConditions lookAtContext;

    public BeeQueenAlwaysLookAtPlayerGoal(Mob mob, Class<? extends LivingEntity> lookAtType, float lookDistance) {
        this(mob, lookAtType, lookDistance, false);
    }

    public BeeQueenAlwaysLookAtPlayerGoal(Mob mob, Class<? extends LivingEntity> lookAtType, float lookDistance, boolean onlyHorizontal) {
        this.mob = mob;
        this.lookAtType = lookAtType;
        this.lookDistance = lookDistance;
        this.onlyHorizontal = onlyHorizontal;
        this.setFlags(EnumSet.of(Flag.LOOK));
        if (lookAtType == Player.class) {
            this.lookAtContext = TargetingConditions.forNonCombat().range(lookDistance).selector((livingEntity) -> EntitySelector.notRiding(mob).test(livingEntity));
        }
        else {
            this.lookAtContext = TargetingConditions.forNonCombat().range(lookDistance);
        }
    }

    public boolean canUse() {
        if (this.mob.getTarget() != null) {
            this.lookAt = this.mob.getTarget();
        }

        if (this.lookAtType == Player.class) {
            this.lookAt = this.mob.level.getNearestPlayer(this.lookAtContext, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        }
        else {
            this.lookAt = this.mob.level.getNearestEntity(this.mob.level.getEntitiesOfClass(this.lookAtType, this.mob.getBoundingBox().inflate(this.lookDistance, 3.0D, this.lookDistance), (livingEntity) -> true), this.lookAtContext, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        }

        return this.lookAt != null;
    }

    public boolean canContinueToUse() {
        if (!this.lookAt.isAlive()) {
            return false;
        }
        else return !(this.mob.distanceToSqr(this.lookAt) > (double) (this.lookDistance * this.lookDistance));
    }

    public void start() {
    }

    public void stop() {
        this.lookAt = null;
    }

    public void tick() {
        if (this.lookAt != null && this.lookAt.isAlive()) {
            double y = this.onlyHorizontal ? this.mob.getEyeY() : this.lookAt.getEyeY();
            this.mob.getLookControl().setLookAt(this.lookAt.getX(), y, this.lookAt.getZ(), 0.05f, 0.05f);
        }
    }
}