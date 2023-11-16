package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.client.rendering.rootmin.RootminPose;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import com.telepathicgrunt.the_bumblezone.items.BeeArmor;
import com.telepathicgrunt.the_bumblezone.items.FlowerHeadwearHelmet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;

public class RootminCuriosityGoal extends Goal {
    protected final RootminEntity mob;
    protected final TargetingConditions targetConditions;
    protected boolean isCuriousNow = false;
    protected LivingEntity inspect = null;
    protected int exposingTiming = 0;
    protected int curiosityWaiting = 0;

    public RootminCuriosityGoal(RootminEntity pathfinderMob) {
        this.mob = pathfinderMob;
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.targetConditions = TargetingConditions.forCombat().range(this.mob.getAttributeValue(Attributes.FOLLOW_RANGE)).selector(null);
    }

    protected AABB getTargetSearchArea(double d) {
        return this.mob.getBoundingBox().inflate(d, 4.0, d);
    }

    @Override
    public boolean canUse() {
        if (this.mob.getRootminPose() != RootminPose.ENTITY_TO_BLOCK || this.mob.animationTimeBetweenHiding != 0) {
            return false;
        }

        if (this.mob.getTarget() != null) {
            return false;
        }

        if (this.mob.curiosityCooldown > 0) {
            return false;
        }

        if (this.mob.getUUID().getLeastSignificantBits() + this.mob.tickCount % 20 != 0) {
            return false;
        }

        double seeRange = this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
        this.inspect = this.mob.level().getNearestEntity(this.mob.level().getEntitiesOfClass(
                        LivingEntity.class,
                        this.getTargetSearchArea(seeRange),
                        livingEntity -> BeeArmor.getBeeThemedWearablesCount(livingEntity) > 0 || !FlowerHeadwearHelmet.getFlowerHeadwear(livingEntity).isEmpty()),
                this.targetConditions,
                this.mob,
                this.mob.getX(),
                this.mob.getEyeY(),
                this.mob.getZ());

        if (this.inspect == null) {
            return false;
        }

        return this.inspect.distanceToSqr(this.mob) <= 32 * 32;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.isDeadOrDying() &&
                this.inspect != null &&
                !this.inspect.isDeadOrDying() &&
                (!this.isCuriousNow || this.curiosityWaiting > 0 || this.mob.getRootminPose() == RootminPose.CURIOUS) &&
                (BeeArmor.getBeeThemedWearablesCount(this.inspect) > 0 || !FlowerHeadwearHelmet.getFlowerHeadwear(this.inspect).isEmpty()) &&
                this.mob.blockPosition().distManhattan(this.inspect.blockPosition()) < 32;
    }

    @Override
    public void start() {
        if (this.mob.getRootminPose() == RootminPose.ENTITY_TO_BLOCK) {
            this.mob.exposeFromBlock();
            this.exposingTiming = 20;
        }
        this.curiosityWaiting = 0;
        this.isCuriousNow = false;
    }

    @Override
    public void stop() {
        this.mob.curiosityCooldown = 600;
        this.mob.exposedTimer = 0;
        this.exposingTiming = 0;
        this.curiosityWaiting = 0;
        this.mob.setRootminPose(RootminPose.NONE);
    }

    @Override
    public void tick() {
        if (this.exposingTiming > 0) {
            exposingTiming--;
        }
        else if (this.curiosityWaiting > 0) {
            this.curiosityWaiting--;
            this.mob.lookAt(this.inspect, 60, 30);
        }
        else if (!this.isCuriousNow) {
            if (this.mob.distanceToSqr(this.inspect) < 5 * 5) {
                this.mob.getNavigation().stop();
                this.isCuriousNow = true;
                this.mob.runCurious();
                this.curiosityWaiting = 60;
            }
            else {
                this.mob.getNavigation().moveTo(this.inspect, 1);
            }
        }
    }
}