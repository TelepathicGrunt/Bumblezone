package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.EnumSet;

public class RandomFlyGoal extends Goal {
    private final BeehemothEntity beehemothEntity;
    private BlockPos target = null;

    public RandomFlyGoal(BeehemothEntity beehemothEntity) {
        this.beehemothEntity = beehemothEntity;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        MovementController movementcontroller = beehemothEntity.getMoveControl();
        if (beehemothEntity.isStopWandering()) {
            return false;
        }
        if (!movementcontroller.hasWanted() || target == null) {
            target = getBlockInViewBeehemoth();
            if (target != null) {
                beehemothEntity.getMoveControl().setWantedPosition(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, beehemothEntity.getFinalFlyingSpeed());
            }
            return true;
        }
        return false;
    }

    public boolean canContinueToUse() {
        return target != null && !beehemothEntity.isStopWandering() && beehemothEntity.distanceToSqr(Vector3d.atCenterOf(target)) > 2.4D && beehemothEntity.getMoveControl().hasWanted() && !beehemothEntity.horizontalCollision;
    }

    public void stop() {
        target = null;
    }

    public void tick() {
        if (target == null) {
            target = getBlockInViewBeehemoth();
        }
        if (target != null) {
            beehemothEntity.getMoveControl().setWantedPosition(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, beehemothEntity.getFinalFlyingSpeed());
            if (beehemothEntity.distanceToSqr(Vector3d.atCenterOf(target)) < 2.5F) {
                target = null;
            }
        }
    }


    public BlockPos getBlockInViewBeehemoth() {
        float radius = 1 + beehemothEntity.getRandom().nextInt(5);
        float neg = beehemothEntity.getRandom().nextBoolean() ? 1 : -1;
        float renderYawOffset = beehemothEntity.yBodyRot;
        float angle = (0.01745329251F * renderYawOffset) + 3.15F + (beehemothEntity.getRandom().nextFloat() * neg);
        double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
        double extraZ = radius * MathHelper.cos(angle);
        BlockPos radialPos = new BlockPos(beehemothEntity.getX() + extraX, beehemothEntity.getY() + 2, beehemothEntity.getZ() + extraZ);
        BlockPos ground = getGroundPosition(beehemothEntity.level, radialPos);
        BlockPos newPos = ground.above(1 + beehemothEntity.getRandom().nextInt(6));
        if (!beehemothEntity.isTargetBlocked(Vector3d.atCenterOf(newPos)) && beehemothEntity.distanceToSqr(Vector3d.atCenterOf(newPos)) > 6) {
            return newPos;
        }
        return null;
    }

    private BlockPos getGroundPosition(World level, BlockPos radialPos) {
        while (radialPos.getY() > 1 && level.isEmptyBlock(radialPos)) {
            radialPos = radialPos.below();
        }
        if (radialPos.getY() <= 1) {
            return new BlockPos(radialPos.getX(), level.getSeaLevel(), radialPos.getZ());
        }
        return radialPos;
    }
}