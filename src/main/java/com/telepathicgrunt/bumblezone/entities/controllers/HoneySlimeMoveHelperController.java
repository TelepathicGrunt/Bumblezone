package com.telepathicgrunt.bumblezone.entities.controllers;

import com.telepathicgrunt.bumblezone.entities.mobs.HoneySlimeEntity;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.attribute.EntityAttributes;

public class HoneySlimeMoveHelperController extends MoveControl {
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
        this.targetYaw = 180.0F * slimeIn.getYaw() / (float) Math.PI;
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
        this.state = MoveControl.State.MOVE_TO;
    }

    public void tick() {
        this.entity.setYaw(this.wrapDegrees(this.entity.getYaw(), this.targetYaw, 90.0F));
        this.entity.headYaw = this.entity.getYaw();
        this.entity.bodyYaw = this.entity.getYaw();
        if (this.state != MoveControl.State.MOVE_TO) {
            this.entity.setForwardSpeed(0.0F);
        } else {
            this.state = MoveControl.State.WAIT;
            if (this.entity.isOnGround()) {
                this.entity.setMovementSpeed((float) (this.speed * this.entity.getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE).getValue()));
                if (this.jumpDelay-- <= 0) {
                    this.jumpDelay = this.slime.getJumpDelay();
                    if (this.isAggressive) {
                        this.jumpDelay /= 3;
                    }

                    this.slime.getJumpControl().setActive();
                    if (this.slime.makesSoundOnJump()) {
                        this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), ((this.slime.getRandom().nextFloat() - this.slime.getRandom().nextFloat()) * 0.2F + 1.0F) * 0.8F);
                    }
                } else {
                    this.slime.sidewaysSpeed = 0.0F;
                    this.slime.forwardSpeed = 0.0F;
                    this.entity.setMovementSpeed(0.0F);
                }
            } else {
                this.entity.setMovementSpeed((float) (this.speed * this.entity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).getValue()));
            }
        }
    }
}