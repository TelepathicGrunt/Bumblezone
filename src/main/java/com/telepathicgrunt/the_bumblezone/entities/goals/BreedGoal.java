package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.entities.controllers.HoneySlimeMoveHelperController;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.stats.Stats;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.EnumSet;
import java.util.List;

public class BreedGoal extends Goal {
    private static final EntityPredicate PARTNER_TARGETING = (new EntityPredicate()).range(8.0D).allowInvulnerable().allowSameTeam().allowUnseeable();
    protected final HoneySlimeEntity slime;
    private final Class<? extends AnimalEntity> mateClass;
    protected final World world;
    protected AnimalEntity partner;
    private int spawnBabyDelay;

    public BreedGoal(HoneySlimeEntity animal, double speedIn) {
        this(animal, speedIn, animal.getClass());
    }

    public BreedGoal(HoneySlimeEntity p_i47306_1_, double p_i47306_2_, Class<? extends AnimalEntity> p_i47306_4_) {
        this.slime = p_i47306_1_;
        this.world = p_i47306_1_.level;
        this.mateClass = p_i47306_4_;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean canUse() {
        if (!this.slime.isInLove()) {
            return false;
        } else {
            this.partner = this.getNearbyMate();
            return this.partner != null && this.slime.getMoveControl() instanceof HoneySlimeMoveHelperController;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean canContinueToUse() {
        return this.partner.isAlive() && this.partner.isInLove() && this.spawnBabyDelay < 60;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void stop() {
        this.partner = null;
        this.spawnBabyDelay = 0;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        this.slime.getLookControl().setLookAt(this.partner, 10.0F, (float)this.slime.getMaxHeadXRot());

        this.slime.lookAt(this.partner, 10.0F, 10.0F);
        ((HoneySlimeMoveHelperController) this.slime.getMoveControl()).setDirection(this.slime.yRot, true);
        ((HoneySlimeMoveHelperController) this.slime.getMoveControl()).setSpeed(1.0D);

        ++this.spawnBabyDelay;
        if (this.spawnBabyDelay >= 60 && this.slime.distanceToSqr(this.partner) < 9.0D) {
            this.spawnBaby();
        }

    }

    /**
     * Loops through nearby animals and finds another animal of the same type that can be mated with. Returns the first
     * valid mate found.
     */
    private AnimalEntity getNearbyMate() {
        List<AnimalEntity> list = this.world.getNearbyEntities(this.mateClass, PARTNER_TARGETING, this.slime, this.slime.getBoundingBox().inflate(8.0D));
        double d0 = Double.MAX_VALUE;
        AnimalEntity animalentity = null;

        for(AnimalEntity animalentity1 : list) {
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
        AgeableEntity childEntity = this.slime.getBreedOffspring((ServerWorld)this.world, this.partner);
        if (childEntity != null) {
            ServerPlayerEntity serverplayerentity = this.slime.getLoveCause();
            if (serverplayerentity == null && this.partner.getLoveCause() != null) {
                serverplayerentity = this.partner.getLoveCause();
            }

            if (serverplayerentity != null) {
                serverplayerentity.awardStat(Stats.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger(serverplayerentity, this.slime, this.partner, childEntity);
            }

            this.slime.setAge(6000);
            this.partner.setAge(6000);
            this.slime.resetLove();
            this.partner.resetLove();
            childEntity.setAge(-24000);
            childEntity.moveTo(this.slime.getX(), this.slime.getY(), this.slime.getZ(), 0.0F, 0.0F);
            this.world.addFreshEntity(childEntity);
            this.world.broadcastEntityEvent(this.slime, (byte)18);
            if (this.world.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                this.world.addFreshEntity(new ExperienceOrbEntity(this.world, this.slime.getX(), this.slime.getY(), this.slime.getZ(), this.slime.getRandom().nextInt(7) + 1));
            }

        }
    }
}
