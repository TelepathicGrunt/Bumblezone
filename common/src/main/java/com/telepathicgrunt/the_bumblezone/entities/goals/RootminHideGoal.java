package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class RootminHideGoal extends Goal {
    protected final RootminEntity mob;
    @Nullable
    protected Path path;
    protected final PathNavigation pathNav;
    @Nullable
    protected Vec3 destination;

    public RootminHideGoal(RootminEntity pathfinderMob) {
        this.mob = pathfinderMob;
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.pathNav = pathfinderMob.getNavigation();
    }

    @Override
    public boolean canUse() {
        if (this.mob.getTarget() != null && this.mob.getTarget().isAlive()) {
            if (this.mob.getTarget() instanceof Player player) {
                if (player.getUUID().equals(this.mob.superHatedPlayer)) {
                    return false;
                }
            }
            else {
                return false;
            }
        }

        if (this.mob.exposedTimer != 0 || this.mob.rootminToLookAt != null) {
            return false;
        }

        if (this.mob.tickCount - this.mob.getLastHurtByMobTimestamp() < 1200 &&
            this.mob.attackerMemory != null &&
            !this.mob.attackerMemory.isDeadOrDying() &&
            this.mob.attackerMemory.blockPosition().distManhattan(this.mob.blockPosition()) < 25)
        {
            if (RootminEntity.isFacingMob(this.mob, this.mob.attackerMemory)) {
                return false;
            }
        }

        Level level = this.mob.level();
        RandomSource randomSource = this.mob.getRandom();
        BlockPos chosenPos = this.mob.blockPosition().offset(
                randomSource.nextInt(11) - 5,
                randomSource.nextInt(5) - 2,
                randomSource.nextInt(11) - 5);

        if (!level.isEmptyBlock(chosenPos)) {
            return false;
        }

        BlockState belowState = level.getBlockState(chosenPos.below());
        if (!belowState.isCollisionShapeFullBlock(level, chosenPos.below()) || belowState.isAir()) {
            return false;
        }

        List<Entity> existingRootmins = level.getEntities(
                this.mob,
                new AABB(
                        chosenPos.getX(),
                        chosenPos.getY(),
                        chosenPos.getZ(),
                        chosenPos.getX() + 1,
                        chosenPos.getY() + 1,
                        chosenPos.getZ() + 1
                ),
                (entity) -> entity.getType() == BzEntities.ROOTMIN.get()
        );

        if (!existingRootmins.isEmpty()) {
            return false;
        }

        this.destination = new Vec3(chosenPos.getX() + 0.5d, chosenPos.getY(), chosenPos.getZ() + 0.5d);
        this.path = this.pathNav.createPath(chosenPos.getX() + 0.5d, chosenPos.getY(), chosenPos.getZ() + 0.5d, 0);
        return this.path != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.pathNav.isDone()) {

            this.mob.takePotShot = false;
            this.mob.hideAsBlock(this.destination);
            return false;
        }

        return !this.mob.isDeadOrDying();
    }

    @Override
    public void start() {
        this.pathNav.moveTo(this.path,
            (this.mob.takePotShot || this.mob.tickCount - this.mob.getLastHurtByMobTimestamp() < 200) ?
                2.0 : 1.0);
    }

    @Override
    public void stop() {
        this.pathNav.stop();
    }

    @Override
    public void tick() {
        boolean isFleeing = (this.mob.takePotShot || this.mob.tickCount - this.mob.getLastHurtByMobTimestamp() < 200);
        this.mob.getNavigation().setSpeedModifier(isFleeing ? 2.0 : 1.0);

        RootminEntity.considerHiddenRootminsInPath(this.path, this.mob);
        RootminEntity.jumpFix(this.path, this.mob);
    }
}