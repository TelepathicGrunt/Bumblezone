package com.telepathicgrunt.bumblezone.entities.controllers;

import com.telepathicgrunt.bumblezone.entities.mobs.HoneySlimeEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

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
        this.targetYaw = 180.0F * slimeIn.getYRot() / (float) Math.PI;
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
        this.speedModifier = speedIn;
        this.operation = MoveControl.Operation.MOVE_TO;
    }

    public void tick() {
        this.mob.setYRot(this.rotlerp(this.mob.getYRot(), this.targetYaw, 90.0F));
        this.mob.yHeadRot = this.mob.getYRot();
        this.mob.yBodyRot = this.mob.getYRot();
        if (this.operation != MoveControl.Operation.MOVE_TO) {
            this.mob.setZza(0.0F);
        } else {
            this.operation = MoveControl.Operation.WAIT;
            if (this.mob.isOnGround()) {
                this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue()));
                if (this.jumpDelay-- <= 0) {
                    this.jumpDelay = this.slime.getJumpDelay();
                    if (this.isAggressive) {
                        this.jumpDelay /= 3;
                    }

                    this.slime.getJumpControl().jump();
                    if (this.slime.makesSoundOnJump()) {
                        this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), ((this.slime.getRandom().nextFloat() - this.slime.getRandom().nextFloat()) * 0.2F + 1.0F) * 0.8F);
                    }
                } else {
                    this.slime.xxa = 0.0F;
                    this.slime.zza = 0.0F;
                    this.mob.setSpeed(0.0F);
                }
            } else {
                this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttribute(Attributes.MOVEMENT_SPEED).getValue()));
            }
        }
    }
}