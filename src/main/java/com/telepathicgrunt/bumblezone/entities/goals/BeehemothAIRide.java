package com.telepathicgrunt.bumblezone.entities.goals;

import com.mojang.math.Vector3d;
import com.telepathicgrunt.bumblezone.entities.mobs.BeehemothEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class BeehemothAIRide extends Goal {
    private final BeehemothEntity beehemothEntity;
    private LivingEntity player;
    private final double speed;
    private double currentSpeed;

    public BeehemothAIRide(BeehemothEntity beehemothEntity, double speed) {
        this.beehemothEntity = beehemothEntity;
        this.speed = speed;
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
        float speedModifier = beehemothEntity.isQueen() ? 3f : 1.0f;
        speedModifier += (beehemothEntity.getFriendship() / 385f);
        double x = beehemothEntity.getX();
        double y = beehemothEntity.getY();
        double z = beehemothEntity.getZ();

        Vec3 lookVec = player.getLookAngle();
        if (player.zza < 0) {
            lookVec = lookVec.yRot((float) Math.PI);
        }
        x += lookVec.x * 10;
        y += lookVec.y * 5;
        z += lookVec.z * 10;

        if (player.zza != 0) {
            currentSpeed = Math.min(speed * speedModifier, currentSpeed + 0.3f);
        }
        else {
            currentSpeed = Math.max(0, currentSpeed - 0.25f);
        }

        beehemothEntity.xxa = player.xxa * 0.35F;
        beehemothEntity.maxUpStep = 1;
        beehemothEntity.getMoveControl().setWantedPosition(x, y, z, currentSpeed);
    }
}
