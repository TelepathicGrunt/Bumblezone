package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.entities.controllers.HoneySlimeMoveHelperController;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.EnumSet;
import java.util.List;

public class HoneySlimeBreedGoal extends Goal {
    private static final TargetingConditions TARGETING_CONDITIONS = (TargetingConditions.forCombat()).range(8.0D).ignoreLineOfSight();
    protected final HoneySlimeEntity slime;
    private final Class<? extends Animal> mateClass;
    protected final Level world;
    protected Animal nearbyMate;
    private int spawnBabyDelay;

    public HoneySlimeBreedGoal(HoneySlimeEntity honeySlimeEntity, double speedIn) {
        this(honeySlimeEntity, speedIn, honeySlimeEntity.getClass());
    }

    public HoneySlimeBreedGoal(HoneySlimeEntity honeySlimeEntity, double speedIn, Class<? extends Animal> mateClass) {
        this.slime = honeySlimeEntity;
        this.world = honeySlimeEntity.level();
        this.mateClass = mateClass;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean canUse() {
        if (!this.slime.isInLove()) {
            return false;
        } else {
            this.nearbyMate = this.getNearbyMate();
            return this.nearbyMate != null && this.slime.getMoveControl() instanceof HoneySlimeMoveHelperController;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean canContinueToUse() {
        return this.nearbyMate.isAlive() && this.nearbyMate.isInLove() && this.spawnBabyDelay < 60;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        this.nearbyMate = null;
        this.spawnBabyDelay = 0;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        this.slime.getLookControl().setLookAt(this.nearbyMate, 10.0F, (float)this.slime.getMaxHeadXRot());

        this.slime.lookAt(this.nearbyMate, 10.0F, 10.0F);
        ((HoneySlimeMoveHelperController) this.slime.getMoveControl()).setDirection(this.slime.getYRot(), true);
        ((HoneySlimeMoveHelperController) this.slime.getMoveControl()).setSpeed(1.0D);

        ++this.spawnBabyDelay;
        if (this.spawnBabyDelay >= 60 && this.slime.distanceToSqr(this.nearbyMate) < 9.0D) {
            this.spawnBaby();
        }

    }

    /**
     * Loops through nearby animals and finds another animal of the same type that can be mated with. Returns the first
     * valid mate found.
     */
    private Animal getNearbyMate() {
        List<? extends Animal> list = this.world.getNearbyEntities(this.mateClass, TARGETING_CONDITIONS, this.slime, this.slime.getBoundingBox().inflate(8.0D));
        double d0 = Double.MAX_VALUE;
        Animal animalentity = null;

        for(Animal animalentity1 : list) {
            if (this.slime.canMate(animalentity1) && this.slime.distanceToSqr(animalentity1) < d0) {
                animalentity = animalentity1;
                d0 = this.slime.distanceToSqr(animalentity1);
            }
        }

        return animalentity;
    }

    /**
     * Spawns a baby animal of the same type.
     */
    protected void spawnBaby() {
        AgeableMob childEntity = this.slime.getBreedOffspring((ServerLevel)this.world, this.nearbyMate);
        if (childEntity != null) {
            ServerPlayer serverPlayer = this.slime.getLoveCause();
            if (serverPlayer == null && this.nearbyMate.getLoveCause() != null) {
                serverPlayer = this.nearbyMate.getLoveCause();
            }

            if (serverPlayer != null) {
                serverPlayer.awardStat(Stats.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger(serverPlayer, this.slime, this.nearbyMate, childEntity);
            }

            this.slime.setAge(6000);
            this.nearbyMate.setAge(6000);
            this.slime.resetLove();
            this.nearbyMate.resetLove();
            childEntity.setAge(-24000);
            childEntity.moveTo(this.slime.getX(), this.slime.getY(), this.slime.getZ(), 0.0F, 0.0F);

            PlatformHooks.finalizeSpawn(childEntity, (ServerLevelAccessor) world, null, MobSpawnType.BREEDING, null);
            this.world.addFreshEntity(childEntity);
            this.world.broadcastEntityEvent(this.slime, (byte) 18);
            if (this.world.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                this.world.addFreshEntity(new ExperienceOrb(this.world, this.slime.getX(), this.slime.getY(), this.slime.getZ(), this.slime.getRandom().nextInt(7) + 1));
            }
        }
    }
}
