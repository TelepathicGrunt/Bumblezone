package com.telepathicgrunt.the_bumblezone.entities.goals;

import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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
        this.targetingConditions = TEMP_TARGETING.copy().selector((entity, _) -> this.mob.getOwner() == entity).selector(this::shouldFollow);
    }

    @Override
    public boolean canUse() {
        if(mob.isTame()) {
            LivingEntity owner = this.mob.getOwner();
            if (owner instanceof Player ownerPlayer && ownerPlayer.isAlive() && ownerPlayer.level() == this.mob.level() && shouldFollow(ownerPlayer, getServerLevel(this.mob))) {
                this.player = ownerPlayer;
                this.mob.setInSittingPose(false);
                return true;
            }
            return false;
        }
        else {
            this.player = getServerLevel(this.mob)
                    .getNearestPlayer(this.targetingConditions.range(this.mob.getAttributeValue(Attributes.TEMPT_RANGE)), this.mob);
            return this.player != null;
        }
    }

    private boolean shouldFollow(LivingEntity livingEntity, ServerLevel serverLevel) {
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