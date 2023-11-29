package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.items.FlowerHeadwearHelmet;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Bee;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class BeeFlowerHeadwearTemptGoal extends Goal {
    private static final int RANGE = 20;
    private static final TargetingConditions TEMP_TARGETING = TargetingConditions
            .forNonCombat()
            .range(RANGE)
            .ignoreLineOfSight()
            .selector(BeeFlowerHeadwearTemptGoal::shouldFollow);

    @Nullable
    protected LivingEntity followEntity;
    protected final Bee mob;
    private final double speedModifier;

    public BeeFlowerHeadwearTemptGoal(Bee pathfinderMob, double speedModifier) {
        this.mob = pathfinderMob;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.mob.getPersistentAngerTarget() != null || this.mob.tickCount % 2 == 0) {
            return false;
        }

        this.followEntity = this.mob.level().getNearestEntity(
            LivingEntity.class,
            TEMP_TARGETING,
            this.mob,
            this.mob.getX(),
            this.mob.getY(),
            this.mob.getZ(),
            this.mob.getBoundingBox().inflate(RANGE)
        );

        return this.followEntity != null;
    }

    private static boolean shouldFollow(LivingEntity livingEntity) {
        if (livingEntity.hasEffect(BzEffects.WRATH_OF_THE_HIVE.get())) {
            return false;
        }

        return !FlowerHeadwearHelmet.getFlowerHeadwear(livingEntity).isEmpty();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob.getPersistentAngerTarget() != null) {
            return false;
        }

        if (this.followEntity == null ||
            this.followEntity.isDeadOrDying() ||
            this.followEntity.hasEffect(BzEffects.WRATH_OF_THE_HIVE.get()))
        {
            return false;
        }

        return this.canUse();
    }

    @Override
    public void stop() {
        this.followEntity = null;
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        this.mob.getLookControl().setLookAt(this.followEntity, this.mob.getMaxHeadYRot() + 20, this.mob.getMaxHeadXRot());
        if (this.mob.distanceToSqr(this.followEntity) < 6) {
            this.mob.getNavigation().stop();
        }
        else {
            this.mob.getNavigation().moveTo(this.followEntity, this.speedModifier);
        }
    }
}