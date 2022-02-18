package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
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
        double speedModifier = beehemothEntity.isQueen() ? 3D : 1.0D;
        speedModifier += (beehemothEntity.getFriendship() / 400D);
        double x = beehemothEntity.getX();
        double y = beehemothEntity.getY();
        double z = beehemothEntity.getZ();

        Vec3 lookVec = player.getLookAngle();
        if (player.zza < 0) {
            lookVec = lookVec.yRot((float) Math.PI);
        }

        if (player.zza != 0 || beehemothEntity.movingStraightUp || beehemothEntity.movingStraightDown) {
            currentSpeed = Math.min(
                    Bumblezone.BZ_CONFIG.BZGeneralConfig.beehemothSpeed * speedModifier * beehemothEntity.getFinalFlyingSpeed(),
                    currentSpeed + 0.3D);
        }
        else {
            currentSpeed = Math.max(0, currentSpeed - 0.25D);
        }

        x += lookVec.x * 10;
        y += lookVec.y * 5 + 0.25D;
        z += lookVec.z * 10;

        if(beehemothEntity.movingStraightUp || beehemothEntity.movingStraightDown) {
            if(beehemothEntity.movingStraightUp) {
                y += 5;
            }
            if(beehemothEntity.movingStraightDown) {
                y -= 5;
            }

            Vec3 velocity = beehemothEntity.getDeltaMovement();
            beehemothEntity.setDeltaMovement(velocity.x(), Math.min(velocity.y, 0.1D * currentSpeed), velocity.z());

            if(player.zza == 0) {
                x = beehemothEntity.getX() + lookVec.x * 0.00001D;
                z = beehemothEntity.getZ() + lookVec.z * 0.00001D;
            }
        }

        beehemothEntity.xxa = player.xxa * 0.35f;
        beehemothEntity.maxUpStep = 1;
        beehemothEntity.getMoveControl().setWantedPosition(x, y, z, currentSpeed);
    }
}
