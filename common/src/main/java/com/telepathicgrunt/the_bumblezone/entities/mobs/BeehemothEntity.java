package com.telepathicgrunt.the_bumblezone.entities.mobs;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.LivingEntityFlyingSoundInstance;
import com.telepathicgrunt.the_bumblezone.configs.BzBeeAggressionConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.entities.BeeInteractivity;
import com.telepathicgrunt.the_bumblezone.entities.goals.BeehemothFlyingStillGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.BeehemothRandomFlyGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.BeehemothTemptGoal;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PlayerRideable;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class BeehemothEntity extends TamableAnimal implements FlyingAnimal, Saddleable, PlayerRideable {

    private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(BeehemothEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> QUEEN = SynchedEntityData.defineId(BeehemothEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> FRIENDSHIP = SynchedEntityData.defineId(BeehemothEntity.class, EntityDataSerializers.INT);
    private static final MutableComponent QUEEN_NAME = Component.translatable("entity.the_bumblezone.beehemoth_queen");
    public static final int TICKS_PER_FLAP = Mth.ceil(1.4959966F);
    private boolean stopWandering = false;
    public float offset1, offset2, offset3, offset4, offset5, offset6;
    public boolean movingStraightUp = false;
    public boolean movingStraightDown = false;
    private boolean wasOnGround = false;
    public float flyingSpeed = 0.02F;

    public BeehemothEntity(EntityType<? extends BeehemothEntity> type, Level world) {
        super(type, world);
        this.moveControl = new MoveHelperController(this);
        this.offset1 = (this.random.nextFloat() - 0.5f);
        this.offset2 = (this.random.nextFloat() - 0.5f);
        this.offset3 = (this.random.nextFloat() - 0.5f);
        this.offset4 = (this.random.nextFloat() - 0.5f);
        this.offset5 = (this.random.nextFloat() - 0.5f);
        this.offset6 = (this.random.nextFloat() - 0.5f);
    }

    @Override
    protected Component getTypeName() {
        if (isQueen()) {
            return QUEEN_NAME;
        }
        return super.getTypeName();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SADDLED, false);
        this.entityData.define(QUEEN, false);
        this.entityData.define(FRIENDSHIP, 0);
    }

    public static AttributeSupplier.Builder getAttributeBuilder() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.FLYING_SPEED, 0.4000000059604645D)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 128.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new BeehemothTemptGoal(this, 0.012D, Ingredient.of(BzTags.BEEHEMOTH_FAST_LURING_DESIRED_ITEMS)));
        this.goalSelector.addGoal(2, new BeehemothTemptGoal(this, 0.006D, Ingredient.of(BzTags.BEEHEMOTH_DESIRED_ITEMS)));
        this.goalSelector.addGoal(3, new BeehemothFlyingStillGoal(this));
        this.goalSelector.addGoal(4, new BeehemothRandomFlyGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 60));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new FloatGoal(this));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("saddled", isSaddled());
        tag.putBoolean("queen", isQueen());
        tag.putInt("friendship", getFriendship());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setSaddled(tag.getBoolean("saddled"));
        setQueen(tag.contains("queen") && tag.getBoolean("queen"));
        setFriendship(tag.getInt("friendship"));
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new DirectPathNavigator(this, pLevel);
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    public boolean isQueen() {
        return this.entityData.get(QUEEN);
    }

    @Override
    public boolean isSaddleable() {
        return this.isAlive() && !this.isBaby() && this.isTame();
    }

    @Override
    public void equipSaddle(SoundSource soundSource) {
        this.entityData.set(SADDLED, true);
        if (soundSource != null) {
            this.level().playSound(null, this, SoundEvents.HORSE_SADDLE, soundSource, 0.5F, 1.0F);
        }
    }

    public void setSaddled(boolean saddled) {
        this.entityData.set(SADDLED, saddled);
    }

    @Override
    public boolean isSaddled() {
        return this.entityData.get(SADDLED);
    }

    public void setQueen(boolean queen) {
        this.entityData.set(QUEEN, queen);
    }

    public int getFriendship() {
        return this.entityData.get(FRIENDSHIP);
    }

    public void setFriendship(Integer newFriendship) {
        this.entityData.set(FRIENDSHIP, Math.min(Math.max(newFriendship, -100), 1000));
    }

    public void addFriendship(Integer deltaFriendship) {
        this.entityData.set(FRIENDSHIP, Math.min(Math.max(getFriendship() + deltaFriendship, -100), 1000));
    }

    public boolean isStopWandering() {
        return stopWandering;
    }

    @Override
    public void makeStuckInBlock(BlockState blockState, Vec3 speedMult) {
        if (blockState.getBlock() instanceof SweetBerryBushBlock) {
            return;
        }
        super.makeStuckInBlock(blockState, speedMult);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        if (damageSource == level().damageSources().sweetBerryBush()) {
            return true;
        }
        return super.isInvulnerableTo(damageSource);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (isInvulnerableTo(source)) {
            return false;
        }
        else if(isOnPortalCooldown() && source == level().damageSources().inWall()) {
            spawnMadParticles();
            playHurtSound(source);
            return false;
        }
        else {
            Entity entity = source.getEntity();
            if (!this.isNoAi()) {
                if (entity != null && entity.getUUID().equals(getOwnerUUID())) {
                    addFriendship((int) (-3 * amount));
                }
                if (BzBeeAggressionConfigs.aggressiveBees &&
                    BzBeeAggressionConfigs.beehemothTriggersWrath &&
                    entity instanceof LivingEntity livingEntity)
                {
                    addFriendship((int) (-amount));
                    if (!(livingEntity instanceof Player player && (player.isCreative() || level().getDifficulty() == Difficulty.PEACEFUL)) &&
                        (livingEntity.level().dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) || BzBeeAggressionConfigs.allowWrathOfTheHiveOutsideBumblezone) &&
                        !livingEntity.isSpectator())
                    {
                        if (livingEntity.hasEffect(BzEffects.PROTECTION_OF_THE_HIVE.get())) {
                            livingEntity.removeEffect(BzEffects.PROTECTION_OF_THE_HIVE.get());
                        }
                        else {
                            //Now all bees nearby in Bumblezone will get VERY angry!!!
                            livingEntity.addEffect(new MobEffectInstance(BzEffects.WRATH_OF_THE_HIVE.get(), BzBeeAggressionConfigs.howLongWrathOfTheHiveLasts, 2, false, BzBeeAggressionConfigs.showWrathOfTheHiveParticles, true));
                        }
                    }
                }
                else {
                    addFriendship((int) -amount);
                }
                setOrderedToSit(false);
            }

            spawnMadParticles();
            return super.hurt(source, amount);
        }
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
        return !worldReader.containsAnyLiquid(box) && worldReader.getBlockStates(box).noneMatch(state -> state.blocksMotion()) && worldReader.isUnobstructed(this);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.isNoAi()) {
            return InteractionResult.PASS;
        }

        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();
        ResourceLocation itemRL = BuiltInRegistries.ITEM.getKey(item);
        if (this.level().isClientSide) {
            if (isTame() && isOwnedBy(player)) {
                return InteractionResult.SUCCESS;
            } 
            else {
                return !(getHealth() < getMaxHealth()) && isTame() ? InteractionResult.PASS : InteractionResult.SUCCESS;
            }
        }
        else {
            // Healing and befriending Beehemoth
            if (isTame()) {
                if (isOwnedBy(player)) {
                    if (stack.is(BzTags.BEE_FEEDING_ITEMS) && !player.isShiftKeyDown()) {
                        if(stack.is(BzTags.ROYAL_JELLY_BUCKETS)) {
                            heal(40);
                            BeeInteractivity.calmAndSpawnHearts(this.level(), player, this, 1f, 30);
                            addFriendship(1000);
                            this.addEffect(new MobEffectInstance(BzEffects.BEENERGIZED.get(), 90000, 3, true, true, true));
                            for (int i = 0; i < 75; i++) {
                                spawnParticles(this.level(), this.position(), this.random, 0.1D, 0.1D, 0.1);
                            }
                            return InteractionResult.PASS;
                        }
                        else if(item == BzItems.ROYAL_JELLY_BOTTLE.get()) {
                            heal(10);
                            BeeInteractivity.calmAndSpawnHearts(this.level(), player, this, 1f, 10);
                            addFriendship(250);
                            this.addEffect(new MobEffectInstance(BzEffects.BEENERGIZED.get(), 20000, 3, true, true, true));
                            for (int i = 0; i < 30; i++) {
                                spawnParticles(this.level(), this.position(), this.random, 0.1D, 0.1D, 0.1);
                            }
                            return InteractionResult.PASS;
                        }
                        else if(item == BzItems.BEE_BREAD.get()) {
                            heal(2);
                            BeeInteractivity.calmAndSpawnHearts(this.level(), player, this, 0.8f, 5);
                            addFriendship(5);
                            return InteractionResult.PASS;
                        }
                        else if (stack.is(BzTags.HONEY_BUCKETS)) {
                            heal(getMaxHealth() - getHealth());
                            BeeInteractivity.calmAndSpawnHearts(this.level(), player, this, 0.8f, 5);
                            addFriendship(5);
                        }
                        else if (itemRL.getPath().contains("honey")) {
                            heal(2);
                            BeeInteractivity.calmAndSpawnHearts(this.level(), player, this, 0.3f, 3);
                            addFriendship(3);
                        }
                        else {
                            heal(1);
                            BeeInteractivity.calmAndSpawnHearts(this.level(), player, this, 0.1f, 3);
                            addFriendship(1);
                        }

                        GeneralUtils.givePlayerItem(player, hand, ItemStack.EMPTY, true, true);

                        player.swing(hand, true);
                        return InteractionResult.CONSUME;
                    }

                    if (item == Items.SADDLE && !isSaddled()) {
                        return InteractionResult.PASS;
                    }

                    if(player.isShiftKeyDown()) {
                        if (isSaddled() && isInSittingPose() && stack.isEmpty()) {
                            setSaddled(false);
                            ItemStack saddle = new ItemStack(Items.SADDLE);
                            if (player.addItem(saddle)) {
                                ItemEntity entity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), saddle);
                                player.level().addFreshEntity(entity);
                            }
                        }
                        else {
                            setOrderedToSit(!isOrderedToSit());
                            this.navigation.stop();
                            setTarget(null);
                        }
                        return InteractionResult.SUCCESS;
                    }

                    if (!isVehicle() && !player.isSecondaryUseActive()) {
                        if (!this.level().isClientSide) {
                            player.startRiding(this);
                            setOrderedToSit(false);
                        }

                        return InteractionResult.sidedSuccess(this.level().isClientSide);
                    }
                }
            }
            // Taming Beehemoth
            else if (stack.is(BzTags.BEE_FEEDING_ITEMS)) {
                if(getFriendship() >= 0) {
                    float tameChance;
                    int friendshipAmount = 6;
                    if (stack.is(BzTags.ROYAL_JELLY_BUCKETS)) {
                        friendshipAmount = 1000;
                        tameChance = 1f;
                        for (int i = 0; i < 75; i++) {
                            spawnParticles(this.level(), this.position(), this.random, 0.1D, 0.1D, 0.1);
                        }
                    }
                    else if (item == BzItems.ROYAL_JELLY_BOTTLE.get()) {
                        friendshipAmount = 250;
                        tameChance = 1f;
                        for (int i = 0; i < 30; i++) {
                            spawnParticles(this.level(), this.position(), this.random, 0.1D, 0.1D, 0.1);
                        }
                    }
                    else if (stack.is(BzTags.HONEY_BUCKETS) || item == BzItems.BEE_BREAD.get()) {
                        tameChance = 0.25f;
                    }
                    else if (itemRL.getPath().contains("honey")) {
                        tameChance = 0.1f;
                    }
                    else {
                        tameChance = 0.067f;
                    }

                    if (this.random.nextFloat() < tameChance) {
                        tame(player);
                        setFriendship(friendshipAmount);
                        setOrderedToSit(true);
                        this.level().broadcastEntityEvent(this, (byte) 7);
                    }
                    else {
                        this.level().broadcastEntityEvent(this, (byte) 6);
                    }
                }
                else {
                    addFriendship(1);
                    if (stack.is(BzTags.ROYAL_JELLY_BUCKETS)) {
                        addFriendship(1000);
                        for (int i = 0; i < 75; i++) {
                            spawnParticles(this.level(), this.position(), this.random, 0.1D, 0.1D, 0.1);
                        }
                        return InteractionResult.PASS;
                    }
                    else if (item == BzItems.ROYAL_JELLY_BOTTLE.get()) {
                        addFriendship(250);
                        for (int i = 0; i < 30; i++) {
                            spawnParticles(this.level(), this.position(), this.random, 0.1D, 0.1D, 0.1);
                        }
                        return InteractionResult.PASS;
                    }
                    else if(item == BzItems.BEE_BREAD.get()) {
                        addFriendship(5);
                        return InteractionResult.PASS;
                    }
                    else if (stack.is(BzTags.HONEY_BUCKETS)) {
                        addFriendship(3);
                    }
                    else if (itemRL.getPath().contains("honey")) {
                        addFriendship(2);
                    }
                    else {
                        addFriendship(1);
                    }
                }

                GeneralUtils.givePlayerItem(player, hand, ItemStack.EMPTY, true, true);

                setPersistenceRequired();
                player.swing(hand, true);

                if(getFriendship() < 0) {
                    spawnMadParticles();
                }

                return InteractionResult.CONSUME;
            }

            InteractionResult actionresulttype1 = super.mobInteract(player, hand);
            if (actionresulttype1.consumesAction()) {
                setPersistenceRequired();
            }

            if(getFriendship() < 0) {
                spawnMadParticles();
            }

            return actionresulttype1;
        }
    }

    private void spawnMadParticles() {
        if (!this.level().isClientSide()) {
            ((ServerLevel) this.level()).sendParticles(
                    ParticleTypes.ANGRY_VILLAGER,
                    getX(),
                    getY(),
                    getZ(),
                    Math.min(Math.max(1, getFriendship() / -3), 7),
                    this.getRandom().nextFloat() - 0.5f,
                    this.getRandom().nextFloat() * 0.4f + 0.4f,
                    this.getRandom().nextFloat() - 0.5f,
                    this.getRandom().nextFloat() * 0.8f + 0.4f);
        }
    }

    @Override
    public void positionRider(Entity passenger) {
        if (hasPassenger(passenger)) {
            float radius = -0.25F;
            float angle = (0.01745329251F * this.yBodyRot);
            double extraX = radius * Mth.sin((float) (Math.PI + angle));
            double extraZ = radius * Mth.cos(angle);
            passenger.setPos(getX() + extraX, getY() + getPassengersRidingOffset() + passenger.getMyRidingOffset(), getZ() + extraZ);

            double currentSpeed = getDeltaMovement().length();
            if(currentSpeed > 0.000001D &&
                this.getRandom().nextFloat() < 0.0085D &&
                passenger.getUUID().equals(getOwnerUUID()))
            {
                addFriendship(1);
            }
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        float f = Math.min(0.25F, this.walkAnimation.speed());
        float f1 = this.walkAnimation.position();
        return (double) getBbHeight() - 0.2D + (double) (0.12F * Mth.cos(f1 * 0.7F) * 0.7F * f);
    }

    @Override
    protected void removePassenger(Entity entity) {
        super.removePassenger(entity);
        if(entity == getOwner()) {
            setOrderedToSit(true);
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void playBlockFallSound() {
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockState) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return BzSounds.BEEHEMOTH_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BzSounds.BEEHEMOTH_DEATH.get();
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket clientboundAddMobPacket) {
        super.recreateFromPacket(clientboundAddMobPacket);
        LivingEntityFlyingSoundInstance.playSound(this, BzSounds.BEEHEMOTH_LOOP.get());
    }

    @Override
    public void tick() {
        super.tick();
        stopWandering = isLeashed();

        // Become queen if friendship is maxed out.
        if(!isQueen() && getFriendship() >= 1000) {
            setQueen(true);
            if(getOwner() instanceof ServerPlayer serverPlayer) {
                BzCriterias.QUEEN_BEEHEMOTH_TRIGGER.trigger(serverPlayer);
            }

            if(this.level().isClientSide()) {
                for (int i = 0; i < 75; i++) {
                    spawnParticles(this.level(), this.position(), this.random, 0.1D, 0.1D, 0.1);
                }
            }
        }
        // Become untamed if bee is no longer a friend
        else if(getFriendship() < 0 && isTame()) {
            ejectPassengers();
            if(this.getRandom().nextFloat() < 0.01f) spawnMadParticles();
        }

        if(onGround()) {
            this.setDeltaMovement(
                    this.getDeltaMovement().x(),
                    this.getDeltaMovement().y() - 0.006D,
                    this.getDeltaMovement().z()
            );
        }
        else if (wasOnGround) {
            this.setDeltaMovement(
                    this.getDeltaMovement().x(),
                    this.getDeltaMovement().y() + 0.006D,
                    this.getDeltaMovement().z()
            );
        }
    }

    public static void spawnParticles(LevelAccessor world, Vec3 location, RandomSource random, double speedXZModifier, double speedYModifier, double initYSpeed) {
        double xOffset = (random.nextFloat() * 2) - 1;
        double yOffset = (random.nextFloat() * 2) - 1;
        double zOffset = (random.nextFloat() * 2) - 1;

        world.addParticle(
                ParticleTypes.FIREWORK,
                location.x() + xOffset,
                location.y() + yOffset + 1,
                location.z() + zOffset,
                random.nextGaussian() * speedXZModifier,
                (random.nextGaussian() * speedYModifier) + initYSpeed,
                random.nextGaussian() * speedXZModifier);
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        return null;
    }

    @Override
    public void setLeashedTo(Entity entity, boolean sendAttachNotification) {
        super.setLeashedTo(entity, sendAttachNotification);
        stopWandering = true;
    }

    @Override
    public boolean isFlying() {
        return this.tickCount % TICKS_PER_FLAP == 0;
    }

    @Nullable
    public LivingEntity getControllingPassenger() {
        if (this.isSaddled()) {
            Entity firstPassenger = this.getFirstPassenger();
            if (firstPassenger instanceof LivingEntity livingEntity) {
                return livingEntity;
            }
        }

        return null;
    }

    @Override
    public float getFlyingSpeed() {
        if (!this.isTame() || (!this.isOrderedToSit() && !this.isVehicle())) {
            return (float) ((getAttributeValue(Attributes.FLYING_SPEED) / Attributes.FLYING_SPEED.getDefaultValue()) * 0.0038f);
        }

        float reducedAttrDiff = (float) ((getAttributeValue(Attributes.FLYING_SPEED) - Attributes.FLYING_SPEED.getDefaultValue()) / 3F);
        float adjustedSpeedMultiplier = (float) ((reducedAttrDiff + Attributes.FLYING_SPEED.getDefaultValue()) / Attributes.FLYING_SPEED.getDefaultValue());
        return adjustedSpeedMultiplier * this.flyingSpeed;
    }

    @Override
    public void travel(Vec3 moveVector) {
        if (this.isAlive() && !this.isNoAi()) {
            LivingEntity livingEntity = this.getControllingPassenger();
            if (this.isVehicle() && livingEntity != null) {
                float startRot = Mth.wrapDegrees(this.getYRot());
                float targetRot = Mth.wrapDegrees(livingEntity.getYRot());
                float lerpedRot = Mth.rotLerp(0.185f, startRot, targetRot);
                this.setYRot(lerpedRot);
                this.yRotO = this.getYRot();
                this.setXRot(livingEntity.getXRot() * 0.5F);
                this.setRot(this.getYRot(), this.getXRot());
                this.yBodyRot = this.getYRot();
                this.yHeadRot = this.yBodyRot;
                double currentSpeed = this.flyingSpeed;
                double queenModifier = this.isQueen() ? 0.55D : 0.15D;
                double friendlyModifier = (this.getFriendship() / 1100D) + 0.05D;
                double speedModifier = (queenModifier + friendlyModifier) * 0.1D;

                double verticalSpeed = (livingEntity.getLookAngle().y() * Math.abs(livingEntity.zza) * 5);
                double forwardSpeed = (livingEntity.zza * 10) ;
                double strafeSpeed = 0;

                if (livingEntity.zza != 0 || this.movingStraightUp || this.movingStraightDown) {
                    currentSpeed = Math.min(BzGeneralConfigs.beehemothSpeed * speedModifier, currentSpeed + 0.003D);
                }
                else {
                    currentSpeed = Math.max(0, currentSpeed - 0.2D);
                }

                if(this.movingStraightUp || this.movingStraightDown) {
                    if(this.movingStraightUp) {
                        verticalSpeed = 10;
                    }
                    if(this.movingStraightDown) {
                        verticalSpeed = -10;
                    }
                }

                if(this.onGround()) {
                    forwardSpeed *= 0.025f;
                    verticalSpeed -= 0.5f;
                }
                else if (wasOnGround) {
                    verticalSpeed += 0.5f;
                }

                if (this.isControlledByLocalInstance()) {
                    this.flyingSpeed = (float) currentSpeed;
                    this.setSpeed(this.getFlyingSpeed());
                    Vec3 moveDir = new Vec3(strafeSpeed, verticalSpeed, forwardSpeed);
                    super.travel(moveDir);
                }
                else if (livingEntity instanceof Player) {
                    this.setDeltaMovement(Vec3.ZERO);
                }

                this.calculateEntityAnimation(false);
                this.tryCheckInsideBlocks();
            }
            else {
                super.travel(moveVector);
            }

            wasOnGround = this.onGround();
        }
    }

    static class MoveHelperController extends MoveControl {
        private final BeehemothEntity beehemothEntity;

        public MoveHelperController(BeehemothEntity beehemothEntity) {
            super(beehemothEntity);
            this.beehemothEntity = beehemothEntity;
        }

        @Override
        public void tick() {

            if (this.operation == Operation.STRAFE) {
                Vec3 vec3 = new Vec3(this.wantedX - beehemothEntity.getX(), this.wantedY - beehemothEntity.getY(), this.wantedZ - beehemothEntity.getZ());
                double d0 = vec3.length();
                beehemothEntity.setDeltaMovement(beehemothEntity.getDeltaMovement().add(0, vec3.scale(this.speedModifier * 0.05D / d0).y(), 0));
                float f = (float) this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
                float f1 = (float) this.speedModifier * f;
                this.strafeForwards = 1.0F;
                this.strafeRight = 0.0F;

                this.mob.setSpeed(f1);
                this.mob.setZza(this.strafeForwards);
                this.mob.setXxa(this.strafeRight);
                this.operation = MoveControl.Operation.WAIT;
            }
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                Vec3 vec3 = new Vec3(
                        this.wantedX - beehemothEntity.getX(),
                        this.wantedY - beehemothEntity.getY(),
                        this.wantedZ - beehemothEntity.getZ());

                double length = vec3.length() / 1.25D;

                if (length < beehemothEntity.getBoundingBox().getSize()) {
                    this.operation = MoveControl.Operation.WAIT;
                    beehemothEntity.setDeltaMovement(beehemothEntity.getDeltaMovement().scale(0.5D));
                }
                else {
                    double localSpeed = this.speedModifier;
                    if (beehemothEntity.isVehicle()) {
                        localSpeed *= 1.5D;
                    }
                    Vec3 newVelocity = beehemothEntity.getDeltaMovement().add(vec3.scale(localSpeed / length));

                    double newYSpeed;
                    if (beehemothEntity.onGround()) {
                        newYSpeed = (newVelocity.y() + 0.009D);
                    }
                    else {
                        newYSpeed = newVelocity.y();
                    }
                    beehemothEntity.setDeltaMovement(newVelocity.x(), newYSpeed, newVelocity.z());

                    float lookAngle = (float)(Mth.atan2(vec3.x(), vec3.z()) * -(double)(180F / (float)Math.PI));
                    beehemothEntity.setYRot(this.rotlerp(beehemothEntity.getYRot(), lookAngle, 90.0F));
                }
            }
        }
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 vec3 = new Vec3(getX(), getEyeY(), getZ());
        return this.level().clip(new ClipContext(vec3, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.MISS;
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
