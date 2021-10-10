package com.telepathicgrunt.bumblezone.entities.goals;

import com.telepathicgrunt.bumblezone.entities.controllers.HoneySlimeMoveHelperController;
import com.telepathicgrunt.bumblezone.entities.mobs.HoneySlimeEntity;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public class BreedGoal extends Goal {
    private static final TargetingConditions field_220689_d = (TargetingConditions.forCombat()).range(8.0D).ignoreLineOfSight();
    protected final HoneySlimeEntity slime;
    private final Class<? extends Animal> mateClass;
    protected final Level world;
    protected Animal field_75391_e;
    private int spawnBabyDelay;

    public BreedGoal(HoneySlimeEntity animal, double speedIn) {
        this(animal, speedIn, animal.getClass());
    }

    public BreedGoal(HoneySlimeEntity p_i47306_1_, double p_i47306_2_, Class<? extends Animal> p_i47306_4_) {
        this.slime = p_i47306_1_;
        this.world = p_i47306_1_.level;
        this.mateClass = p_i47306_4_;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean canUse() {
        if (!this.slime.isInLove()) {
            return false;
        } else {
            this.field_75391_e = this.getNearbyMate();
            return this.field_75391_e != null && this.slime.getMoveControl() instanceof HoneySlimeMoveHelperController;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean canContinueToUse() {
        return this.field_75391_e.isAlive() && this.field_75391_e.isInLove() && this.spawnBabyDelay < 60;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        this.field_75391_e = null;
        this.spawnBabyDelay = 0;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        this.slime.getLookControl().setLookAt(this.field_75391_e, 10.0F, (float)this.slime.getMaxHeadXRot());

        this.slime.lookAt(this.field_75391_e, 10.0F, 10.0F);
        ((HoneySlimeMoveHelperController) this.slime.getMoveControl()).setDirection(this.slime.getYRot(), true);
        ((HoneySlimeMoveHelperController) this.slime.getMoveControl()).setSpeed(1.0D);

        ++this.spawnBabyDelay;
        if (this.spawnBabyDelay >= 60 && this.slime.distanceToSqr(this.field_75391_e) < 9.0D) {
            this.spawnBaby();
        }

    }

    /**
     * Loops through nearby animals and finds another animal of the same type that can be mated with. Returns the first
     * valid mate found.
     */
    private Animal getNearbyMate() {
        List<? extends Animal> list = this.world.getNearbyEntities(this.mateClass, field_220689_d, this.slime, this.slime.getBoundingBox().inflate(8.0D));
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
        AgeableMob childEntity = this.slime.getBreedOffspring((ServerLevel)this.world, this.field_75391_e);
        if (childEntity != null) {
            ServerPlayer serverplayerentity = this.slime.getLoveCause();
            if (serverplayerentity == null && this.field_75391_e.getLoveCause() != null) {
                serverplayerentity = this.field_75391_e.getLoveCause();
            }

            if (serverplayerentity != null) {
                serverplayerentity.awardStat(Stats.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger(serverplayerentity, this.slime, this.field_75391_e, childEntity);
            }

            this.slime.setAge(6000);
            this.field_75391_e.setAge(6000);
            this.slime.resetLove();
            this.field_75391_e.resetLove();
            childEntity.setAge(-24000);
            childEntity.moveTo(this.slime.getX(), this.slime.getY(), this.slime.getZ(), 0.0F, 0.0F);
            this.world.addFreshEntity(childEntity);
            this.world.broadcastEntityEvent(this.slime, (byte)18);
            if (this.world.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                this.world.addFreshEntity(new ExperienceOrb(this.world, this.slime.getX(), this.slime.getY(), this.slime.getZ(), this.slime.getRandom().nextInt(7) + 1));
            }

        }
    }
}
