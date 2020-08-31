package com.telepathicgrunt.bumblezone.entities.controllers;

import com.telepathicgrunt.bumblezone.entities.mobs.HoneySlimeEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;

public class HoneySlimeMoveHelperController extends MovementController {
    /**
     * Special thanks to Bagel for the Honey Slime code and texture!
     */

    private float targetYaw;
    private int jumpDelay;
    private final HoneySlimeEntity slime;
    private boolean isAggressive;

    public HoneySlimeMoveHelperController(HoneySlimeEntity slimeIn) {
        super(slimeIn);
        this.slime = slimeIn;
        this.targetYaw = 180.0F * slimeIn.rotationYaw / (float) Math.PI;
    }

    public void setDirection(float yRotIn, boolean aggressive) {
        this.targetYaw = yRotIn;
        this.isAggressive = aggressive;
    }

    public void setDirection(float yRotIn) {
        this.targetYaw = yRotIn;
        this.isAggressive = true;
    }

    public void setSpeed(double speedIn) {
        this.speed = speedIn;
        this.action = MovementController.Action.MOVE_TO;
    }

    public void tick() {
        this.mob.rotationYaw = this.limitAngle(this.mob.rotationYaw, this.targetYaw, 90.0F);
        this.mob.rotationYawHead = this.mob.rotationYaw;
        this.mob.renderYawOffset = this.mob.rotationYaw;
        if (this.action != MovementController.Action.MOVE_TO) {
            this.mob.setMoveForward(0.0F);
        } else {
            this.action = MovementController.Action.WAIT;
            if (this.mob.isOnGround()) {
                this.mob.setAIMoveSpeed((float) (this.speed * this.mob.getAttribute(Attributes.GENERIC_KNOCKBACK_RESISTANCE).getValue()));
                if (this.jumpDelay-- <= 0) {
                    this.jumpDelay = this.slime.getJumpDelay();
                    if (this.isAggressive) {
                        this.jumpDelay /= 3;
                    }

                    this.slime.getJumpController().setJumping();
                    if (this.slime.makesSoundOnJump()) {
                        this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), ((this.slime.getRNG().nextFloat() - this.slime.getRNG().nextFloat()) * 0.2F + 1.0F) * 0.8F);
                    }
                } else {
                    this.slime.moveStrafing = 0.0F;
                    this.slime.moveForward = 0.0F;
                    this.mob.setAIMoveSpeed(0.0F);
                }
            } else {
                this.mob.setAIMoveSpeed((float) (this.speed * this.mob.getAttribute(Attributes.GENERIC_MOVEMENT_SPEED).getValue()));
            }
        }
    }
}