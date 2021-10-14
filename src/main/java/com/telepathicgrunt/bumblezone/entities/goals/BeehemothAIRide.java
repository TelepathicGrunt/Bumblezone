package com.telepathicgrunt.bumblezone.entities.goals;

import com.mojang.math.Vector3d;
import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.bumblezone.mixin.LivingEntityAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class BeehemothAIRide extends Goal {
    private final BeehemothEntity beehemothEntity;
    private LivingEntity player;
    private double currentSpeed;

    public BeehemothAIRide(BeehemothEntity beehemothEntity) {
        this.beehemothEntity = beehemothEntity;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (beehemothEntity.getControllingPassenger() instanceof Player && beehemothEntity.isSaddled()) {
            player = (Player) beehemothEntity.getControllingPassenger();
            return true;
        }
        return false;
    }

    @Override
    public void start() {
        beehemothEntity.getNavigation().stop();
    }

    @Override
    public void tick() {
        double speedModifier = beehemothEntity.isQueen() ? 3d : 1.0d;
        speedModifier += (beehemothEntity.getFriendship() / 400d);
        double x = beehemothEntity.getX();
        double y = beehemothEntity.getY();
        double z = beehemothEntity.getZ();

        Vec3 lookVec = player.getLookAngle();
        if (player.zza < 0) {
            lookVec = lookVec.yRot((float) Math.PI);
        }

        if (player.zza != 0 || ((LivingEntityAccessor)player).isJumping()) {
            currentSpeed = Math.min(
                    Bumblezone.BZ_CONFIG.BZGeneralConfig.beehemothSpeed * speedModifier,
                    currentSpeed + 0.3d);
        }
        else {
            currentSpeed = Math.max(0, currentSpeed - 0.25d);
        }

        x += lookVec.x * 10;
        y += lookVec.y * 5 + 0.25d;
        z += lookVec.z * 10;

        if(((LivingEntityAccessor)player).isJumping()) {
            y += 5;
            Vec3 velocity = beehemothEntity.getDeltaMovement();
            beehemothEntity.setDeltaMovement(velocity.x(), Math.min(velocity.y, 0.1d * currentSpeed), velocity.z());

            if(player.zza == 0) {
                x = beehemothEntity.getX() + lookVec.x * 0.00001d;
                z = beehemothEntity.getZ() + lookVec.z * 0.00001d;
            }
        }

        beehemothEntity.xxa = player.xxa * 0.35f;
        beehemothEntity.maxUpStep = 1;
        beehemothEntity.getMoveControl().setWantedPosition(x, y, z, currentSpeed);
    }
}
