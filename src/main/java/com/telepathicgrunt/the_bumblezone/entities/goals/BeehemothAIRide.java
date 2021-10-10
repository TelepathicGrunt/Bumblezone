package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;

public class BeehemothAIRide extends Goal {
    private final BeehemothEntity beehemothEntity;
    private LivingEntity player;
    private final double speed;

    public BeehemothAIRide(BeehemothEntity beehemothEntity, double speed) {
        this.beehemothEntity = beehemothEntity;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (beehemothEntity.getControllingPassenger() instanceof PlayerEntity && beehemothEntity.isSaddled()) {
            player = (PlayerEntity) beehemothEntity.getControllingPassenger();
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
        float speedModifier = beehemothEntity.isQueen() ? 3f : 1.0f;
        speedModifier += (beehemothEntity.getFriendship() / 500f);
        beehemothEntity.getNavigation().stop();
        beehemothEntity.setTarget(null);
        double x = beehemothEntity.getX();
        double y = beehemothEntity.getY();
        double z = beehemothEntity.getZ();
        if (player.zza != 0) {
            Vector3d lookVec = player.getLookAngle();
            if (player.zza < 0) {
                lookVec = lookVec.yRot((float) Math.PI);
            }
            x += lookVec.x * 10;
            z += lookVec.z * 10;
            if (beehemothEntity != null) {
                y += lookVec.y * 10;
            }
        }
        beehemothEntity.xxa = player.xxa * 0.35F;
        beehemothEntity.maxUpStep = 1;
        beehemothEntity.getMoveControl().setWantedPosition(x, y, z, speed * speedModifier);
    }
}
