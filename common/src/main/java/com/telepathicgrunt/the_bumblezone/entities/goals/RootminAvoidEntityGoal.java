package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.client.rendering.rootmin.RootminPose;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Predicate;

public class RootminAvoidEntityGoal extends Goal {
    protected final RootminEntity mob;
    private final double walkSpeedModifier;
    private final double sprintSpeedModifier;
    @Nullable
    protected LivingEntity toAvoid;
    protected final float maxDist;
    @Nullable
    protected Path path;
    protected final PathNavigation pathNav;
    protected final TagKey<EntityType<?>> avoidTag;
    protected final Predicate<LivingEntity> avoidPredicate;
    protected final Predicate<LivingEntity> predicateOnAvoidEntity;
    private final TargetingConditions avoidEntityTargeting;
    protected float fleeTime = 0;

    public RootminAvoidEntityGoal(RootminEntity pathfinderMob, TagKey<EntityType<?>> typeTagKey, float range, double walkSpeedModifier, double runSpeedModifier) {
        this(pathfinderMob, typeTagKey, livingEntity -> true, range, walkSpeedModifier, runSpeedModifier, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
    }

    public RootminAvoidEntityGoal(RootminEntity pathfinderMob, TagKey<EntityType<?>> typeTagKey, Predicate<LivingEntity> predicate, float range, double walkSpeedModifier, double runSpeedModifier, Predicate<LivingEntity> predicate2) {
        this.mob = pathfinderMob;
        this.avoidTag = typeTagKey;
        this.avoidPredicate = predicate;
        this.maxDist = range;
        this.walkSpeedModifier = walkSpeedModifier;
        this.sprintSpeedModifier = runSpeedModifier;
        this.predicateOnAvoidEntity = predicate2;
        this.pathNav = pathfinderMob.getNavigation();
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        this.avoidEntityTargeting = TargetingConditions.forCombat().range(range).selector(predicate2.and(predicate));
    }

    @Override
    public boolean canUse() {
        if (this.mob.animationTimeBetweenHiding > 0 || this.mob.isHidden) {
            return false;
        }

        if (this.mob.tickCount - this.mob.getLastHurtByMobTimestamp() < 1200 && this.mob.attackerMemory != null) {
            int distApart = this.mob.attackerMemory.blockPosition().distManhattan(this.mob.blockPosition());
            if (distApart < this.maxDist) {
                this.toAvoid = this.mob.attackerMemory;
            }
        }
        else if (this.mob.tickCount - this.mob.getLastHurtByMobTimestamp() < 1200 &&
                this.mob.getLastHurtByMob() != null &&
                !(this.mob.getLastHurtByMob() instanceof RootminEntity))
        {
            int distApart = this.mob.getLastHurtByMob().blockPosition().distManhattan(this.mob.blockPosition());
            if (distApart < this.maxDist) {
                this.toAvoid = this.mob.getLastHurtByMob();
                this.mob.attackerMemory = this.mob.getLastHurtByMob();
            }
        }
        else {
            this.toAvoid = this.mob.level().getNearestEntity(this.mob.level().getEntitiesOfClass(
                            LivingEntity.class,
                            this.mob.getBoundingBox().inflate(this.maxDist, 3.0, this.maxDist),
                            livingEntity -> livingEntity.getType().is(this.avoidTag)),
                    this.avoidEntityTargeting,
                    this.mob,
                    this.mob.getX(),
                    this.mob.getY(),
                    this.mob.getZ());
        }

        if (this.toAvoid == null) {
            return false;
        }
        Vec3 vec3 = null;
        for (int i = 0; i < 10; i++) {
            vec3 = DefaultRandomPos.getPosAway(this.mob, 26, 8, this.toAvoid.position());
            if (vec3 != null) {
                break;
            }
        }
        if (vec3 == null) {
            return false;
        }
        if (this.toAvoid.distanceToSqr(vec3.x, vec3.y, vec3.z) < this.toAvoid.distanceToSqr(this.mob)) {
            return false;
        }
        this.path = this.pathNav.createPath(vec3.x, vec3.y, vec3.z, 0);
        return this.path != null && this.path.getNodeCount() > 1;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.pathNav.isDone() || (this.toAvoid != null && this.toAvoid.isDeadOrDying())) {
            return false;
        }
        else if (this.mob.animationTimeBetweenHiding > 0 || this.mob.isHidden) {
            return false;
        }
        else if (this.fleeTime > 200) {
            return false;
        }
        else {
            this.mob.rootminToLookAt = null;
            return true;
        }
    }

    @Override
    public void start() {
        this.fleeTime = 0;
        this.pathNav.moveTo(this.path, this.mob.distanceToSqr(this.toAvoid) < (14 * 14) ? this.sprintSpeedModifier : this.walkSpeedModifier);
    }

    @Override
    public void stop() {
        this.fleeTime = 0;
        this.pathNav.stop();
        this.toAvoid = null;
    }

    @Override
    public void tick() {
        if (this.mob.distanceToSqr(this.toAvoid) < (14 * 14)) {
            this.mob.getNavigation().setSpeedModifier(this.sprintSpeedModifier);

            if (RootminEntity.POSES_THAT_CAN_BE_FEAR_INTERRUPTED.contains(this.mob.getRootminPose())) {
                this.mob.setRootminPose(RootminPose.RUN);
            }
        }
        else {
            this.mob.getNavigation().setSpeedModifier(this.walkSpeedModifier);

            if (RootminEntity.POSES_THAT_CAN_BE_FEAR_INTERRUPTED.contains(this.mob.getRootminPose())) {
                this.mob.setRootminPose(RootminPose.WALK);
            }
        }

        RootminEntity.considerHiddenRootminsInPath(this.path, this.mob);
        RootminEntity.jumpFix(this.path, this.mob);

        if (this.mob.hurtTime > 0) {
            this.fleeTime = 0;
        }
        this.fleeTime++;
    }
}