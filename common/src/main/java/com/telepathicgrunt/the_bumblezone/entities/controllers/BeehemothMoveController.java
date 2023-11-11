package com.telepathicgrunt.the_bumblezone.entities.controllers;

import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class BeehemothMoveController extends MoveControl {
    private final BeehemothEntity beehemothEntity;

    public BeehemothMoveController(BeehemothEntity beehemothEntity) {
        super(beehemothEntity);
        this.beehemothEntity = beehemothEntity;
    }

    @Override
    public void tick() {

        if (this.operation == Operation.STRAFE) {
            Vec3 vec3 = new Vec3(this.wantedX - beehemothEntity.getX(), this.wantedY - beehemothEntity.getY(), this.wantedZ - beehemothEntity.getZ());
            double d0 = vec3.length();
            beehemothEntity.setDeltaMovement(beehemothEntity.getDeltaMovement().add(0, vec3.scale(this.speedModifier * 0.05D / d0).y(), 0));
            float f = (float) this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
            float f1 = (float) this.speedModifier * f;
            this.strafeForwards = 1.0F;
            this.strafeRight = 0.0F;

            this.mob.setSpeed(f1);
            this.mob.setZza(this.strafeForwards);
            this.mob.setXxa(this.strafeRight);
            this.operation = MoveControl.Operation.WAIT;
        }

        if (this.operation == MoveControl.Operation.MOVE_TO) {
            Vec3 vec3 = new Vec3(
                    this.wantedX - beehemothEntity.getX(),
                    this.wantedY - beehemothEntity.getY(),
                    this.wantedZ - beehemothEntity.getZ());

            double length = vec3.length() / 1.25D;

            if (length < beehemothEntity.getBoundingBox().getSize()) {
                this.operation = MoveControl.Operation.WAIT;
                beehemothEntity.setDeltaMovement(beehemothEntity.getDeltaMovement().scale(0.5D));
            }
            else {
                double localSpeed = this.speedModifier;
                if (beehemothEntity.isVehicle()) {
                    localSpeed *= 1.5D;
                }
                Vec3 newVelocity = beehemothEntity.getDeltaMovement().add(vec3.scale(localSpeed / length));

                double newYSpeed;
                if (beehemothEntity.onGround()) {
                    newYSpeed = (newVelocity.y() + 0.009D);
                }
                else {
                    newYSpeed = newVelocity.y();
                }
                beehemothEntity.setDeltaMovement(newVelocity.x(), newYSpeed, newVelocity.z());

                float lookAngle = (float)(Mth.atan2(vec3.x(), vec3.z()) * -(double)(180F / (float)Math.PI));
                beehemothEntity.setYRot(this.rotlerp(beehemothEntity.getYRot(), lookAngle, 90.0F));
            }
        }
    }
}