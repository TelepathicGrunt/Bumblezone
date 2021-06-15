package com.telepathicgrunt.bumblezone.entities.goals;

import com.telepathicgrunt.bumblezone.entities.controllers.HoneySlimeMoveHelperController;
import com.telepathicgrunt.bumblezone.entities.mobs.HoneySlimeEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

import java.util.EnumSet;

public class TemptGoal extends Goal {
    private static final TargetPredicate ENTITY_PREDICATE = (TargetPredicate.createAttackable()).setBaseMaxDistance(10.0D).ignoreVisibility();
    protected final HoneySlimeEntity slime;
    private double targetX;
    private double targetY;
    private double targetZ;
    private double pitch;
    private double yaw;
    protected PlayerEntity closestPlayer;
    private int delayTemptCounter;
    private final Ingredient temptItem;

    public TemptGoal(HoneySlimeEntity creatureIn, double speedIn, Ingredient temptItemsIn) {
        this.slime = creatureIn;
        this.temptItem = temptItemsIn;
        this.setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE, Goal.Control.LOOK));
        if (!(creatureIn.getNavigation() instanceof MobNavigation) && !(creatureIn.getNavigation() instanceof BirdNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for TemptGoal");
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean canStart() {
        if (this.delayTemptCounter > 0) {
            --this.delayTemptCounter;
            return false;
        } else {
            this.closestPlayer = this.slime.world.getClosestPlayer(ENTITY_PREDICATE, this.slime);
            if (this.closestPlayer == null) {
                return false;
            } else {
                return this.isTempting(this.closestPlayer.getMainHandStack()) || this.isTempting(this.closestPlayer.getOffHandStack());
            }
        }
    }

    protected boolean isTempting(ItemStack stack) {
        return this.temptItem.test(stack);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinue() {
        if (this.isScaredByPlayerMovement()) {
            if (this.slime.squaredDistanceTo(this.closestPlayer) < 36.0D) {
                if (this.closestPlayer.squaredDistanceTo(this.targetX, this.targetY, this.targetZ) > 0.010000000000000002D) {
                    return false;
                }

                if (Math.abs((double)this.closestPlayer.getPitch() - this.pitch) > 5.0D || Math.abs((double)this.closestPlayer.getYaw() - this.yaw) > 5.0D) {
                    return false;
                }
            } else {
                this.targetX = this.closestPlayer.getX();
                this.targetY = this.closestPlayer.getY();
                this.targetZ = this.closestPlayer.getZ();
            }

            this.pitch = this.closestPlayer.getPitch();
            this.yaw = this.closestPlayer.getYaw();
        }

        return this.canStart();
    }

    protected boolean isScaredByPlayerMovement() {
        return false;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        this.targetX = this.closestPlayer.getX();
        this.targetY = this.closestPlayer.getY();
        this.targetZ = this.closestPlayer.getZ();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        this.closestPlayer = null;
        this.slime.getNavigation().stop();
        this.delayTemptCounter = 100;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        this.slime.getLookControl().lookAt(this.closestPlayer, (float)(this.slime.getBodyYawSpeed() + 20), (float)this.slime.getLookPitchSpeed());
        if (this.slime.squaredDistanceTo(this.closestPlayer) < 6.25D) {
            this.slime.getNavigation().stop();
        } else {
            this.slime.lookAtEntity(this.closestPlayer, 10.0F, 10.0F);
            ((HoneySlimeMoveHelperController) this.slime.getMoveControl()).setDirection(this.slime.getYaw(), true);
            ((HoneySlimeMoveHelperController) this.slime.getMoveControl()).setSpeed(1.0D);
        }

    }
}
