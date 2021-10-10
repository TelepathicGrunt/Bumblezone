package com.telepathicgrunt.bumblezone.entities.goals;

import com.telepathicgrunt.bumblezone.entities.controllers.HoneySlimeMoveHelperController;
import com.telepathicgrunt.bumblezone.entities.mobs.HoneySlimeEntity;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class TemptGoal extends Goal {
    private static final TargetingConditions ENTITY_PREDICATE = (TargetingConditions.forCombat()).range(10.0D).ignoreLineOfSight();
    protected final HoneySlimeEntity slime;
    private double targetX;
    private double targetY;
    private double targetZ;
    private double pitch;
    private double yaw;
    protected Player closestPlayer;
    private int delayTemptCounter;
    private final Ingredient temptItem;

    public TemptGoal(HoneySlimeEntity creatureIn, double speedIn, Ingredient temptItemsIn) {
        this.slime = creatureIn;
        this.temptItem = temptItemsIn;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE, Goal.Flag.LOOK));
        if (!(creatureIn.getNavigation() instanceof GroundPathNavigation) && !(creatureIn.getNavigation() instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for TemptGoal");
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean canUse() {
        if (this.delayTemptCounter > 0) {
            --this.delayTemptCounter;
            return false;
        } else {
            this.closestPlayer = this.slime.level.getNearestPlayer(ENTITY_PREDICATE, this.slime);
            if (this.closestPlayer == null) {
                return false;
            } else {
                return this.isTempting(this.closestPlayer.getMainHandItem()) || this.isTempting(this.closestPlayer.getOffhandItem());
            }
        }
    }

    protected boolean isTempting(ItemStack stack) {
        return this.temptItem.test(stack);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean canContinueToUse() {
        if (this.isScaredByPlayerMovement()) {
            if (this.slime.distanceToSqr(this.closestPlayer) < 36.0D) {
                if (this.closestPlayer.distanceToSqr(this.targetX, this.targetY, this.targetZ) > 0.010000000000000002D) {
                    return false;
                }

                if (Math.abs((double)this.closestPlayer.getXRot() - this.pitch) > 5.0D || Math.abs((double)this.closestPlayer.getYRot() - this.yaw) > 5.0D) {
                    return false;
                }
            } else {
                this.targetX = this.closestPlayer.getX();
                this.targetY = this.closestPlayer.getY();
                this.targetZ = this.closestPlayer.getZ();
            }

            this.pitch = this.closestPlayer.getXRot();
            this.yaw = this.closestPlayer.getYRot();
        }

        return this.canUse();
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
        this.slime.getLookControl().setLookAt(this.closestPlayer, (float)(this.slime.getMaxHeadYRot() + 20), (float)this.slime.getMaxHeadXRot());
        if (this.slime.distanceToSqr(this.closestPlayer) < 6.25D) {
            this.slime.getNavigation().stop();
        } else {
            this.slime.lookAt(this.closestPlayer, 10.0F, 10.0F);
            ((HoneySlimeMoveHelperController) this.slime.getMoveControl()).setDirection(this.slime.getYRot(), true);
            ((HoneySlimeMoveHelperController) this.slime.getMoveControl()).setSpeed(1.0D);
        }

    }
}
