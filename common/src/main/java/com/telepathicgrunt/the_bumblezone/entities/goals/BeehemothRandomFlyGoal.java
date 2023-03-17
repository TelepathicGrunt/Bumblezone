package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class BeehemothRandomFlyGoal extends Goal {
    private final BeehemothEntity beehemothEntity;
    private BlockPos target = null;

    public BeehemothRandomFlyGoal(BeehemothEntity beehemothEntity) {
        this.beehemothEntity = beehemothEntity;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public void start() {
        target = getBlockInViewBeehemoth();
        if (target != null) {
            beehemothEntity.getMoveControl().setWantedPosition(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, beehemothEntity.getFlyingSpeed());
        }
    }

    public boolean canUse() {
        MoveControl movementcontroller = beehemothEntity.getMoveControl();
        if (beehemothEntity.isStopWandering()) {
            return false;
        }
        if (!movementcontroller.hasWanted() || target == null) {
            target = getBlockInViewBeehemoth();
            if (target != null) {
                beehemothEntity.getMoveControl().setWantedPosition(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, beehemothEntity.getFlyingSpeed());
            }
            return true;
        }
        return false;
    }

    public boolean canContinueToUse() {
        return target != null && !beehemothEntity.isStopWandering() && beehemothEntity.distanceToSqr(Vec3.atCenterOf(target)) > 2.4D && beehemothEntity.getMoveControl().hasWanted() && !beehemothEntity.horizontalCollision;
    }

    public void stop() {
        target = null;
    }

    public void tick() {
        if (target == null) {
            target = getBlockInViewBeehemoth();
        }
        if (target != null) {
            double distance = beehemothEntity.distanceToSqr(Vec3.atCenterOf(target));
            if (distance < 2.5D) {
                target = null;
                return;
            }

            beehemothEntity.getMoveControl().setWantedPosition(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, beehemothEntity.getFlyingSpeed());
        }
    }

    public BlockPos getBlockInViewBeehemoth() {
        float radius = 3 + beehemothEntity.getRandom().nextInt(6);
        float neg = beehemothEntity.getRandom().nextBoolean() ? 1 : -1;
        float renderYawOffset = beehemothEntity.yBodyRot;
        float angle = (0.01745329251F * renderYawOffset) + 3.15F + (beehemothEntity.getRandom().nextFloat() * neg);
        double extraX = radius * Mth.sin((float) (Math.PI + angle));
        double extraZ = radius * Mth.cos(angle);
        BlockPos radialPos = BlockPos.containing(beehemothEntity.getX() + extraX, beehemothEntity.getY() + 2, beehemothEntity.getZ() + extraZ);
        BlockPos ground = getGroundPosition(beehemothEntity.level, radialPos);
        BlockPos newPos = ground.above(1 + beehemothEntity.getRandom().nextInt(6));
        if (!beehemothEntity.isTargetBlocked(Vec3.atCenterOf(newPos)) && beehemothEntity.distanceToSqr(Vec3.atCenterOf(newPos)) > 6) {
            return newPos;
        }
        return null;
    }

    private BlockPos getGroundPosition(Level level, BlockPos radialPos) {
        while (radialPos.getY() > 1 && level.isEmptyBlock(radialPos)) {
            radialPos = radialPos.below();
        }
        if (radialPos.getY() <= 1) {
            return new BlockPos(radialPos.getX(), level.getSeaLevel(), radialPos.getZ());
        }
        return radialPos;
    }
}