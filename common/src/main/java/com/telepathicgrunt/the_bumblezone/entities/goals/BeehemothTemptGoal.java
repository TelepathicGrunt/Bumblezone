package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class BeehemothTemptGoal extends Goal {
    private static final TargetingConditions TEMP_TARGETING = TargetingConditions.forNonCombat().range(10.0).ignoreLineOfSight();
    private final TargetingConditions targetingConditions;
    protected final BeehemothEntity mob;
    private final double speedModifier;
    @Nullable
    protected Player player;
    private final TagKey<Item> temptItemTag;

    public BeehemothTemptGoal(BeehemothEntity pathfinderMob, double speedModifier, TagKey<Item> temptItemTag) {
        this.mob = pathfinderMob;
        this.speedModifier = speedModifier;
        this.temptItemTag = temptItemTag;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.targetingConditions = TEMP_TARGETING.copy().selector(entity -> this.mob.getOwner() == entity).selector(this::shouldFollow);
    }

    @Override
    public boolean canUse() {
        if(mob.isTame()) {
            LivingEntity owner = this.mob.getOwner();
            if (owner instanceof Player ownerPlayer && ownerPlayer.isAlive() && ownerPlayer.level() == mob.level() && shouldFollow(ownerPlayer)) {
                this.player = ownerPlayer;
                this.mob.setInSittingPose(false);
                return true;
            }
            return false;
        }
        else {
            this.player = this.mob.level().getNearestPlayer(this.targetingConditions, this.mob);
            return this.player != null;
        }
    }

    private boolean shouldFollow(LivingEntity livingEntity) {
        return livingEntity.getMainHandItem().is(this.temptItemTag) || livingEntity.getOffhandItem().is(this.temptItemTag);
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob.distanceToSqr(this.player) > (mob.isTame() ? 200 : 36.0)) {
            return false;
        }

        return this.canUse();
    }

    @Override
    public void stop() {
        this.player = null;
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        this.mob.getLookControl().setLookAt(this.player, (float)(this.mob.getMaxHeadYRot() + 20), (float)this.mob.getMaxHeadXRot());
        if (this.mob.distanceToSqr(this.player) < 6.25) {
            this.mob.getNavigation().stop();
        }
        else {
            this.mob.getNavigation().moveTo(this.player, this.speedModifier * (mob.isTame() ? 2 : 1));
        }
    }
}