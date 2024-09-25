package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.GameRules;

import java.util.EnumSet;
import java.util.List;

public class RootminHurtByTargetGoal extends TargetGoal {
    private static final TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
    private int timestamp;

    public RootminHurtByTargetGoal(PathfinderMob mob){
        super(mob, true);
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    public boolean canUse() {
        int lastHurtByMobTimestamp = this.mob.getLastHurtByMobTimestamp();
        LivingEntity livingEntity = this.mob.getLastHurtByMob();
        if (lastHurtByMobTimestamp != this.timestamp && livingEntity != null) {
            if (this.mob instanceof RootminEntity rootminEntity && rootminEntity.isOwnedBy(livingEntity)) {
                return false;
            }

            if (livingEntity.getType() == EntityType.PLAYER && this.mob.level().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
                return false;
            }
            else {
                if (livingEntity instanceof RootminEntity rootminEntityAttacker &&
                    this.mob instanceof RootminEntity rootminEntity &&
                    !rootminEntity.isInvulnerable())
                {
                    rootminEntity.runCurse();
                    rootminEntityAttacker.runEmbarrassed();

                    rootminEntity.disableAttackGoals = true;
                    rootminEntityAttacker.disableAttackGoals = true;

                    rootminEntity.rootminToLookAt = rootminEntityAttacker;
                    rootminEntityAttacker.rootminToLookAt = rootminEntity;

                    this.timestamp = this.mob.getLastHurtByMobTimestamp();
                    return false;
                }

                if (livingEntity.getType().is(BzTags.ROOTMIN_FORCED_DO_NOT_TARGET)) {
                    return false;
                }

                return this.canAttack(livingEntity, HURT_BY_TARGETING);
            }
        }
        else {
            return false;
        }
    }

    public void start () {
        this.mob.setTarget(this.mob.getLastHurtByMob());
        this.targetMob = this.mob.getTarget();
        this.timestamp = this.mob.getLastHurtByMobTimestamp();
        this.unseenMemoryTicks = 300;
        super.start();

        if (this.targetMob != null && this.mob instanceof RootminEntity rootminEntity && !rootminEntity.isOwnedBy(this.targetMob)) {
            List<RootminEntity> rootminEntities = this.mob.level().getEntitiesOfClass(RootminEntity.class, this.mob.getBoundingBox().inflate(30));
            for(RootminEntity rootminEntityNear : rootminEntities) {
                if (rootminEntityNear != this.mob && rootminEntityNear.getTarget() != this.targetMob && !RootminEntity.POSES_THAT_CANT_BE_MOTION_INTERRUPTED.contains(rootminEntityNear.getRootminPose())) {
                    rootminEntityNear.runAngry();
                }

                rootminEntityNear.setTarget(this.targetMob);
                rootminEntityNear.superHatedPlayer = this.targetMob.getUUID();
            }
        }
    }
}