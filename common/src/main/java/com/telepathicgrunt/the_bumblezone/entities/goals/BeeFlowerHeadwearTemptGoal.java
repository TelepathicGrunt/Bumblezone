package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.items.FlowerHeadwearHelmet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class BeeFlowerHeadwearTemptGoal extends Goal {
    private static final TargetingConditions TEMP_TARGETING = TargetingConditions.forNonCombat().range(20.0).ignoreLineOfSight();
    private final TargetingConditions targetingConditions;
    protected final PathfinderMob mob;
    private final double speedModifier;
    @Nullable
    protected Player player;

    public BeeFlowerHeadwearTemptGoal(PathfinderMob pathfinderMob, double speedModifier) {
        this.mob = pathfinderMob;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.targetingConditions = TEMP_TARGETING.copy().selector(this::shouldFollow);
    }

    @Override
    public boolean canUse() {
        this.player = this.mob.level().getNearestPlayer(this.targetingConditions, this.mob);
        return this.player != null;
    }

    private boolean shouldFollow(LivingEntity livingEntity) {
        return !FlowerHeadwearHelmet.getFlowerHeadware(livingEntity).isEmpty();
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void stop() {
        this.player = null;
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        this.mob.getLookControl().setLookAt(this.player, this.mob.getMaxHeadYRot() + 20, this.mob.getMaxHeadXRot());
        if (this.mob.distanceToSqr(this.player) < 6.25) {
            this.mob.getNavigation().stop();
        }
        else {
            this.mob.getNavigation().moveTo(this.player, this.speedModifier);
        }
    }
}