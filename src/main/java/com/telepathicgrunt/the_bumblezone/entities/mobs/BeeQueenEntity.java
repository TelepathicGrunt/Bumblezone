package com.telepathicgrunt.the_bumblezone.entities.mobs;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.capabilities.BzCapabilities;
import com.telepathicgrunt.the_bumblezone.capabilities.EntityMisc;
import com.telepathicgrunt.the_bumblezone.configs.BzBeeAggressionConfigs;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.TradeEntryReducedObj;
import com.telepathicgrunt.the_bumblezone.mixin.PlayerAdvancementsAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class BeeQueenEntity extends Animal implements NeutralMob {

    public final AnimationState idleAnimationState = new AnimationState();
    private static final EntityDataAccessor<Integer> THROWCOOLDOWN = SynchedEntityData.defineId(BeeQueenEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> BEESPAWNCOOLDOWN = SynchedEntityData.defineId(BeeQueenEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(BeeQueenEntity.class, EntityDataSerializers.INT);
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(60, 120);
    private UUID persistentAngerTarget;
    private int underWaterTicks;

    public BeeQueenEntity(EntityType<? extends BeeQueenEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(THROWCOOLDOWN, 0);
        this.entityData.define(DATA_REMAINING_ANGER_TIME, 1);
        this.entityData.define(BEESPAWNCOOLDOWN, 2);
    }

    public static AttributeSupplier.Builder getAttributeBuilder() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 150.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.1)
                .add(Attributes.ATTACK_DAMAGE, 10.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new AngerableMeleeAttackGoal(this));
        this.goalSelector.addGoal(2, new AlwaysLookAtPlayerGoal(this, Player.class, 60));
        this.goalSelector.addGoal(3, new FloatGoal(this));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("throwcooldown", getThrowCooldown());
        tag.putInt("beespawncooldown", getBeeSpawnCooldown());
        this.addPersistentAngerSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setThrowCooldown(tag.getInt("throwcooldown"));
        setBeeSpawnCooldown(tag.getInt("beespawncooldown"));
        this.readPersistentAngerSaveData(this.level, tag);
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new DirectPathNavigator(this, pLevel);
    }

    @Override
    protected MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    public static boolean checkMobSpawnRules(EntityType<? extends Mob> entityType, LevelAccessor iWorld, MobSpawnType spawnReason, BlockPos blockPos, RandomSource random) {
        return true;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor world, MobSpawnType spawnReason) {
        return true;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldReader) {
        AABB box = getBoundingBox();
        return !worldReader.containsAnyLiquid(box) && worldReader.getBlockStates(box).noneMatch(state -> state.getMaterial().blocksMotion()) && worldReader.isUnobstructed(this);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (isInvulnerableTo(source)) {
            return false;
        }
        else if(isOnPortalCooldown() && source == DamageSource.IN_WALL) {
            spawnAngryParticles(6);
            playHurtSound(source);
            return false;
        }
        else {
            Entity entity = source.getEntity();

            if (BzBeeAggressionConfigs.aggressiveBees.get() && entity instanceof LivingEntity livingEntity) {

                if (!(livingEntity instanceof Player player && player.isCreative()) &&
                    (livingEntity.level.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) ||
                    BzBeeAggressionConfigs.allowWrathOfTheHiveOutsideBumblezone.get()) &&
                    !livingEntity.isSpectator() &&
                    BzBeeAggressionConfigs.aggressiveBees.get())
                {
                    if(livingEntity.hasEffect(BzEffects.PROTECTION_OF_THE_HIVE.get())) {
                        livingEntity.removeEffect(BzEffects.PROTECTION_OF_THE_HIVE.get());
                    }
                    else {
                        //Now all bees nearby in Bumblezone will get VERY angry!!!
                        livingEntity.addEffect(new MobEffectInstance(BzEffects.WRATH_OF_THE_HIVE.get(), BzBeeAggressionConfigs.howLongWrathOfTheHiveLasts.get(), 3, false, BzBeeAggressionConfigs.showWrathOfTheHiveParticles.get(), true));
                        this.startPersistentAngerTimer();
                        this.setPersistentAngerTarget(livingEntity.getUUID());
                        this.setTarget(livingEntity);
                    }
                }
            }

            spawnAngryParticles(6);
            return super.hurt(source, amount);
        }
    }

    protected void customServerAiStep() {
        if (this.isUnderWater()) {
            ++this.underWaterTicks;
        }
        else {
            this.underWaterTicks = 0;
        }

        if (this.underWaterTicks > 100) {
            this.hurt(DamageSource.DROWN, 3.0F);
        }

        if (!this.level.isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level, false);
        }
    }

    public static void applyMiningFatigueInStructures(ServerPlayer serverPlayer) {
        if(serverPlayer.isCreative() || serverPlayer.isSpectator()) {
            return;
        }

        StructureManager structureManager = ((ServerLevel)serverPlayer.level).structureManager();
        if (structureManager.getStructureWithPieceAt(serverPlayer.blockPosition(), BzTags.BEE_QUEEN_MINING_FATIGUE).isValid() &&
            !serverPlayer.level.getEntitiesOfClass(BeeQueenEntity.class, serverPlayer.getBoundingBox().inflate(30.0D, 30.0D, 30.0D), (e) -> true).isEmpty())
        {
            serverPlayer.addEffect(new MobEffectInstance(
                    MobEffects.DIG_SLOWDOWN,
                    100,
                    2,
                    false,
                    false,
                    true));
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isAlive()) {
            this.idleAnimationState.startIfStopped(this.tickCount);
        }
        else {
            this.idleAnimationState.stop();
        }

        if (this.getAge() % 200 == 0) {
            this.heal(1);
        }

        if (!this.level.isClientSide()) {
            if (this.isAngry()) {
                performAngryActions();
            }
            else {
                performGroundTrades();
            }
        }
    }

    private void performAngryActions() {
        int beeCooldown = this.getBeeSpawnCooldown();
        if (beeCooldown <= 0) {
            this.setBeeSpawnCooldown(this.random.nextInt(50) + 75);

            // Grab a nearby air materialposition a bit away
            BlockPos spawnBlockPos = GeneralUtils.getRandomBlockposWithinRange(this.level, this, 5, 0);
            if(this.level.getBlockState(spawnBlockPos).getMaterial() != Material.AIR) {
                return;
            }

            Bee bee = EntityType.BEE.create(this.level);
            if(bee == null) return;
            ((NeutralMob)bee).setRemainingPersistentAngerTime(this.getRemainingPersistentAngerTime());
            ((NeutralMob)bee).setPersistentAngerTarget(this.getPersistentAngerTarget());
            bee.setTarget(this.getTarget());

            bee.absMoveTo(
                    spawnBlockPos.getX() + 0.5D,
                    spawnBlockPos.getY() + 0.5D,
                    spawnBlockPos.getZ() + 0.5D,
                    this.random.nextFloat() * 360.0F,
                    0.0F);

            bee.finalizeSpawn(
                    (ServerLevel) this.level,
                    this.level.getCurrentDifficultyAt(spawnBlockPos),
                    MobSpawnType.TRIGGERED,
                    null,
                    null);

            this.level.addFreshEntity(bee);
            this.spawnAngryParticles(6);
        }
        else {
            this.setBeeSpawnCooldown(beeCooldown - 1);
        }
    }

    private void performGroundTrades() {
        int throwCooldown = getThrowCooldown();
        if (throwCooldown > 0) {
            setThrowCooldown(throwCooldown - 1);
        }

        if (this.getAge() % 20 == 0 && throwCooldown <= 0) {
            Vec3 forwardVect = Vec3.directionFromRotation(0, this.getVisualRotationYInDegrees());
            Vec3 sideVect = Vec3.directionFromRotation(0, this.getVisualRotationYInDegrees() - 90);
            AABB scanArea = this.getBoundingBox().deflate(0.45, 0.9, 0.45).move(forwardVect.x() * 0.5d, -0.95, forwardVect.z() * 0.5d);
            List<ItemEntity> items = this.level.getEntitiesOfClass(ItemEntity.class, scanArea);
            items.stream().filter(ie -> !ie.hasPickUpDelay()).findFirst().ifPresent((itemEntity) -> {
                boolean traded = false;
                for (Map.Entry<Set<Item>, WeightedRandomList<TradeEntryReducedObj>> tradeEntries : QueensTradeManager.QUEENS_TRADE_MANAGER.tradeReduced.entrySet()) {
                    if (tradeEntries.getKey().contains(itemEntity.getItem().getItem())) {
                        for (int i = 0; i < itemEntity.getItem().getCount(); i++) {
                            Optional<TradeEntryReducedObj> reward = tradeEntries.getValue().getRandom(this.random);
                            if (reward.isPresent()) {
                                spawnReward(forwardVect, sideVect, reward.get(), itemEntity.getItem());
                                traded = true;
                            }
                        }
                        if (traded) {
                            break;
                        }
                    }
                }

                if (traded) {
                    itemEntity.remove(RemovalReason.DISCARDED);
                }
                else {
                    itemEntity.remove(RemovalReason.DISCARDED);
                    ItemEntity rejectedItemEntity = new ItemEntity(
                            this.level,
                            this.getX() + (sideVect.x() * 1.75) + (forwardVect.x() * 1),
                            this.getY() + 0.3,
                            this.getZ() + (sideVect.z() * 1.75) + (forwardVect.x() * 1),
                            itemEntity.getItem(),
                            (this.random.nextFloat() - 0.5f) / 10 + forwardVect.x() / 3,
                            0.4f,
                            (this.random.nextFloat() - 0.5f) / 10 + forwardVect.z() / 3);
                    this.level.addFreshEntity(rejectedItemEntity);
                    rejectedItemEntity.setDefaultPickUpDelay();
                    spawnAngryParticles(2);
                }

                setThrowCooldown(50);
            });
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.isAngry()) {
            return InteractionResult.FAIL;
        }

        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();

        if (stack.equals(ItemStack.EMPTY) && player instanceof ServerPlayer serverPlayer) {
            if (finalbeeQueenAdvancementDone(serverPlayer)) {
                EntityMisc capability = serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).orElseThrow(RuntimeException::new);
                long timeDiff = this.level.getGameTime() - capability.tradeResetPrimedTime;
                if (timeDiff < 200 && timeDiff > 10) {
                    Iterable<Advancement> advancements = serverPlayer.createCommandSourceStack().getAdvancement(BzCriterias.QUEENS_DESIRE_ROOT_ADVANCEMENT).getChildren();
                    for (Advancement advancement : advancements) {
                        AdvancementProgress advancementprogress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
                        for(String criteria : advancementprogress.getCompletedCriteria()) {
                            serverPlayer.getAdvancements().revoke(advancement, criteria);
                        }
                    }
                    capability.tradeResetPrimedTime = -1000;
                    capability.receivedEssencePrize = false;
                    serverPlayer.displayClientMessage(Component.translatable("entity.the_bumblezone.beehemoth_queen.reset_advancements"), false);
                }
                else {
                    capability.tradeResetPrimedTime = this.level.getGameTime();
                    serverPlayer.displayClientMessage(Component.translatable("entity.the_bumblezone.beehemoth_queen.advancements_warning"), false);
                }
            }

            return InteractionResult.PASS;
        }

        boolean traded = false;
        for (Map.Entry<Set<Item>, WeightedRandomList<TradeEntryReducedObj>> tradeEntries : QueensTradeManager.QUEENS_TRADE_MANAGER.tradeReduced.entrySet()) {
            if (tradeEntries.getKey().contains(item)) {
                if (this.level.isClientSide()) {
                    return InteractionResult.SUCCESS;
                }

                Vec3 forwardVect = Vec3.directionFromRotation(0, this.getVisualRotationYInDegrees());
                Vec3 sideVect = Vec3.directionFromRotation(0, this.getVisualRotationYInDegrees() - 90);

                Optional<TradeEntryReducedObj> reward = tradeEntries.getValue().getRandom(this.random);
                if (reward.isPresent()) {
                    spawnReward(forwardVect, sideVect, reward.get(), stack);
                    traded = true;
                }
                if (traded) {
                    break;
                }
            }
        }

        if (!this.level.isClientSide()) {
            if (!traded) {
                spawnAngryParticles(2);
            }
            else {
                setThrowCooldown(50);
                stack.shrink(1);
                player.setItemInHand(hand, stack);

                if (player instanceof ServerPlayer serverPlayer) {
                    BzCriterias.BEE_QUEEN_HAND_TRADE_TRIGGER.trigger(serverPlayer);
                    EntityMisc.onQueenBeeTrade(serverPlayer);

                    if (finalbeeQueenAdvancementDone(serverPlayer)) {
                        EntityMisc capability = serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).orElseThrow(RuntimeException::new);
                        if (!capability.receivedEssencePrize) {
                            Vec3 forwardVect = Vec3.directionFromRotation(0, this.getVisualRotationYInDegrees());
                            Vec3 sideVect = Vec3.directionFromRotation(0, this.getVisualRotationYInDegrees() - 90);
                            spawnReward(forwardVect, sideVect, new TradeEntryReducedObj(BzItems.ESSENCE_OF_THE_BEES.get(), 1, 1000, 1), ItemStack.EMPTY);
                            capability.receivedEssencePrize = true;
                            serverPlayer.displayClientMessage(Component.translatable("entity.the_bumblezone.beehemoth_queen.mention_reset"), false);
                        }
                    }
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    private static boolean finalbeeQueenAdvancementDone(ServerPlayer serverPlayer) {
        Advancement advancement = serverPlayer.createCommandSourceStack().getAdvancement(BzCriterias.QUEENS_DESIRE_FINAL_ADVANCEMENT);
        Map<Advancement, AdvancementProgress> advancementsProgressMap = ((PlayerAdvancementsAccessor)serverPlayer.getAdvancements()).getAdvancements();
        return advancement != null &&
                advancementsProgressMap.containsKey(advancement) &&
                advancementsProgressMap.get(advancement).isDone();
    }

    private void spawnReward(Vec3 forwardVect, Vec3 sideVect, TradeEntryReducedObj reward, ItemStack originalItem) {
        ItemStack rewardItem = reward.item().getDefaultInstance();

        if (originalItem.is(BzTags.SHULKER_BOXES) && rewardItem.is(BzTags.SHULKER_BOXES) && originalItem.hasTag()) {
            rewardItem.getOrCreateTag().merge(originalItem.getOrCreateTag());
        }
        else if (originalItem.is(ItemTags.BANNERS) && rewardItem.is(ItemTags.BANNERS) && originalItem.hasTag()) {
            rewardItem.getOrCreateTag().merge(originalItem.getOrCreateTag());
        }

        rewardItem.setCount(reward.count());
        ItemEntity rewardItemEntity = new ItemEntity(
                this.level,
                this.getX() + (sideVect.x() * 1.75) + (forwardVect.x() * 1),
                this.getY() + 0.3,
                this.getZ() + (sideVect.z() * 1.75) + (forwardVect.x() * 1),
                rewardItem,
                (this.random.nextFloat() - 0.5f) / 10 + forwardVect.x() / 4,
                0.3f,
                (this.random.nextFloat() - 0.5f) / 10 + forwardVect.z() / 4);
        this.level.addFreshEntity(rewardItemEntity);
        rewardItemEntity.setDefaultPickUpDelay();
        spawnHappyParticles();

        if (reward.xpReward() > 0 && this.level instanceof ServerLevel serverLevel) {
            ExperienceOrb.award(
                    serverLevel,
                    new Vec3(this.getX() + (forwardVect.x() * 1),
                            this.getY() + 0.3,
                            this.getZ() + (forwardVect.x() * 1)),
                    reward.xpReward());
        }

        level.playSound(
                null,
                this.blockPosition(),
                BzSounds.BEE_QUEEN_HAPPY.get(),
                SoundSource.NEUTRAL,
                1.0F,
                (level.getRandom().nextFloat() * 0.2F) + 0.6F);
    }

    private void spawnAngryParticles(int particles) {
        if(!this.level.isClientSide()) {
            ((ServerLevel)this.level).sendParticles(
                    ParticleTypes.ANGRY_VILLAGER,
                    getX(),
                    getY() + 0.45f,
                    getZ(),
                    particles,
                    this.level.getRandom().nextFloat() - 0.5f,
                    this.level.getRandom().nextFloat() * 0.4f + 0.4f,
                    this.level.getRandom().nextFloat() - 0.5f,
                    this.level.getRandom().nextFloat() * 0.8f + 0.4f);
        }
    }

    private void spawnHappyParticles() {
        ((ServerLevel)this.level).sendParticles(
                ParticleTypes.HAPPY_VILLAGER,
                getX(),
                getY() + 0.75d,
                getZ(),
                5,
                0.8d,
                0.75d,
                0.8d,
                this.level.getRandom().nextFloat() + 0.5d);
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        Bee bee = EntityType.BEE.create(serverWorld);
        bee.setBaby(true);
        return bee;
    }

    @Override
    public int getHeadRotSpeed() {
        return 1;
    }

    @Override
    public int getMaxHeadXRot() {
        return 90;
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
        if (DATA_POSE.equals(entityDataAccessor)) {
            Pose pose = this.getPose();
        }

        super.onSyncedDataUpdated(entityDataAccessor);
    }

    public int getThrowCooldown() {
        return this.entityData.get(THROWCOOLDOWN);
    }

    public void setThrowCooldown(Integer cooldown) {
        this.entityData.set(THROWCOOLDOWN, cooldown);
    }

    public int getBeeSpawnCooldown() {
        return this.entityData.get(BEESPAWNCOOLDOWN);
    }

    public void setBeeSpawnCooldown(Integer cooldown) {
        this.entityData.set(BEESPAWNCOOLDOWN, cooldown);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    @Override
    public void setRemainingPersistentAngerTime(int remainingPersistentAngerTime) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, remainingPersistentAngerTime);
    }

    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID uuid) {
        this.persistentAngerTarget = uuid;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public void stopBeingAngry() {
        NeutralMob.super.stopBeingAngry();
        this.setBeeSpawnCooldown(0);
        this.setTarget(null);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockState) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return BzSounds.BEE_QUEEN_LOOP.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return BzSounds.BEE_QUEEN_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BzSounds.BEE_QUEEN_DEATH.get();
    }

    public static class DirectPathNavigator extends GroundPathNavigation {

        private final Mob mob;

        public DirectPathNavigator(Mob mob, Level world) {
            super(mob, world);
            this.mob = mob;
        }

        @Override
        public void tick() {
            ++this.tick;
        }

        @Override
        public boolean moveTo(double x, double y, double z, double speedIn) {
            mob.getMoveControl().setWantedPosition(x, y, z, speedIn);
            return true;
        }

        @Override
        public boolean moveTo(Entity entityIn, double speedIn) {
            mob.getMoveControl().setWantedPosition(entityIn.getX(), entityIn.getY(), entityIn.getZ(), speedIn);
            return true;
        }
    }

    public class AlwaysLookAtPlayerGoal extends Goal {
        protected final Mob mob;
        @Nullable
        protected Entity lookAt;
        protected final float lookDistance;
        private final boolean onlyHorizontal;
        protected final Class<? extends LivingEntity> lookAtType;
        protected final TargetingConditions lookAtContext;

        public AlwaysLookAtPlayerGoal(Mob mob, Class<? extends LivingEntity> lookAtType, float lookDistance) {
            this(mob, lookAtType, lookDistance, false);
        }

        public AlwaysLookAtPlayerGoal(Mob mob, Class<? extends LivingEntity> lookAtType, float lookDistance, boolean onlyHorizontal) {
            this.mob = mob;
            this.lookAtType = lookAtType;
            this.lookDistance = lookDistance;
            this.onlyHorizontal = onlyHorizontal;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
            if (lookAtType == Player.class) {
                this.lookAtContext = TargetingConditions.forNonCombat().range(lookDistance).selector((livingEntity) -> EntitySelector.notRiding(mob).test(livingEntity));
            }
            else {
                this.lookAtContext = TargetingConditions.forNonCombat().range(lookDistance);
            }
        }

        public boolean canUse() {
            if (this.mob.getTarget() != null) {
                this.lookAt = this.mob.getTarget();
            }

            if (this.lookAtType == Player.class) {
                this.lookAt = this.mob.level.getNearestPlayer(this.lookAtContext, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
            }
            else {
                this.lookAt = this.mob.level.getNearestEntity(this.mob.level.getEntitiesOfClass(this.lookAtType, this.mob.getBoundingBox().inflate((double)this.lookDistance, 3.0D, (double)this.lookDistance), (p_148124_) -> true), this.lookAtContext, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
            }

            return this.lookAt != null;
        }

        public boolean canContinueToUse() {
            if (!this.lookAt.isAlive()) {
                return false;
            }
            else return !(this.mob.distanceToSqr(this.lookAt) > (double) (this.lookDistance * this.lookDistance));
        }

        public void start() {
        }

        public void stop() {
            this.lookAt = null;
        }

        public void tick() {
            if (this.lookAt != null && this.lookAt.isAlive()) {
                double y = this.onlyHorizontal ? this.mob.getEyeY() : this.lookAt.getEyeY();
                this.mob.getLookControl().setLookAt(this.lookAt.getX(), y, this.lookAt.getZ(), 0.05f, this.mob.getMaxHeadXRot());
            }
        }
    }

    public static class AngerableMeleeAttackGoal extends Goal {
        protected final BeeQueenEntity mob;
        private int ticksUntilNextAttack;

        public AngerableMeleeAttackGoal(BeeQueenEntity mob) {
            this.mob = mob;
        }

        public boolean canUse() {
            return this.mob.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return mob.getTarget() != null && mob.getTarget().isAlive();
        }

        public void start() {
            this.ticksUntilNextAttack = 0;
        }

        public void stop() {}

        public void tick() {
            LivingEntity target = this.mob.getTarget();
            if (target != null && target.isAlive()) {
                double distance = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
                this.checkAndPerformAttack(this.mob.getTarget(), distance);
            }
        }

        protected void resetAttackCooldown() {
            this.ticksUntilNextAttack = this.adjustedTickDelay(20);
        }

        protected void checkAndPerformAttack(LivingEntity target, double distance) {
            double attackReachSqr1 = this.getAttackReachSqr(target);
            if (distance <= attackReachSqr1 && this.ticksUntilNextAttack <= 0) {
                this.resetAttackCooldown();
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(target);
                this.mob.spawnAngryParticles(4);
            }
        }

        protected double getAttackReachSqr(LivingEntity livingEntity) {
            return this.mob.getBbWidth() * 1.2f;
        }
    }
}
