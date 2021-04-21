package com.telepathicgrunt.bumblezone.entities.goals;

import com.telepathicgrunt.bumblezone.entities.controllers.HoneySlimeMoveHelperController;
import com.telepathicgrunt.bumblezone.entities.mobs.HoneySlimeEntity;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.List;

public class BreedGoal extends Goal {
    private static final TargetPredicate field_220689_d = (new TargetPredicate()).setBaseMaxDistance(8.0D).includeInvulnerable().includeTeammates().includeHidden();
    protected final HoneySlimeEntity slime;
    private final Class<? extends AnimalEntity> mateClass;
    protected final World world;
    protected AnimalEntity field_75391_e;
    private int spawnBabyDelay;

    public BreedGoal(HoneySlimeEntity animal, double speedIn) {
        this(animal, speedIn, animal.getClass());
    }

    public BreedGoal(HoneySlimeEntity p_i47306_1_, double p_i47306_2_, Class<? extends AnimalEntity> p_i47306_4_) {
        this.slime = p_i47306_1_;
        this.world = p_i47306_1_.world;
        this.mateClass = p_i47306_4_;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean canStart() {
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
    public boolean shouldContinue() {
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
        this.slime.getLookControl().lookAt(this.field_75391_e, 10.0F, (float)this.slime.getLookPitchSpeed());

        this.slime.lookAtEntity(this.field_75391_e, 10.0F, 10.0F);
        ((HoneySlimeMoveHelperController) this.slime.getMoveControl()).setDirection(this.slime.yaw, true);
        ((HoneySlimeMoveHelperController) this.slime.getMoveControl()).setSpeed(1.0D);

        ++this.spawnBabyDelay;
        if (this.spawnBabyDelay >= 60 && this.slime.squaredDistanceTo(this.field_75391_e) < 9.0D) {
            this.spawnBaby();
        }

    }

    /**
     * Loops through nearby animals and finds another animal of the same type that can be mated with. Returns the first
     * valid mate found.
     */
    private AnimalEntity getNearbyMate() {
        List<AnimalEntity> list = this.world.getTargets(this.mateClass, field_220689_d, this.slime, this.slime.getBoundingBox().expand(8.0D));
        double d0 = Double.MAX_VALUE;
        AnimalEntity animalentity = null;

        for(AnimalEntity animalentity1 : list) {
            if (this.slime.canBreedWith(animalentity1) && this.slime.squaredDistanceTo(animalentity1) < d0) {
                animalentity = animalentity1;
                d0 = this.slime.squaredDistanceTo(animalentity1);
            }
        }

        return animalentity;
    }

    /**
     * Spawns a baby animal of the same type.
     */
    protected void spawnBaby() {
        PassiveEntity childEntity = this.slime.createChild((ServerWorld)this.world, this.field_75391_e);
        if (childEntity != null) {
            ServerPlayerEntity serverplayerentity = this.slime.getLovingPlayer();
            if (serverplayerentity == null && this.field_75391_e.getLovingPlayer() != null) {
                serverplayerentity = this.field_75391_e.getLovingPlayer();
            }

            if (serverplayerentity != null) {
                serverplayerentity.incrementStat(Stats.ANIMALS_BRED);
                Criteria.BRED_ANIMALS.trigger(serverplayerentity, this.slime, this.field_75391_e, childEntity);
            }

            this.slime.setBreedingAge(6000);
            this.field_75391_e.setBreedingAge(6000);
            this.slime.resetLoveTicks();
            this.field_75391_e.resetLoveTicks();
            childEntity.setBreedingAge(-24000);
            childEntity.refreshPositionAndAngles(this.slime.getX(), this.slime.getY(), this.slime.getZ(), 0.0F, 0.0F);
            this.world.spawnEntity(childEntity);
            this.world.sendEntityStatus(this.slime, (byte)18);
            if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                this.world.spawnEntity(new ExperienceOrbEntity(this.world, this.slime.getX(), this.slime.getY(), this.slime.getZ(), this.slime.getRandom().nextInt(7) + 1));
            }

        }
    }
}
