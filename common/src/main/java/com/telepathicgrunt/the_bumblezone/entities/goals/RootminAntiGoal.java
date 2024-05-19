package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

import java.util.EnumSet;
import java.util.UUID;

public class RootminAntiGoal extends Goal {
    protected final RootminEntity mob;

    public RootminAntiGoal(RootminEntity pathfinderMob) {
        this.mob = pathfinderMob;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.mob.getEssenceController() != null;
    }

    @Override
    public boolean canContinueToUse() {
        UUID essenceUuid = this.mob.getEssenceController();
        ResourceKey<Level> essenceDimension = this.mob.getEssenceControllerDimension();
        BlockPos essenceBlockPos = this.mob.getEssenceControllerBlockPos();

        return essenceUuid != null && essenceDimension != null && essenceBlockPos != null;
    }

    @Override
    public void start() {
        this.mob.setRootminPose(RootminState.NONE);
    }

    @Override
    public void stop() {
        this.mob.remove(Entity.RemovalReason.DISCARDED);
    }

    @Override
    public void tick() {
        UUID essenceUuid = this.mob.getEssenceController();
        ResourceKey<Level> essenceDimension = this.mob.getEssenceControllerDimension();
        BlockPos essenceBlockPos = this.mob.getEssenceControllerBlockPos();

        BlockPos rootminBlockPos = this.mob.blockPosition();
        EssenceBlockEntity essenceBlockEntity = EssenceBlockEntity.getEssenceBlockAtLocation(this.mob.level(), essenceDimension, essenceBlockPos, essenceUuid);

        if (essenceBlockEntity != null) {
            BlockPos arenaSize = essenceBlockEntity.getArenaSize();

            if (Math.abs(rootminBlockPos.getX() - essenceBlockPos.getX()) > (arenaSize.getX() / 2) ||
                Math.abs(rootminBlockPos.getY() - essenceBlockPos.getY()) > (arenaSize.getY() / 2) ||
                Math.abs(rootminBlockPos.getZ() - essenceBlockPos.getZ()) > (arenaSize.getZ() / 2))
            {
                //Failed check. Kill mob.
                this.mob.remove(Entity.RemovalReason.DISCARDED);
            }
        }
        else {
            //Failed check. Kill mob.
            this.mob.remove(Entity.RemovalReason.DISCARDED);
        }
    }
}