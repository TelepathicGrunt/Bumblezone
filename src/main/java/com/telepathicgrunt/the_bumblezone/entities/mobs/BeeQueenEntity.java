package com.telepathicgrunt.the_bumblezone.entities.mobs;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.rendering.beequeen.BeeQueenPose;
import com.telepathicgrunt.the_bumblezone.components.MiscComponent;
import com.telepathicgrunt.the_bumblezone.configs.BzConfig;
import com.telepathicgrunt.the_bumblezone.entities.goals.BeeQueenAlwaysLookAtPlayerGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.BeeQueenAngerableMeleeAttackGoal;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.TradeEntryReducedObj;
import com.telepathicgrunt.the_bumblezone.mixin.entities.PlayerAdvancementsAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class BeeQueenEntity extends Animal implements NeutralMob {

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState itemThrownAnimationState = new AnimationState();
    public final AnimationState itemRejectAnimationState = new AnimationState();
    public static final EntityDataSerializer<BeeQueenPose> QUEEN_POSE_SERIALIZER = EntityDataSerializer.simpleEnum(BeeQueenPose.class);
    private static final EntityDataAccessor<Integer> THROWCOOLDOWN = SynchedEntityData.defineId(BeeQueenEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> BEESPAWNCOOLDOWN = SynchedEntityData.defineId(BeeQueenEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(BeeQueenEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<BeeQueenPose> QUEEN_POSE = SynchedEntityData.defineId(BeeQueenEntity.class, QUEEN_POSE_SERIALIZER);
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(60, 120);
    private UUID persistentAngerTarget;
    private int underWaterTicks;
    private int poseTicks;

    public BeeQueenEntity(EntityType<? extends BeeQueenEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(THROWCOOLDOWN, 0);
        this.entityData.define(DATA_REMAINING_ANGER_TIME, 1);
        this.entityData.define(BEESPAWNCOOLDOWN, 2);
        this.entityData.define(QUEEN_POSE, BeeQueenPose.NONE);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
        if (QUEEN_POSE.equals(entityDataAccessor)) {
            BeeQueenPose pose = this.getQueenPose();
            if (pose == BeeQueenPose.ATTACKING) {
                this.attackAnimationState.start(this.tickCount);
            }
            else {
                this.attackAnimationState.stop();
            }

            if (pose == BeeQueenPose.ITEM_REJECT) {
                this.itemRejectAnimationState.start(this.tickCount);
            }
            else {
                this.itemRejectAnimationState.stop();
            }

            if (pose == BeeQueenPose.ITEM_THROW) {
                this.itemThrownAnimationState.start(this.tickCount);
            }
            else {
                this.itemThrownAnimationState.stop();
            }
        }

        super.onSyncedDataUpdated(entityDataAccessor);
    }

    public static AttributeSupplier.Builder getAttributeBuilder() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 150.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.1)
                .add(Attributes.ATTACK_DAMAGE, 10.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new BeeQueenAngerableMeleeAttackGoal(this));
        this.goalSelector.addGoal(2, new BeeQueenAlwaysLookAtPlayerGoal(this, Player.class, 60));
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

    public void setQueenPose(BeeQueenPose beeQueenPose) {
        this.entityData.set(QUEEN_POSE, beeQueenPose);
    }

    public BeeQueenPose getQueenPose() {
        return this.entityData.get(QUEEN_POSE);
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
            if (entity instanceof LivingEntity livingEntity &&
                !livingEntity.isSpectator() &&
                !(livingEntity instanceof Player player && player.isCreative()))
            {
                if ((livingEntity.level.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) ||
                    BzConfig.allowWrathOfTheHiveOutsideBumblezone) &&
                    BzConfig.aggressiveBees)
                {
                    if(livingEntity.hasEffect(BzEffects.PROTECTION_OF_THE_HIVE)) {
                        livingEntity.removeEffect(BzEffects.PROTECTION_OF_THE_HIVE);
                    }
                    else {
                        //Now all bees nearby in Bumblezone will get VERY angry!!!
                        livingEntity.addEffect(new MobEffectInstance(BzEffects.WRATH_OF_THE_HIVE, BzConfig.howLongWrathOfTheHiveLasts, 3, false, BzConfig.showWrathOfTheHiveParticles, true));
                    }
                }

                this.startPersistentAngerTimer();
                this.setPersistentAngerTarget(livingEntity.getUUID());
                this.setTarget(livingEntity);
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

        BeeQueenPose pose = this.getQueenPose();
        if (pose != BeeQueenPose.NONE) {
            if (pose == BeeQueenPose.ATTACKING && poseTicks > 17) {
                setQueenPose(BeeQueenPose.NONE);
                poseTicks = 0;
            }
            if (pose == BeeQueenPose.ITEM_REJECT && poseTicks > 20) {
                setQueenPose(BeeQueenPose.NONE);
                poseTicks = 0;
            }
            if (pose == BeeQueenPose.ITEM_THROW && poseTicks > 20) {
                setQueenPose(BeeQueenPose.NONE);
                poseTicks = 0;
            }

            poseTicks++;
        }

        if (this.getLevel().getGameTime() % 200 == 0) {
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
        if (beeCooldown <= 0 && !this.isImmobile()) {
            this.setBeeSpawnCooldown(this.random.nextInt(50) + 75);

            // Grab a nearby air materialposition a bit away
            BlockPos spawnBlockPos = GeneralUtils.getRandomBlockposWithinRange(this, 5, 0);
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

            bee.addEffect(new MobEffectInstance(
                    MobEffects.WITHER,
                    Integer.MAX_VALUE,
                    0,
                    true,
                    false,
                    false));

            this.level.addFreshEntity(bee);
            this.spawnAngryParticles(6);
            setQueenPose(BeeQueenPose.ATTACKING);
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
                    setQueenPose(BeeQueenPose.ITEM_REJECT);
                }

                setThrowCooldown(50);
            });
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.isAngry() || hand == InteractionHand.OFF_HAND) {
            return InteractionResult.FAIL;
        }

        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();

        if (stack.equals(ItemStack.EMPTY) && player instanceof ServerPlayer serverPlayer) {
            if (finalbeeQueenAdvancementDone(serverPlayer)) {
                MiscComponent capability = Bumblezone.MISC_COMPONENT.get(serverPlayer);
                if (!capability.receivedEssencePrize) {
                    Vec3 forwardVect = Vec3.directionFromRotation(0, this.getVisualRotationYInDegrees());
                    Vec3 sideVect = Vec3.directionFromRotation(0, this.getVisualRotationYInDegrees() - 90);
                    spawnReward(forwardVect, sideVect, new TradeEntryReducedObj(BzItems.ESSENCE_OF_THE_BEES, 1, 1000, 1), ItemStack.EMPTY);
                    capability.receivedEssencePrize = true;
                    serverPlayer.displayClientMessage(Component.translatable("entity.the_bumblezone.beehemoth_queen.mention_reset").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GOLD), false);
                }
                else {
                    long timeDiff = this.level.getGameTime() - capability.tradeResetPrimedTime;
                    if (timeDiff < 200 && timeDiff > 10) {
                        resetAdvancementTree(serverPlayer, BzCriterias.QUEENS_DESIRE_ROOT_ADVANCEMENT);
                        capability.resetAllTrackerStats();
                        serverPlayer.displayClientMessage(Component.translatable("entity.the_bumblezone.beehemoth_queen.reset_advancements").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GOLD), false);
                    }
                    else {
                        capability.tradeResetPrimedTime = this.level.getGameTime();
                        serverPlayer.displayClientMessage(Component.translatable("entity.the_bumblezone.beehemoth_queen.advancements_warning").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GOLD), false);
                    }
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
                setQueenPose(BeeQueenPose.ITEM_REJECT);
            }
            else {
                setThrowCooldown(50);
                stack.shrink(1);
                player.setItemInHand(hand, stack);

                if (player instanceof ServerPlayer serverPlayer) {
                    BzCriterias.BEE_QUEEN_HAND_TRADE_TRIGGER.trigger(serverPlayer);
                    MiscComponent.onQueenBeeTrade(serverPlayer);

                    if (finalbeeQueenAdvancementDone(serverPlayer)) {
                        MiscComponent capability = Bumblezone.MISC_COMPONENT.get(serverPlayer);
                        if (!capability.receivedEssencePrize) {
                            Vec3 forwardVect = Vec3.directionFromRotation(0, this.getVisualRotationYInDegrees());
                            Vec3 sideVect = Vec3.directionFromRotation(0, this.getVisualRotationYInDegrees() - 90);
                            spawnReward(forwardVect, sideVect, new TradeEntryReducedObj(BzItems.ESSENCE_OF_THE_BEES, 1, 1000, 1), ItemStack.EMPTY);
                            capability.receivedEssencePrize = true;
                            serverPlayer.displayClientMessage(Component.translatable("entity.the_bumblezone.beehemoth_queen.mention_reset").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GOLD), false);
                        }
                    }
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    private void resetAdvancementTree(ServerPlayer serverPlayer, ResourceLocation advancementRL) {
        Iterable<Advancement> advancements = serverPlayer.server.getAdvancements().getAdvancement(advancementRL).getChildren();
        for (Advancement advancement : advancements) {
            AdvancementProgress advancementprogress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
            for(String criteria : advancementprogress.getCompletedCriteria()) {
                serverPlayer.getAdvancements().revoke(advancement, criteria);
            }
            resetAdvancementTree(serverPlayer, advancement.getId());
        }
    }

    private static boolean finalbeeQueenAdvancementDone(ServerPlayer serverPlayer) {
        Advancement advancement = serverPlayer.server.getAdvancements().getAdvancement(BzCriterias.QUEENS_DESIRE_FINAL_ADVANCEMENT);
        Map<Advancement, AdvancementProgress> advancementsProgressMap = ((PlayerAdvancementsAccessor)serverPlayer.getAdvancements()).getAdvancements();
        return advancement != null &&
                advancementsProgressMap.containsKey(advancement) &&
                advancementsProgressMap.get(advancement).isDone();
    }

    private void spawnReward(Vec3 forwardVect, Vec3 sideVect, TradeEntryReducedObj reward, ItemStack originalItem) {
        ItemStack rewardItem = reward.item().getDefaultInstance();
        setQueenPose(BeeQueenPose.ITEM_THROW);

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

        this.level.playSound(
                null,
                this.blockPosition(),
                BzSounds.BEE_QUEEN_HAPPY,
                SoundSource.NEUTRAL,
                1.0F,
                (this.getRandom().nextFloat() * 0.2F) + 0.6F);
    }

    public void spawnAngryParticles(int particles) {
        if(!this.level.isClientSide()) {
            ((ServerLevel)this.level).sendParticles(
                    ParticleTypes.ANGRY_VILLAGER,
                    getX(),
                    getY() + 0.45f,
                    getZ(),
                    particles,
                    this.getRandom().nextFloat() - 0.5f,
                    this.getRandom().nextFloat() * 0.4f + 0.4f,
                    this.getRandom().nextFloat() - 0.5f,
                    this.getRandom().nextFloat() * 0.8f + 0.4f);
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
                this.getRandom().nextFloat() + 0.5d);
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
        return BzSounds.BEE_QUEEN_LOOP;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return BzSounds.BEE_QUEEN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BzSounds.BEE_QUEEN_DEATH;
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
}
