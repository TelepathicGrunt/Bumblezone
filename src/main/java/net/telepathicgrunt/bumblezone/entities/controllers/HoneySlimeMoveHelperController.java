package net.telepathicgrunt.bumblezone.entities.controllers;

import com.bagel.buzzierbees.common.entities.HoneySlimeEntity;

import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;

public class HoneySlimeMoveHelperController extends MovementController {
    private float yRot;
    private int jumpDelay;
    private final HoneySlimeEntity slime;
    private boolean isAggressive;

    public HoneySlimeMoveHelperController(HoneySlimeEntity slimeIn) {
        super(slimeIn);
        this.slime = slimeIn;
        this.yRot = 180.0F * slimeIn.rotationYaw / (float) Math.PI;
    }

    public void setDirection(float yRotIn, boolean aggressive) {
        this.yRot = yRotIn;
        this.isAggressive = aggressive;
    }

    public void setDirection(float yRotIn) {
        this.yRot = yRotIn;
        this.isAggressive = true;
    }

    public void setSpeed(double speedIn) {
        this.speed = speedIn;
        this.action = MovementController.Action.MOVE_TO;
    }

    public void tick() {
        this.mob.rotationYaw = this.limitAngle(this.mob.rotationYaw, this.yRot, 90.0F);
        this.mob.rotationYawHead = this.mob.rotationYaw;
        this.mob.renderYawOffset = this.mob.rotationYaw;
        if (this.action != MovementController.Action.MOVE_TO) {
            this.mob.setMoveForward(0.0F);
        } else {
            this.action = MovementController.Action.WAIT;
            if (this.mob.func_233570_aj_()) {
                this.mob.setAIMoveSpeed((float) (this.speed * this.mob.getAttribute(Attributes.field_233820_c_).getValue()));
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
                this.mob.setAIMoveSpeed((float) (this.speed * this.mob.getAttribute(Attributes.field_233820_c_).getValue()));
            }

        }
    }
}