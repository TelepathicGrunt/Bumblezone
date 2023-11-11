package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class RootminHiddenGoal extends Goal {
    protected final RootminEntity mob;
    protected int unhidingTimer = 0;

    public RootminHiddenGoal(RootminEntity pathfinderMob) {
        this.mob = pathfinderMob;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.mob.isHidden;
    }

    @Override
    public boolean canContinueToUse() {
        return (this.unhidingTimer > 0 || this.mob.isHidden) && this.mob.hurtTime == 0 && !this.mob.isDeadOrDying();
    }

    @Override
    public void start() {
        this.mob.stayHidingTimer = 200;
        this.unhidingTimer = 0;
    }

    @Override
    public void stop() {
    }

    @Override
    public void tick() {
        if (!this.mob.isPassenger()) {
            float lookAngle = this.mob.getYRot();
            Direction direction = Direction.fromYRot(lookAngle);
            Vec3 lookVec = Vec3.atLowerCornerOf(direction.getNormal()).add(this.mob.position());
            this.mob.getLookControl().setLookAt(lookVec.x(), lookVec.y(), lookVec.z(), 60, 0);
            this.mob.setYRot(direction.toYRot());
        }

        if (this.mob.stayHidingTimer == 0) {
            if (this.unhidingTimer > 0) {
                this.unhidingTimer--;
            }
            else if (this.unhidingTimer == 0) {
                LivingEntity target = this.mob.getTarget();
                if (target != null) {
                    int distance = target.blockPosition().distManhattan(this.mob.blockPosition());
                    if (distance >= 8 && distance <= 26) {
                        if (!(target instanceof Player) || !RootminEntity.isFacingMob(this.mob, target)) {
                            this.unhidingTimer = 20;
                            this.mob.exposeFromBlock();
                            this.mob.exposedTimer = 160;
                            this.mob.takePotShot = true;
                        }
                    }
                }
            }
        }
        else {
            this.mob.stayHidingTimer--;
        }
    }
}