package com.telepathicgrunt.the_bumblezone.entities.living;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.blocks.EssenceBlockWhite;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.items.essence.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.mixin.entities.EntityAccessor;
import com.telepathicgrunt.the_bumblezone.mixin.entities.LivingEntityAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.*;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CosmicCrystalEntity extends LivingEntity {
    private static final EntityDataAccessor<CosmicCrystalState> COSMIC_CRYSTAL_STATE = SynchedEntityData.defineId(CosmicCrystalEntity.class, BzEntities.COSMIC_CRYSTAL_STATE_SERIALIZER);
    private static final EntityDataAccessor<Integer> INITIAL_ROTATION_ANIMATION_TIMESPAN = SynchedEntityData.defineId(CosmicCrystalEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> STATE_TIMESPAN = SynchedEntityData.defineId(CosmicCrystalEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LASER_START_DELAY = SynchedEntityData.defineId(CosmicCrystalEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LASER_FIRE_START_TIME = SynchedEntityData.defineId(CosmicCrystalEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SYNCED_CURRENT_STATE_TIME_TICK = SynchedEntityData.defineId(CosmicCrystalEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ORBIT_OFFSET_DEGREES = SynchedEntityData.defineId(CosmicCrystalEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DIFFICULTY_BOOST = SynchedEntityData.defineId(CosmicCrystalEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> COLLIDED = SynchedEntityData.defineId(CosmicCrystalEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SECOND_PHASE = SynchedEntityData.defineId(CosmicCrystalEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<UUID>> ESSENCE_CONTROLLER_UUID = SynchedEntityData.defineId(CosmicCrystalEntity.class, BzEntities.UUID_ENTITY_DATA_SERIALIZER);
    private static final EntityDataAccessor<Optional<BlockPos>> ESSENCE_CONTROLLER_BLOCK_POS = SynchedEntityData.defineId(CosmicCrystalEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    private static final EntityDataAccessor<String> ESSENCE_CONTROLLER_DIMENSION = SynchedEntityData.defineId(CosmicCrystalEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> SHIELD = SynchedEntityData.defineId(CosmicCrystalEntity.class, EntityDataSerializers.BOOLEAN);
    private static final Vec3 UP_VECT = new Vec3(0, 1, 0);
    private static final Vec3 POSITIVE_X_VECT = new Vec3(1, 0, 0);
    public static final int MAX_RANGE = 30;
    protected static final TargetingConditions TARGETING_CONDITIONS = TargetingConditions.forCombat().range(MAX_RANGE).selector((livingEntity, _) -> !(livingEntity instanceof CosmicCrystalEntity) && livingEntity.attackable());

    public final AnimationState idleAnimationState = new AnimationState();

    private UUID targetEntityUUID = null;
    private Entity targetEntity = null;
    private Vec3 prevTargetPosition = new Vec3(0, 0, 0);

    public int currentStateTimeTick = 0;
    public int currentTickCount = 0;
    public int animationTimeTick = 0;
    public int prevAnimationTick = 0;
    public Vec3 prevLookAngle = new Vec3(1, 0, 0);
    private boolean laserChargeSoundPlayed = false;
    public int lastPhysicalHit = 0;
    public final ArrayDeque<CosmicCrystalState> pastStates = new ArrayDeque<>();
    private boolean noAI = false;

    public CosmicCrystalEntity(EntityType<? extends CosmicCrystalEntity> entityType, Level level) {
        super(entityType, level);
        this.idleAnimationState.start(this.tickCount);
    }

    public static AttributeSupplier.Builder getAttributeBuilder() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, BzGeneralConfigs.cosmicCrystalHealth)
                .add(Attributes.MOVEMENT_SPEED, 0.1D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.FOLLOW_RANGE, MAX_RANGE);
    }

    @Nullable
    public UUID getEssenceController() {
        return this.entityData.get(ESSENCE_CONTROLLER_UUID).orElse(null);
    }

    public void setEssenceController(UUID essenceController) {
        this.entityData.set(ESSENCE_CONTROLLER_UUID, Optional.ofNullable(essenceController));
    }

    public BlockPos getEssenceControllerBlockPos() {
        return this.entityData.get(ESSENCE_CONTROLLER_BLOCK_POS).orElse(null);
    }

    public void setEssenceControllerBlockPos(BlockPos essenceControllerBlockPos) {
        this.entityData.set(ESSENCE_CONTROLLER_BLOCK_POS,
                essenceControllerBlockPos == null ? Optional.empty() : Optional.of(essenceControllerBlockPos));
    }

    public ResourceKey<Level> getEssenceControllerDimension() {
        String dimensionString = this.entityData.get(ESSENCE_CONTROLLER_DIMENSION);

        if (dimensionString == null || dimensionString.isEmpty()) {
            return null;
        }

        return ResourceKey.create(Registries.DIMENSION, Identifier.tryParse(dimensionString));
    }

    public void setEssenceControllerDimension(ResourceKey<Level> essenceControllerDimension) {
        this.entityData.set(ESSENCE_CONTROLLER_DIMENSION, essenceControllerDimension.identifier().toString());
    }

    public int getOrbitOffsetDegrees() {
        return this.entityData.get(ORBIT_OFFSET_DEGREES);
    }

    public void setOrbitOffsetDegrees(int orbitOffsetDegrees) {
        this.entityData.set(ORBIT_OFFSET_DEGREES, orbitOffsetDegrees);
    }

    public float getDifficultyBoost() {
        return this.entityData.get(DIFFICULTY_BOOST);
    }

    public void setDifficultyBoost(float difficultyBoost) {
        this.entityData.set(DIFFICULTY_BOOST, difficultyBoost);
    }

    public void setCosmicCrystalState(CosmicCrystalState cosmicCrystalState) {
        if(!this.level().isClientSide() && cosmicCrystalState != this.getCosmicCrystalState()) {
            if (this.tickCount > 1) {
                this.currentStateTimeTick = 0;
                this.lastPhysicalHit = 0;
                this.setSecondPhase(false);
            }

            if (this.targetEntity != null) {
                this.prevTargetPosition = this.targetEntity.position();
            }

            this.pastStates.addFirst(this.getCosmicCrystalState());
            if (pastStates.size() > 8) {
                pastStates.removeLast();
            }
            this.entityData.set(COSMIC_CRYSTAL_STATE, cosmicCrystalState);

            // setup timing for each state
            switch (this.getCosmicCrystalState()) {
                case NORMAL -> {
                    this.setInitialRotationAnimationTimespan(40);
                    this.setStateTimespan(Integer.MAX_VALUE);
                }
                case TRACKING_SMASHING_ATTACK -> {
                    this.setInitialRotationAnimationTimespan((int) (100 - (40 * this.getDifficultyBoost()) + (getOrbitOffsetDegrees() / 3)));
                    this.setStateTimespan(450);

                    this.level().playSound(
                            this,
                            this.blockPosition(),
                            BzSounds.COSMIC_CRYSTAL_ENTITY_CRASH_CHARGE.get(),
                            SoundSource.HOSTILE,
                            1,
                            1);
                }
                case TRACKING_SPINNING_ATTACK -> {
                    int timeOffset = (int) (this.getOrbitOffsetDegrees() * this.getDifficultyBoost() * this.getDifficultyBoost());
                    this.setInitialRotationAnimationTimespan(20 + timeOffset);
                    this.setStateTimespan((int) (timeOffset + (this.getDifficultyBoost() * this.getDifficultyBoost() * 80)));
                }
                case VERTICAL_LASER -> {
                    this.setInitialRotationAnimationTimespan((int) (40 + (20 * this.getDifficultyBoost())));
                    this.setLaserStartDelay(20);
                    this.setLaserFireStartTime(60);
                    this.setStateTimespan(400);
                }
                case HORIZONTAL_LASER -> {
                    this.setInitialRotationAnimationTimespan((int) (40 + (20 * this.getDifficultyBoost())));
                    this.setLaserStartDelay(40);
                    this.setLaserFireStartTime(80);
                    this.setStateTimespan(350);
                }
                case SWEEP_LASER -> {
                    this.setInitialRotationAnimationTimespan((int) (40 + (20 * this.getDifficultyBoost())));
                    this.setLaserStartDelay(60);
                    this.setLaserFireStartTime(80);
                    this.setStateTimespan(400);
                }
                case TRACKING_LASER -> {
                    int timeOffset = getOrbitOffsetDegrees() / 3;
                    this.setInitialRotationAnimationTimespan((int) (40 + (20 * this.getDifficultyBoost())));
                    this.setLaserStartDelay((int) (40 + (20 * this.getDifficultyBoost()) + timeOffset));
                    this.setLaserFireStartTime(80 + timeOffset);
                    this.setStateTimespan(180 + timeOffset);
                }
            }
        }
    }

    public CosmicCrystalState getCosmicCrystalState() {
        return this.entityData.get(COSMIC_CRYSTAL_STATE);
    }

    public void setInitialRotationAnimationTimespan(int initialRotationAnimationTimespan) {
        this.animationTimeTick = 0;
        this.prevAnimationTick = 0;
        this.entityData.set(INITIAL_ROTATION_ANIMATION_TIMESPAN, initialRotationAnimationTimespan);
        this.setCollided(false);
    }

    public int getInitialRotationAnimationTimespan() {
        return this.entityData.get(INITIAL_ROTATION_ANIMATION_TIMESPAN);
    }

    public void setStateTimespan(int stateTimespan) {
        this.entityData.set(STATE_TIMESPAN, stateTimespan);
    }

    public int getStateTimespan() {
        return this.entityData.get(STATE_TIMESPAN);
    }

    public void setLaserStartDelay(int laserStartDelay) {
        this.laserChargeSoundPlayed = false;
        this.entityData.set(LASER_START_DELAY, laserStartDelay);
    }

    public int getLaserStartDelay() {
        return this.entityData.get(LASER_START_DELAY);
    }

    public void setLaserFireStartTime(int laserFireStartTime) {
        this.entityData.set(LASER_FIRE_START_TIME, laserFireStartTime);
    }

    public int getLaserFireStartTime() {
        return this.entityData.get(LASER_FIRE_START_TIME);
    }

    public void setSyncedCurrentStateTimeTick(int syncedCurrentStateTimeTick) {
        this.entityData.set(SYNCED_CURRENT_STATE_TIME_TICK, syncedCurrentStateTimeTick);
    }

    public int getSyncedCurrentStateTimeTick() {
        return this.entityData.get(SYNCED_CURRENT_STATE_TIME_TICK);
    }

    public void setCollided(boolean collided) {
        this.entityData.set(COLLIDED, collided);
    }

    public boolean getCollided() {
        return this.entityData.get(COLLIDED);
    }

    public void setSecondPhase(boolean smashingPhase) {
        this.entityData.set(SECOND_PHASE, smashingPhase);
    }

    public boolean getSecondPhase() {
        return this.entityData.get(SECOND_PHASE);
    }

    public void setTargetEntityUUID(UUID targetEntityUUID) {
        this.targetEntityUUID = targetEntityUUID;
    }

    public UUID getTargetEntityUUID() {
        return this.targetEntityUUID;
    }

    public void setShield(boolean shield) {
        this.entityData.set(SHIELD, shield);
    }

    public boolean getShield() {
        return this.entityData.get(SHIELD);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COSMIC_CRYSTAL_STATE, CosmicCrystalState.NORMAL);
        builder.define(INITIAL_ROTATION_ANIMATION_TIMESPAN, 0);
        builder.define(STATE_TIMESPAN, 0);
        builder.define(LASER_START_DELAY, 0);
        builder.define(LASER_FIRE_START_TIME, 0);
        builder.define(SYNCED_CURRENT_STATE_TIME_TICK, 0);
        builder.define(ORBIT_OFFSET_DEGREES, 0);
        builder.define(DIFFICULTY_BOOST, 1F);
        builder.define(COLLIDED, false);
        builder.define(SECOND_PHASE, false);
        builder.define(ESSENCE_CONTROLLER_UUID, Optional.empty());
        builder.define(ESSENCE_CONTROLLER_BLOCK_POS, Optional.empty());
        builder.define(ESSENCE_CONTROLLER_DIMENSION, "");
        builder.define(SHIELD, false);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
        super.onSyncedDataUpdated(entityDataAccessor);
        if (COSMIC_CRYSTAL_STATE.equals(entityDataAccessor) && this.tickCount > 1) {
            this.animationTimeTick = 0;
            this.prevAnimationTick = 0;
            this.currentStateTimeTick = 0;
            this.lastPhysicalHit = 0;

            if (this.targetEntity != null) {
                this.prevTargetPosition = this.targetEntity.position();
            }
        }
        else if (SYNCED_CURRENT_STATE_TIME_TICK.equals(entityDataAccessor)) {
            this.currentStateTimeTick = this.getSyncedCurrentStateTimeTick();
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);

        if (this.getEssenceController() != null) {
            output.store("essenceController", UUIDUtil.CODEC, this.getEssenceController());
        }
        if (this.getEssenceControllerBlockPos() != null) {
            output.store("essenceControllerBlockPos", BlockPos.CODEC, this.getEssenceControllerBlockPos());
        }
        if (this.getEssenceControllerDimension() != null) {
            output.store("essenceControllerDimension", Identifier.CODEC, this.getEssenceControllerDimension().identifier());
        }

        output.putString("cosmicCrystalState", this.getCosmicCrystalState().name());
        output.putInt("initialRotationAnimationTimespan", this.getInitialRotationAnimationTimespan());
        output.putInt("stateTimespan", this.getStateTimespan());
        output.putInt("laserStartDelay", this.getLaserStartDelay());
        output.putInt("laserFireStartTime", this.getLaserFireStartTime());
        output.putBoolean("secondPhase", this.getSecondPhase());
        output.putBoolean("collided", this.getCollided());
        output.putInt("orbitOffsetDegrees", this.getOrbitOffsetDegrees());
        output.putFloat("difficultyBoost", this.getDifficultyBoost());

        output.putInt("currentStateTimeTick", this.currentStateTimeTick);
        output.putInt("animationTimeTick", this.animationTimeTick);
        output.putInt("prevAnimationTick", this.prevAnimationTick);

        if (this.targetEntityUUID != null) {
            output.store("targetEntityUUID", UUIDUtil.CODEC, this.targetEntityUUID);
        }
        output.store("prevLookAngle", Vec3.CODEC, this.prevLookAngle);

        output.putBoolean("NoAI", this.noAI);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);

        this.setEssenceController(input.read("essenceController", UUIDUtil.CODEC).orElse(null));
        input.read("essenceControllerBlockPos", BlockPos.CODEC).ifPresent(this::setEssenceControllerBlockPos);
        input.read("essenceControllerDimension", Identifier.CODEC).ifPresent(dim -> this.setEssenceControllerDimension(ResourceKey.create(Registries.DIMENSION, dim)));

        this.setCosmicCrystalState(CosmicCrystalState.valueOf(input.getStringOr("prevCosmicCrystalState", CosmicCrystalState.NORMAL.name())));
        this.setCosmicCrystalState(CosmicCrystalState.valueOf(input.getStringOr("cosmicCrystalState", CosmicCrystalState.NORMAL.name())));

        this.setInitialRotationAnimationTimespan(input.getIntOr("initialRotationAnimationTimespan", 0));
        this.setStateTimespan(input.getIntOr("stateTimespan", 0));
        this.setLaserStartDelay(input.getIntOr("laserStartDelay", 0));
        this.setLaserFireStartTime(input.getIntOr("laserFireStartTime", 0));
        this.setSecondPhase(input.getBooleanOr("secondPhase", false));
        this.setCollided(input.getBooleanOr("collided", false));
        this.setOrbitOffsetDegrees(input.getIntOr("orbitOffsetDegrees", 0));
        this.setDifficultyBoost(input.getFloatOr("difficultyBoost", 0));

        this.currentStateTimeTick = input.getIntOr("currentStateTimeTick", 0);
        this.setSyncedCurrentStateTimeTick(this.currentStateTimeTick);

        this.animationTimeTick = input.getIntOr("animationTimeTick", 0);
        this.prevAnimationTick = input.getIntOr("prevAnimationTick", 0);

        this.targetEntityUUID = input.read("targetEntityUUID", UUIDUtil.CODEC).orElse(null);
        this.prevLookAngle = input.read("prevLookAngle", Vec3.CODEC).orElse(Vec3.ZERO);
        this.noAI = input.getBooleanOr("NoAI", false);
    }

    public static boolean isOrFromHorizontalState(CosmicCrystalState cosmicCrystalState) {
        return cosmicCrystalState == CosmicCrystalState.HORIZONTAL_LASER ||
                cosmicCrystalState == CosmicCrystalState.TRACKING_LASER ||
                cosmicCrystalState == CosmicCrystalState.SWEEP_LASER ||
                cosmicCrystalState == CosmicCrystalState.TRACKING_SPINNING_ATTACK;
    }

    public static boolean isLaserState(CosmicCrystalState cosmicCrystalState) {
        return cosmicCrystalState == CosmicCrystalState.VERTICAL_LASER ||
                cosmicCrystalState == CosmicCrystalState.HORIZONTAL_LASER ||
                cosmicCrystalState == CosmicCrystalState.TRACKING_LASER ||
                cosmicCrystalState == CosmicCrystalState.SWEEP_LASER;
    }

    public static boolean isTrackingState(CosmicCrystalState cosmicCrystalState) {
        return cosmicCrystalState == CosmicCrystalState.TRACKING_LASER ||
                cosmicCrystalState == CosmicCrystalState.TRACKING_SMASHING_ATTACK ||
                cosmicCrystalState == CosmicCrystalState.TRACKING_SPINNING_ATTACK ||
                cosmicCrystalState == CosmicCrystalState.SWEEP_LASER ||
                cosmicCrystalState == CosmicCrystalState.HORIZONTAL_LASER;
    }

    public static boolean isOrbitState(CosmicCrystalState cosmicCrystalState) {
        return cosmicCrystalState == CosmicCrystalState.NORMAL ||
                cosmicCrystalState == CosmicCrystalState.HORIZONTAL_LASER ||
                cosmicCrystalState == CosmicCrystalState.VERTICAL_LASER ||
                cosmicCrystalState == CosmicCrystalState.TRACKING_LASER ||
                cosmicCrystalState == CosmicCrystalState.SWEEP_LASER;
    }

    public boolean isLaserFiring() {
        boolean isLaserState = CosmicCrystalEntity.isLaserState(this.getCosmicCrystalState());
        return isLaserState && this.currentStateTimeTick > this.getLaserFireStartTime();
    }

    public void tick() {
        if(this.tickCount == 1) {
            if (this.level().isClientSide()) {
                spawnLargeParticleCloud(3);
            }

            this.level().playSound(
                    this,
                    this.blockPosition(),
                    BzSounds.COSMIC_CRYSTAL_ENTITY_SPAWN_EXPLOSION.get(),
                    SoundSource.HOSTILE,
                    1,
                    1f);
        }

        if (this.isDeadOrDying() && this.level().shouldTickDeath(this)) {
            this.tickDeath();
        }

        if (this.noAI) {
            return;
        }

        if (!this.level().isClientSide() && !checkIfStillInEvent()) {
            return;
        }

        if (this.getCosmicCrystalState() == CosmicCrystalState.NORMAL) {
            this.setTargetEntityUUID(null);
            this.targetEntity = null;

            if (this.getEssenceController() == null && this.currentStateTimeTick > 50) {
                CosmicCrystalState chosenAttack;
                do {
                    chosenAttack = CosmicCrystalState.values()[this.getRandom().nextInt(CosmicCrystalState.values().length)];
                }
                while (this.pastStates.contains(chosenAttack) || chosenAttack == CosmicCrystalState.NORMAL);
                this.setCosmicCrystalState(chosenAttack);
            }
        }
        else if (!this.level().isClientSide() && this.tickCount % 5 == 0) {
            this.setSyncedCurrentStateTimeTick(this.currentStateTimeTick);
        }

        this.prevLookAngle = this.getLookAngle();

        orbitMovement();
        spinningTrackingBehaviour();

        super.tick();

        incrementAnimationAndRotationTicks();

        spawnFancyParticlesOnClient();
        laserBreakBlocks();
        smashingBehaviour();
        destroyTouchingBlocks();

        this.currentStateTimeTick++;
        this.currentTickCount++;
    }

    private boolean checkIfStillInEvent() {
        BlockPos essenceBlockPos = this.getEssenceControllerBlockPos();

        if (essenceBlockPos == null) {
            return false;
        }

        if (this.tickCount % 20 != 0 && this.blockPosition().distManhattan(essenceBlockPos) < 16) {
            return true;
        }

        UUID essenceUuid = this.getEssenceController();
        ResourceKey<Level> essenceDimension = this.getEssenceControllerDimension();

        if (essenceUuid == null || essenceDimension == null) {
            return false;
        }

        BlockPos blockPos = this.blockPosition();
        EssenceBlockEntity essenceBlockEntity = EssenceBlockEntity.getEssenceBlockAtLocation(this.level(), essenceDimension, essenceBlockPos, essenceUuid);

        if (essenceBlockEntity != null) {
            BlockPos arenaSize = essenceBlockEntity.getArenaSize();
            if (Math.abs(blockPos.getX() - essenceBlockPos.getX()) > (arenaSize.getX() / 2) ||
                Math.abs(blockPos.getY() - essenceBlockPos.getY()) > (arenaSize.getY() / 2) ||
                Math.abs(blockPos.getZ() - essenceBlockPos.getZ()) > (arenaSize.getZ() / 2))
            {
                //Failed check. Kill mob.
                this.remove(RemovalReason.DISCARDED);
                return false;
            }
        }
        else {
            //Failed check. Kill mob.
            this.remove(RemovalReason.DISCARDED);
            return false;
        }
        return true;
    }

    private void orbitMovement() {
        if (this.getEssenceControllerBlockPos() != null) {
            Vec3 orbitPosition;
            if (isOrbitState(this.getCosmicCrystalState())) {
                orbitPosition = this.getEssenceControllerBlockPos().getCenter().add(0, -1, 0);

                if (this.getCosmicCrystalState() == CosmicCrystalState.HORIZONTAL_LASER && this.targetEntity != null) {
                    Vec3 targetPos = this.targetEntity.position().add(0, this.targetEntity.getBbHeight() / 2, 0);
                    Vec3 diffFromNow = this.prevTargetPosition.subtract(targetPos).scale(0.97);
                    this.prevTargetPosition = diffFromNow.add(targetPos);

                    orbitPosition = new Vec3(orbitPosition.x(), this.prevTargetPosition.y() - 1, orbitPosition.z());
                }
            }
            else {
                return;
            }

            float orbitOffsetDegrees = this.getOrbitOffsetDegrees() * Mth.DEG_TO_RAD;
            float difficultyBoost = this.getDifficultyBoost();
            float radius = 4;
            if (this.getCosmicCrystalState() == CosmicCrystalState.VERTICAL_LASER) {
                radius = (Math.max(-1, Math.min(1, Mth.sin(this.currentTickCount * 3 * Mth.DEG_TO_RAD) * 1.25f)) + 1f) * 13;
            }

            float spinRadians = (float) (((this.currentTickCount * Math.pow(difficultyBoost, 5)) % 360) * Mth.DEG_TO_RAD);
            Vector3f rotationOffset = POSITIVE_X_VECT.toVector3f().rotateY(orbitOffsetDegrees + spinRadians);
            Vec3 targetOrbitSpot = orbitPosition.add(new Vec3(rotationOffset).scale(radius));
            Vec3 diffToTargetOrbitSpot = targetOrbitSpot.subtract(this.position());
            double xzScale = Math.abs(diffToTargetOrbitSpot.horizontalDistance()) * 0.05d * difficultyBoost;
            double yScale = Math.abs(diffToTargetOrbitSpot.y()) * 0.1d * difficultyBoost;

            this.setDeltaMovement(diffToTargetOrbitSpot.normalize().multiply(xzScale, yScale, xzScale));
        }
    }

    private void spawnFancyParticlesOnClient() {
        if (this.level().isClientSide()) {
            if (this.tickCount % 5 == 0 || this.hurtTime > 0) {
                Vec3 center = this.getBoundingBox().getCenter();
                spawnFancyParticle(center);
                if (this.hurtTime == 8) {
                    for (int i = 0; i < 50; i++) {
                        spawnFancyParticle(center);
                    }
                }
            }

            if (this.isLaserFiring()) {
                spawnFancyParticle(this.getEyePosition().add(this.getLookAngle().scale(1.2f)));
            }
        }
    }

    private void incrementAnimationAndRotationTicks() {
        this.prevAnimationTick = this.animationTimeTick;

        if (this.animationTimeTick < this.getInitialRotationAnimationTimespan()) {
            this.animationTimeTick++;
        }
        else if(this.getCosmicCrystalState() != CosmicCrystalState.NORMAL && this.currentStateTimeTick >= this.getStateTimespan()) {
            this.setCosmicCrystalState(CosmicCrystalState.NORMAL);
        }

        this.refreshDimensions();
        this.setupTargetForTrackingStates();

        float progress;
        if (this.getInitialRotationAnimationTimespan() == 0) {
            progress = 1;
        }
        else {
            progress = (float)this.animationTimeTick / this.getInitialRotationAnimationTimespan();
        }

        setActualRotations((float) Math.pow(progress, 4));
    }

    private void setActualRotations(float progress) {
        Vec3 currentLookAngle = this.getLookAngle();
        Vec3 desiredLookPosition = this.getEyePosition().add(0, -1, 0);
        Vec3 currentLookPosition = this.getEyePosition().add(currentLookAngle);

        if (this.getCosmicCrystalState() == CosmicCrystalState.HORIZONTAL_LASER) {
            if (this.getEssenceControllerBlockPos() != null) {
                Vec3 anchorPoint = new Vec3(
                        this.getEssenceControllerBlockPos().getX() + 0.5,
                        this.getEyeY(),
                        this.getEssenceControllerBlockPos().getZ() + 0.5);

                Vec3 diff = this.getEyePosition().subtract(anchorPoint);

                desiredLookPosition = this.getEyePosition().add(diff);
            }
            else {
                desiredLookPosition = this.getEyePosition().add(this.calculateViewVector(0, this.getYRot()));
            }
        }
        else if (this.getCosmicCrystalState() == CosmicCrystalState.TRACKING_LASER) {
            if (this.targetEntity != null) {
                Vec3 targetPos = this.targetEntity.position().add(0, 0.25, 0);
                double difficultyMultiplier = ((this.getDifficultyBoost() - 1) / 3);
                double targetStrength = 0.94 - difficultyMultiplier;
                Vec3 diffFromNow = this.prevTargetPosition.subtract(targetPos).scale(targetStrength);
                this.prevTargetPosition = diffFromNow.add(targetPos);

                if (diffFromNow.length() > 3d) {
                    diffFromNow.scale(3d / diffFromNow.length());
                }

                desiredLookPosition = diffFromNow.add(targetPos);
            }
        }
        else if (this.getCosmicCrystalState() == CosmicCrystalState.SWEEP_LASER) {
            Vec3 targetPos = null;
            Vec3 directionToTarget = null;

            if (this.getEssenceControllerBlockPos() != null) {
                targetPos = this.getEssenceControllerBlockPos().getCenter();
                directionToTarget = this.position().subtract(targetPos).normalize();
            }
            else if (this.targetEntity != null) {
                if (!this.getSecondPhase()) {
                    this.prevTargetPosition = this.targetEntity.position().add(0, 0.25, 0);
                    this.setSecondPhase(true);
                }

                targetPos = this.prevTargetPosition;
                directionToTarget = this.position().subtract(targetPos).normalize();
            }

            if (directionToTarget != null && targetPos != null) {
                Vec3 crossProduct = new Vec3(
                        UP_VECT.y() * directionToTarget.z() - UP_VECT.z() * directionToTarget.y(),
                        UP_VECT.z() * directionToTarget.x() - UP_VECT.x() * directionToTarget.z(),
                        UP_VECT.x() * directionToTarget.y() - UP_VECT.y() * directionToTarget.x()
                ).normalize();

                if (crossProduct.length() != 0) {
                    double difficultyMultiplier = ((this.getDifficultyBoost() - 1) * 1.5) + 1;
                    float spinRadians = (float) (this.currentStateTimeTick * 3 * difficultyMultiplier * Mth.DEG_TO_RAD);

                    Vector3f vectorToRotate = directionToTarget.toVector3f();
                    Vector3f axisToRotateAround = crossProduct.toVector3f();

                    Vector3f finalLookSpot = vectorToRotate.rotateAxis(
                            spinRadians,
                            axisToRotateAround.x(),
                            axisToRotateAround.y(),
                            axisToRotateAround.z());

                    desiredLookPosition = new Vec3(finalLookSpot).add(this.getEyePosition());
                }
            }
        }
        else if (this.getCosmicCrystalState() == CosmicCrystalState.TRACKING_SMASHING_ATTACK) {
            int moveTime = this.getInitialRotationAnimationTimespan();
            if ((this.currentStateTimeTick <= moveTime || this.getSecondPhase()) && this.targetEntity != null) {
                Vec3 targetPos = this.targetEntity.position().add(0, 0.25, 0);
                Vec3 diffFromNow = this.prevTargetPosition.subtract(targetPos).scale(0.9);
                this.prevTargetPosition = diffFromNow.add(targetPos);
                desiredLookPosition = diffFromNow.add(targetPos);
            }
            else {
                desiredLookPosition = this.getEyePosition().add(0, -1, 0);
            }
        }
        else if (this.getCosmicCrystalState() == CosmicCrystalState.TRACKING_SPINNING_ATTACK) {
            float windDownDuration = 40f;
            int windDownPhase = (int) (this.getStateTimespan() - windDownDuration);

            Vec3 targetPos;
            if (this.currentStateTimeTick > windDownPhase) {
                double antiProgress = Math.min((this.currentStateTimeTick - windDownPhase) / windDownDuration, 1);
                float spinStrength = (float) (1 - antiProgress);
                targetPos = new Vec3(
                        Mth.lerp(antiProgress, Mth.sin(this.currentStateTimeTick * 40 * Mth.DEG_TO_RAD) * spinStrength, 0),
                        Mth.lerp(antiProgress, 1, 0),
                        Mth.lerp(antiProgress, Mth.cos(this.currentStateTimeTick * 40 * Mth.DEG_TO_RAD) * spinStrength, 0));
            }
            else {
                targetPos = new Vec3(
                        Mth.sin(this.currentStateTimeTick * 40 * Mth.DEG_TO_RAD),
                        Mth.lerp(progress, 0, 1),
                        Mth.cos(this.currentStateTimeTick * 40 * Mth.DEG_TO_RAD));
            }

            desiredLookPosition = this.position().add(targetPos);
        }

        if (!currentLookPosition.equals(desiredLookPosition)) {
            Vec3 lerpedDesiredLook = new Vec3(
                    Mth.lerp(progress, currentLookPosition.x(), desiredLookPosition.x()),
                    Mth.lerp(progress, currentLookPosition.y(), desiredLookPosition.y()),
                    Mth.lerp(progress, currentLookPosition.z(), desiredLookPosition.z())
            );
            this.lookAtCurrent(lerpedDesiredLook);
        }
    }

    private void lookAtCurrent(Vec3 targetPos) {
        Vec3 crystalPos = EntityAnchorArgument.Anchor.EYES.apply(this);
        double xDiff = targetPos.x - crystalPos.x;
        double yDiff = targetPos.y - crystalPos.y;
        double zDiff = targetPos.z - crystalPos.z;
        double diffMagn = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        float xRotD = (float)(-(Mth.atan2(yDiff, diffMagn) * 180.0F / (float)Math.PI));
        float yRotD = (float)(Mth.atan2(zDiff, xDiff) * 180.0F / (float)Math.PI) - 90.0F;
        this.setXRot(Mth.wrapDegrees(xRotD));
        this.setYRot(Mth.wrapDegrees(yRotD));
        this.setYHeadRot(this.getYRot());
        this.setYBodyRot(this.getYRot());
    }

    private void setupTargetForTrackingStates() {
        if (isTrackingState(this.getCosmicCrystalState())) {
            if (this.getTargetEntityUUID() == null || this.targetEntity == null || !this.targetEntity.getUUID().equals(this.getTargetEntityUUID())) {
                if (this.getTargetEntityUUID() != null) {
                    this.targetEntity = this.level().getPlayerByUUID(this.getTargetEntityUUID());
                }
                else {
                    this.targetEntity = this.level().getNearestPlayer(this, MAX_RANGE);
                    if (this.targetEntity != null) {
                        this.setTargetEntityUUID(this.targetEntity.getUUID());

                        if (this.getCosmicCrystalState() == CosmicCrystalState.HORIZONTAL_LASER) {
                            this.prevTargetPosition = this.position();
                        }
                        else if (this.getCosmicCrystalState() != CosmicCrystalState.SWEEP_LASER) {
                            this.prevTargetPosition = this.targetEntity.position();
                        }
                    }
                    else {
                        if (this.level() instanceof ServerLevel serverLevel) {
                            this.targetEntity = serverLevel.getNearestEntity(LivingEntity.class, TARGETING_CONDITIONS, this, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(MAX_RANGE));
                            if (this.targetEntity != null) {
                                this.setTargetEntityUUID(this.targetEntity.getUUID());

                                if (this.getCosmicCrystalState() == CosmicCrystalState.HORIZONTAL_LASER) {
                                    this.prevTargetPosition = this.position();
                                }
                                else if (this.getCosmicCrystalState() != CosmicCrystalState.SWEEP_LASER) {
                                    this.prevTargetPosition = this.targetEntity.position();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void laserBreakBlocks() {
        if (this.level() instanceof ServerLevel serverLevel && this.isLaserFiring()) {
            HitResult hitResult = ProjectileUtil.getHitResultOnViewVector(this, (_) -> true, 50);

            if (hitResult instanceof BlockHitResult blockHitResult) {
               BlockState state = serverLevel.getBlockState(blockHitResult.getBlockPos());
               if (state.getBlock().getExplosionResistance() < 1500 && !state.is(BlockTags.WITHER_IMMUNE) && this.getRemovalReason() == null) {
                   serverLevel.destroyBlock(blockHitResult.getBlockPos(), true);
               }
            }
            else if (hitResult instanceof EntityHitResult entityHitResult) {
                Entity entity = entityHitResult.getEntity();
                if (entity instanceof ItemEntity itemEntity) {
                    itemEntity.hurt(serverLevel.damageSources().source(BzDamageSources.COSMIC_CRYSTAL_TYPE, this, this), 10);
                }
                else if (entity instanceof Projectile projectile) {
                    projectile.remove(RemovalReason.KILLED);
                }
                else if (entity instanceof LivingEntity livingEntity && !(entity instanceof CosmicCrystalEntity)) {
                    if (laserHurtAttack(livingEntity)) return;
                }
            }

            if (serverLevel.getGameTime() % 10 == 0) {
                serverLevel.playSound(
                        this,
                        this.blockPosition(),
                        BzSounds.COSMIC_CRYSTAL_ENTITY_LASER.get(),
                        SoundSource.HOSTILE,
                        1.2f,
                        1);

                serverLevel.playSound(
                        this,
                        BlockPos.containing(hitResult.getLocation()),
                        BzSounds.COSMIC_CRYSTAL_ENTITY_LASER.get(),
                        SoundSource.HOSTILE,
                        1.2f,
                        1);
            }
        }
        else if (!this.laserChargeSoundPlayed &&
                CosmicCrystalEntity.isLaserState(this.getCosmicCrystalState()) &&
                this.currentStateTimeTick > this.getLaserStartDelay() - 10)
        {
            if (this.level().getGameTime() % 10 == 0) {
                this.level().playSound(
                        this,
                        this.blockPosition(),
                        BzSounds.COSMIC_CRYSTAL_ENTITY_LASER_CHARGE.get(),
                        SoundSource.HOSTILE,
                        1.2f,
                        1);
            }

            this.laserChargeSoundPlayed = true;
        }
    }

    private void smashingBehaviour() {
        if (this.getCosmicCrystalState() == CosmicCrystalState.TRACKING_SMASHING_ATTACK) {
            int moveTime = this.getInitialRotationAnimationTimespan();
            if (this.currentStateTimeTick >= this.getStateTimespan() - 1) {
                this.setCosmicCrystalState(CosmicCrystalState.NORMAL);
                this.setDeltaMovement(0, 0, 0);
                this.setCollided(false);
            }
            else if (this.currentStateTimeTick == moveTime) {
                this.setSecondPhase(true);
            }
            else if (this.currentStateTimeTick < moveTime) {
                this.addDeltaMovement(new Vec3(0, (1 - ((float)this.currentStateTimeTick / moveTime)) * 0.005d, 0));
            }
            else if (!this.getSecondPhase() &&
                this.currentStateTimeTick > moveTime &&
                this.currentStateTimeTick < this.getStateTimespan())
            {
                this.setCosmicCrystalState(CosmicCrystalState.NORMAL);
                this.setDeltaMovement(0, 0, 0);
                this.setCollided(false);
            }
            else if (this.targetEntity != null) {
                double crashSpeed = 0.07d * this.getDifficultyBoost();
                this.addDeltaMovement(this.getLookAngle().scale(crashSpeed));

                if (this.getCollided() || this.horizontalCollision || this.verticalCollision || this.onGround()) {
                    collidingAttackExplosion();
                }
            }
            else {
                this.setSecondPhase(false);
            }
        }
    }

    private void collidingAttackExplosion() {
        if (!this.level().isClientSide()) {

            this.level().explode(
                    this,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    3,
                    Level.ExplosionInteraction.MOB);

            this.level().playSound(
                    this,
                    this.blockPosition(),
                    BzSounds.COSMIC_CRYSTAL_ENTITY_CRASHES.get(),
                    SoundSource.HOSTILE,
                    1,
                    1f);

            for (BlockPos pos : BlockPos.betweenClosed(
                    this.blockPosition().offset(-2, -2, -2),
                    this.blockPosition().offset(2, 2, 2)))
            {
                this.level().levelEvent(2001, pos, Block.getId(this.level().getBlockState(pos)));
            }

            this.level().getEntities(this, this.getBoundingBox().inflate(16)).forEach(e -> {
                if (e instanceof Player player) {
                    player.indicateDamage(0, 0);
                }
            });

            this.setCollided(true);
        }
        else {
            spawnLargeParticleCloud(5);
        }

        this.setDeltaMovement(0, 0, 0);
        this.setSecondPhase(false);
    }

    private void spinningTrackingBehaviour() {
        if (this.getCosmicCrystalState() == CosmicCrystalState.TRACKING_SPINNING_ATTACK) {
            int moveTime = this.getInitialRotationAnimationTimespan();
            if (this.currentStateTimeTick >= this.getStateTimespan() - 1) {
                this.setDeltaMovement(0, 0, 0);
            }
            else if (this.targetEntity != null) {

                double progress;
                if (this.getInitialRotationAnimationTimespan() == 0) {
                    progress = 1;
                }
                else {
                    progress = (float)this.animationTimeTick / this.getInitialRotationAnimationTimespan();
                }
                progress = Math.pow(progress, 4);

                double speedAdj = Math.min(1, (float)this.currentStateTimeTick / moveTime) * progress;

                Vec3 targetPos = this.targetEntity.position().add(0, this.targetEntity.getBbHeight() / 4, 0);
                Vec3 diffFromNow = targetPos.subtract(this.position());
                Vec3 diffFromPast = this.prevTargetPosition.subtract(this.position());

                float turnStrength = 0.0375f + ((this.getDifficultyBoost() - 1) / 30);
                Vec3 lerpedLookVector = new Vec3(
                        Mth.lerp(turnStrength, diffFromPast.x(), diffFromNow.x()),
                        Mth.lerp(turnStrength, diffFromPast.y(), diffFromNow.y()),
                        Mth.lerp(turnStrength, diffFromPast.z(), diffFromNow.z())
                );
                this.prevTargetPosition = lerpedLookVector.add(targetPos);

                int hitDiff = this.currentStateTimeTick - this.lastPhysicalHit;
                double cappedSpeed = Mth.lerp(Math.min(hitDiff / 40f, 1), 0, 0.65) * speedAdj;

                this.setDeltaMovement(lerpedLookVector.normalize().scale(cappedSpeed));

                // collision checks

                float inflatedSpeed = 2f;
                Vec3 originalVect = this.getDeltaMovement();
                Vec3 inflatedVect = originalVect.scale(inflatedSpeed);
                Vec3 collideVect = ((EntityAccessor)this).bumblezone$callCollide(inflatedVect);

                int xDirection = getDirection(inflatedVect.x(), collideVect.x());
                int yDirection = getDirection(inflatedVect.y(), collideVect.y());
                int zDirection = getDirection(inflatedVect.z(), collideVect.z());

                if (xDirection == -1 || yDirection == -1 || zDirection == -1) {
                    this.setDeltaMovement(
                        originalVect.x() * xDirection,
                        originalVect.y() * yDirection,
                        originalVect.z() * zDirection
                    );
                }

                if (this.tickCount % 10 == 0){
                    this.level().playSound(
                            this,
                            this.blockPosition(),
                            BzSounds.COSMIC_CRYSTAL_ENTITY_SPIN.get(),
                            SoundSource.HOSTILE,
                            (float)progress,
                            1f);
                }
            }
        }
    }

    private static int getDirection(double originalCoordinate, double collideCoordinate) {
        if (originalCoordinate > 0) {
            if (originalCoordinate > collideCoordinate) {
                return -1;
            }
        }
        else if (originalCoordinate < 0) {
            if (originalCoordinate < collideCoordinate) {
                return -1;
            }
        }
        return 1;
    }

    @Override
    public void baseTick() {

        if (this.isPassenger()) {
            this.stopRiding();
        }

        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();

        if (this.level().isClientSide()) {
            this.clearFire();
        }
        else if (this.getRemainingFireTicks() > 0) {
            if (this.getRemainingFireTicks() > 25) {
                this.setRemainingFireTicks(25);
            }

            if (this.getRemainingFireTicks() == 1 && !this.isInLava()) {
                this.hurt(this.damageSources().onFire(), 1.0f);
            }
            this.setRemainingFireTicks(this.getRemainingFireTicks() - 1);

            if (this.getTicksFrozen() > 0) {
                this.setTicksFrozen(0);
                this.level().levelEvent(null, 1009, this.blockPosition(), 1);
            }
        }
        else {
            this.setRemainingFireTicks(-1);
        }

        if (!this.level().isClientSide() && this.getTicksFrozen() > 0) {
            if (this.wasInPowderSnow && !this.isInPowderSnow && this.canFreeze()) {
                this.setTicksFrozen(39);
            }
            else if (this.isInPowderSnow && this.canFreeze()) {
                if (this.getTicksFrozen() > 39 && this.getTicksFrozen() < 70) {
                    if (this.getTicksFrozen() == 69) {
                        this.hurt(this.damageSources().freeze(), 1.0f);
                    }
                }
                else {
                    this.setTicksFrozen(40);
                }
            }

            if (this.getTicksFrozen() == 1 || this.getTicksFrozen() == 2) {
                this.hurt(this.damageSources().freeze(), 1.0f);
            }
        }

        this.wasInPowderSnow = this.isInPowderSnow;
        this.isInPowderSnow = false;

        if (this.hurtTime > 0) {
            --this.hurtTime;
        }
        if (this.invulnerableTime > 0) {
            --this.invulnerableTime;
        }
        if (this.lastHurtByPlayerMemoryTime > 0) {
            --this.lastHurtByPlayerMemoryTime;
        }
        else {
            this.lastHurtByPlayer = null;
        }
        if (this.getLastHurtMob() != null && !this.getLastHurtMob().isAlive()) {
            this.setLastHurtByMob(null);
        }
        if (this.getLastHurtByMob() != null) {
            if (!this.getLastHurtByMob().isAlive()) {
                this.setLastHurtByMob(null);
            }
            else if (this.tickCount - this.getLastHurtByMobTimestamp() > 100) {
                this.setLastHurtByMob(null);
            }
        }
        this.yBodyRotO = this.yBodyRot;
        this.yHeadRotO = this.yHeadRot;
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();

        for (MobEffectInstance mobEffectInstance : new ArrayList<>(this.getActiveEffects())) {
            Holder<MobEffect> mobEffect = mobEffectInstance.getEffect();
            int currentEffectTick = mobEffectInstance.isInfiniteDuration() ? Integer.MAX_VALUE : mobEffectInstance.getDuration();

            if (mobEffect == MobEffects.POISON) {
                if (currentEffectTick > 45) {
                    this.forceAddEffect(new MobEffectInstance(
                            mobEffect,
                            45,
                            mobEffectInstance.getAmplifier(),
                            mobEffectInstance.isAmbient(),
                            mobEffectInstance.isVisible(),
                            mobEffectInstance.showIcon()
                        ),
                        this);
                }
            }
            else if (mobEffect == MobEffects.WITHER) {
                if (currentEffectTick > 80) {
                    this.forceAddEffect(new MobEffectInstance(
                            mobEffect,
                            80,
                            mobEffectInstance.getAmplifier(),
                            mobEffectInstance.isAmbient(),
                            mobEffectInstance.isVisible(),
                            mobEffectInstance.showIcon()
                        ),
                        this);
                }
            }
            else if (currentEffectTick > 30 && !mobEffectInstance.getEffect().value().isBeneficial()) {
                this.forceAddEffect(new MobEffectInstance(
                        mobEffect,
                        30,
                        mobEffectInstance.getAmplifier(),
                        mobEffectInstance.isAmbient(),
                        mobEffectInstance.isVisible(),
                        mobEffectInstance.showIcon()
                    ),
                    this);
            }
        }

        this.tickEffects();

        if (!this.level().isClientSide()) {
            this.setSharedFlagOnFire(this.getRemainingFireTicks() > 0);
        }

        this.firstTick = false;
    }

    @Override
    public void travel(Vec3 vec3) {
        if (this.isEffectiveAi()) {
            double d = 0.08;
            boolean bl = this.getDeltaMovement().y <= 0.0;
            if (bl && this.hasEffect(MobEffects.SLOW_FALLING)) {
                d = 0.01;
            }
            BlockPos blockPos = this.getBlockPosBelowThatAffectsMyMovement();
            float p = this.level().getBlockState(blockPos).getBlock().getFriction();
            float f = this.getCosmicCrystalState() == CosmicCrystalState.TRACKING_SPINNING_ATTACK ? 0.93f : 0.91f;
            Vec3 vec37 = this.handleRelativeFrictionAndCalculateMovement(vec3, p);

            double q = vec37.y;
            if (!this.level().isClientSide() || this.level().hasChunkAt(blockPos)) {
                if (!this.isNoGravity()) {
                    q -= d;
                }
            }
            else {
                q = this.getY() > (double)this.level().getMinY() ? -0.1 : 0.0;
            }

            this.setDeltaMovement(vec37.x * (double)f, q * (double)0.98f, vec37.z * (double)f);
        }

        this.calculateEntityAnimation(true);
    }

    @Override
    protected AABB makeBoundingBox() {
        EntityDimensions entityDimensions = ((EntityAccessor)this).bumblezone$getDimensions();
        float radius = entityDimensions.width() / 2.0F;
        float heightRadius = entityDimensions.height() / 2;
        float yOffset = 1f;

        float progress = 1 - Mth.abs(((90 - this.getXRot()) / 90) - 1);

        if (isOrFromHorizontalState(this.getCosmicCrystalState()) ||
            (this.pastStates != null && !this.pastStates.isEmpty() && isOrFromHorizontalState(this.pastStates.getFirst())))
        {
            float yRotRadian = this.getYRot() * Mth.DEG_TO_RAD;
            double yRotSin = Math.abs(Mth.sin(yRotRadian));
            double yRotCos = Math.abs(Mth.cos(yRotRadian));
            double xRadius = radius + (yRotSin * 0.5f * progress);
            double zRadius = radius + (yRotCos * 0.5f * progress);

            heightRadius = heightRadius - (0.5f * progress);

            double xMin = this.getX() - xRadius;
            double yMin = this.getY() + yOffset - heightRadius;
            double zMin = this.getZ() - zRadius;
            double xMax = this.getX() + xRadius;
            double yMax = this.getY() + yOffset + heightRadius;
            double zMax = this.getZ() + zRadius;

            return new AABB(xMin, yMin, zMin, xMax, yMax, zMax);
        }
        else {
            Vec3 vec3 = new Vec3(this.getX() - (double)radius, this.getY() + yOffset - heightRadius, this.getZ() - (double)radius);
            Vec3 vec32 = new Vec3(this.getX() + (double)radius, this.getY() + yOffset + heightRadius, this.getZ() + (double)radius);
            return new AABB(vec3, vec32);
        }
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        EntityDimensions dimensions = this.getType().getDimensions().scale(this.getAgeScale());
        float eyeHeight = dimensions.height() / 2;
        if (isOrFromHorizontalState(this.getCosmicCrystalState()) ||
            (this.pastStates != null && !this.pastStates.isEmpty() && isOrFromHorizontalState(this.pastStates.getFirst())))
        {
            float progress = 1 + (((90 - this.getXRot()) / 90) - 1);
            eyeHeight *= (progress);
        }
        return dimensions.withEyeHeight(eyeHeight);
    }

    @Override
    protected int checkInsideBlocks(
            Vec3 from,
            Vec3 to,
            InsideBlockEffectApplier.StepBasedCollector effectCollector,
            LongSet visitedBlocks,
            int maxMovementIterations
    ) {

        AABB deflatedBoundingBoxAtTarget = this.makeBoundingBox(to).deflate(1.0E-5F);
        boolean movedFar = from.distanceToSqr(to) > Mth.square(0.9999900000002526);
        AtomicInteger iterations = new AtomicInteger();
        BlockGetter.forEachBlockIntersectedBetween(
                from,
                to,
                deflatedBoundingBoxAtTarget,
                (blockIntersection, iteration) -> {
                    if (!this.isAlive()) {
                        return false;
                    } else if (iteration >= maxMovementIterations) {
                        return false;
                    } else {
                        iterations.set(iteration);
                        BlockState state = this.level().getBlockState(blockIntersection);

                        if (state.isAir()) {
                            return true;
                        }
                        else if (!this.level().isClientSide() &&
                                !state.getCollisionShape(this.level(), blockIntersection).isEmpty() &&
                                state.getBlock().getExplosionResistance() < 1500 &&
                                !state.is(BlockTags.WITHER_IMMUNE))
                        {
                            if (this.getRemovalReason() == null) {
                                this.level().destroyBlock(blockIntersection, true);
                            }
                            return true;
                        }
                        else {
                            VoxelShape intersectShape = state.getEntityInsideCollisionShape(this.level(), blockIntersection, this);
                            boolean insideBlock = intersectShape == Shapes.block()
                                    || this.collidedWithShapeMovingFrom(from, to, intersectShape.move(new Vec3(blockIntersection)).toAabbs());
                            boolean insideFluid = this.collidedWithFluid(state.getFluidState(), blockIntersection, from, to);
                            if ((insideBlock || insideFluid) && visitedBlocks.add(blockIntersection.asLong())) {
                                if (insideBlock) {
                                    try {
                                        boolean isPrecise = movedFar || deflatedBoundingBoxAtTarget.intersects(blockIntersection);
                                        effectCollector.advanceStep(iteration);
                                        state.entityInside(this.level(), blockIntersection, this, effectCollector, isPrecise);
                                        this.onInsideBlock(state);
                                    } catch (Throwable var20) {
                                        CrashReport report = CrashReport.forThrowable(var20, "Colliding entity with block");
                                        CrashReportCategory category = report.addCategory("Block being collided with");
                                        CrashReportCategory.populateBlockDetails(category, this.level(), blockIntersection, state);
                                        CrashReportCategory entityCategory = report.addCategory("Entity being checked for collision");
                                        this.fillCrashReportCategory(entityCategory);
                                        throw new ReportedException(report);
                                    }
                                }

                                if (insideFluid) {
                                    effectCollector.advanceStep(iteration);
                                    state.getFluidState().entityInside(this.level(), blockIntersection, this, effectCollector);
                                }

                                return true;
                            } else {
                                return true;
                            }
                        }
                    }
                }

        );
        return iterations.get() + 1;
    }

    private void destroyTouchingBlocks() {
        if (this.level().isClientSide()) {
            return;
        }

        AABB aABB = this.getBoundingBox();
        BlockPos blockPos = BlockPos.containing(aABB.minX - 1.0E-7, aABB.minY - 1.0E-7, aABB.minZ - 1.0E-7);
        BlockPos blockPos2 = BlockPos.containing(aABB.maxX + 1.0E-7, aABB.maxY + 1.0E-7, aABB.maxZ + 1.0E-7);
        if (this.level().hasChunksAt(blockPos, blockPos2)) {
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            for (int i = blockPos.getX(); i <= blockPos2.getX(); ++i) {
                for (int j = blockPos.getY(); j <= blockPos2.getY(); ++j) {
                    for (int k = blockPos.getZ(); k <= blockPos2.getZ(); ++k) {
                        mutableBlockPos.set(i, j, k);
                        BlockState blockState = this.level().getBlockState(mutableBlockPos);
                        if (!blockState.isAir() &&
                            !blockState.getCollisionShape(this.level(), mutableBlockPos).isEmpty() &&
                            blockState.getBlock().getExplosionResistance() < 1500 &&
                            !blockState.is(BlockTags.WITHER_IMMUNE))
                        {
                            if (this.getRemovalReason() == null) {
                                this.level().destroyBlock(mutableBlockPos, true);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void pushEntities() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            this.level().getEntities(EntityTypeTest.forClass(Player.class), this.getBoundingBox(), EntitySelector.pushableBy(this)).forEach(this::doPush);
            return;
        }
        List<Entity> list = serverLevel.getEntities(this, this.getBoundingBox(), EntitySelector.pushableBy(this));
        if (!list.isEmpty()) {
            int entityIndex;
            int maxCrammingLimit = serverLevel.getGameRules().get(GameRules.MAX_ENTITY_CRAMMING);
            if (maxCrammingLimit > 0 && list.size() > maxCrammingLimit - 1 && this.random.nextInt(4) == 0) {
                entityIndex = 0;
                for (Entity entity : list) {
                    if (entity.isPassenger()) continue;
                    ++entityIndex;
                }
            }

            for (entityIndex = 0; entityIndex < list.size(); ++entityIndex) {
                Entity entity = list.get(entityIndex);
                this.doPush(entity);

                if (entity instanceof LivingEntity livingEntity && !(entity instanceof CosmicCrystalEntity)) {
                    if (physicalHurtAttack(livingEntity)) continue;

                    Vec3 center = livingEntity.getBoundingBox().getCenter();
                    serverLevel.sendParticles(
                            ParticleTypes.END_ROD,
                            center.x() + this.random.nextGaussian() / 5,
                            center.y() + this.random.nextGaussian() / 2.5,
                            center.z() + this.random.nextGaussian() / 5,
                            15,
                            (this.random.nextFloat() * this.random.nextGaussian() / 15),
                            (this.random.nextFloat() * this.random.nextGaussian() / 15),
                            (this.random.nextFloat() * this.random.nextGaussian() / 15),
                            (this.random.nextFloat() * 0.035) + 0.085);
                }
            }
        }
    }

    @Override
    protected void onEffectAdded(MobEffectInstance mobEffectInstance, @Nullable Entity entity) {
        super.onEffectAdded(mobEffectInstance, entity);

        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.players().forEach(p -> p.connection.send(new ClientboundUpdateMobEffectPacket(this.getId(), mobEffectInstance, true)));
        }
    }

    @Override
    public boolean canBeAffected(MobEffectInstance mobEffectInstance) {
        return mobEffectInstance.getEffect() != MobEffects.REGENERATION &&
                mobEffectInstance.getEffect() != MobEffects.INSTANT_HEALTH &&
                mobEffectInstance.getEffect() != MobEffects.ABSORPTION;
    }

    @Override
    public void kill(ServerLevel serverLevel) {
        this.setHealth(0);
        this.die(this.damageSources().genericKill());
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float damageAmount) {
        if (damageAmount >= 30) {
            damageAmount = 2;
        }
        else if (damageAmount >= 10 && damageSource.isDirect() && !damageSource.is(BzTags.COSMIC_CRYSTAL_RESISTANT_TO)) {
            damageAmount = 2;
        }
        else if (damageAmount > 1) {
            damageAmount = 1;
        }

        Entity entity2;
        if (this.isInvulnerableTo(serverLevel, damageSource)) {
            return false;
        }
        if (damageSource.getEntity() instanceof CosmicCrystalEntity ||
            damageSource.getDirectEntity() instanceof CosmicCrystalEntity)
        {
            return false;
        }
        if (this.isDeadOrDying()) {
            return false;
        }
        if (damageSource.is(DamageTypeTags.IS_FIRE) && this.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            return false;
        }
        if (this.isSleeping()) {
            this.stopSleeping();
        }
        this.noActionTime = 0;

        boolean bl = false;

        if (this.invulnerableTime > 0) {
            return false;
        }
        else {
            this.lastHurt = damageAmount;
            this.invulnerableTime = 10;
            this.actuallyHurt(serverLevel, damageSource, damageAmount);
            this.hurtTime = this.hurtDuration = 10;
        }

        if (damageSource.is(DamageTypeTags.DAMAGES_HELMET) && !this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            this.hurtHelmet(damageSource, damageAmount);
            damageAmount *= 0.75f;
        }

        if ((entity2 = damageSource.getEntity()) != null) {
            if (entity2 instanceof LivingEntity livingEntity2) {
                if (!damageSource.is(DamageTypeTags.NO_ANGER)) {
                    this.setLastHurtByMob(livingEntity2);
                }
            }

            if (entity2 instanceof Player player) {
                this.lastHurtByPlayerMemoryTime = 100;
                this.lastHurtByPlayer = EntityReference.of(player);
            }
            else if (entity2 instanceof TamableAnimal tamableAnimal && tamableAnimal.isTame()) {
                this.lastHurtByPlayerMemoryTime = 100;
                LivingEntity livingEntity = tamableAnimal.getOwner();
                this.lastHurtByPlayer = livingEntity instanceof Player player ? EntityReference.of(player) : null;
            }
        }

        this.level().broadcastDamageEvent(this, damageSource);
        if (!damageSource.is(DamageTypeTags.NO_IMPACT)) {
            this.markHurt();
        }

        if (entity2 != null && !damageSource.is(DamageTypeTags.IS_EXPLOSION)) {
            double xDiff = entity2.getX() - this.getX();
            double zDiff = entity2.getZ() - this.getZ();
            while (xDiff * xDiff + zDiff * zDiff < 1.0E-4) {
                xDiff = (Math.random() - Math.random()) * 0.01;
                zDiff = (Math.random() - Math.random()) * 0.01;
            }
            if (!bl) {
                this.indicateDamage(xDiff, zDiff);
            }
        }

        if (this.isDeadOrDying()) {
            SoundEvent soundEvent = this.getDeathSound();
            if (soundEvent != null) {
                this.playSound(soundEvent, this.getSoundVolume(), this.getVoicePitch());
            }
            this.die(damageSource);
            EssenceBlockEntity essenceBlockEntity = EssenceBlockEntity.getEssenceBlockAtLocation(this.level(), this.getEssenceControllerDimension(), this.getEssenceControllerBlockPos(), this.getEssenceController());
            if (essenceBlockEntity != null && essenceBlockEntity.getBlockState().getBlock() instanceof EssenceBlockWhite essenceBlockWhite) {
                essenceBlockWhite.crystalKilled(this, essenceBlockEntity);
            }
        }
        else {
            this.playHurtSound(damageSource);
        }

        boolean dealtDamage = damageAmount > 0;
        if (dealtDamage) {
            ((LivingEntityAccessor)this).bumblezone$setLastDamageSource(damageSource);
            ((LivingEntityAccessor)this).bumblezone$setLastDamageStamp(this.level().getGameTime());
        }

        if (entity2 instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.PLAYER_HURT_ENTITY.trigger(serverPlayer, this, damageSource, damageAmount, damageAmount, bl);
        }

        return damageAmount > 0;
    }

    @Override
    protected void actuallyHurt(ServerLevel serverLevel, DamageSource damageSource, float damage) {
        if (!this.isInvulnerableTo(serverLevel, damageSource)) {
            float nonNegativeDamage = Math.max(damage, 0.0F);
            float h = damage - nonNegativeDamage;
            if (h > 0.0F && h < 3.4028235E37F) {
                Entity damageSourceEntity = damageSource.getEntity();
                if (damageSourceEntity instanceof ServerPlayer serverPlayer) {
                    serverPlayer.awardStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(h * 10.0F));
                }
            }

            if (nonNegativeDamage != 0.0F) {
                this.getCombatTracker().recordDamage(damageSource, nonNegativeDamage);
                this.setHealth(this.getHealth() - nonNegativeDamage);
                this.gameEvent(GameEvent.ENTITY_DAMAGE);
            }
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public boolean shouldDropExperience() {
        return false;
    }

    @Override
    protected void dropExperience(ServerLevel level, Entity entity) {}

    @Override
    public HumanoidArm getMainArm() {
        return null;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {}

    @Override
    public boolean shouldShowName() {
        return false;
    }

    @Override
    public boolean isOnPortalCooldown() {
        return true;
    }

    @Override
    public boolean fireImmune() {
        return false;
    }

    @Override
    public void lavaHurt() {}

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    public boolean isInWall() {
        return false;
    }

    @Override
    public boolean isCurrentlyGlowing() {
        return true;
    }

    @Override
    public int getPortalCooldown() {
        return this.getEssenceController() == null ? super.getPortalCooldown() : Integer.MAX_VALUE;
    }

    @Override
    public boolean canTeleport(Level fromLevel, Level toLevel) {
        return super.canTeleport(fromLevel, toLevel) && this.getEssenceController() == null;
    }

    @Override
    public Entity teleport(TeleportTransition teleportTransition) {
        if (this.getEssenceController() != null) {
            return this;
        }
        return super.teleport(teleportTransition);
    }

    private boolean laserHurtAttack(LivingEntity livingEntity) {
        float damageAmount;
        float maxHealth = Math.max(livingEntity.getHealth(), livingEntity.getMaxHealth());

        if (livingEntity instanceof ServerPlayer serverPlayer) {
            if (serverPlayer.isCreative()) {
                return true;
            }

            if (EssenceOfTheBees.hasEssence(serverPlayer)) {
                damageAmount = maxHealth / 4;
            }
            else {
                damageAmount = maxHealth / 3;
            }
        }
        else {
            damageAmount = maxHealth / 4;
        }

        livingEntity.hurt(this.level().damageSources().source(BzDamageSources.COSMIC_CRYSTAL_TYPE, this, this), damageAmount);
        return false;
    }

    private boolean physicalHurtAttack(LivingEntity livingEntity) {
        float damageAmount;
        float maxHealth = Math.max(livingEntity.getHealth(), livingEntity.getMaxHealth());

        if (this.getCosmicCrystalState() == CosmicCrystalState.TRACKING_SMASHING_ATTACK && this.targetEntity == livingEntity) {
            collidingAttackExplosion();
        }

        if (livingEntity instanceof ServerPlayer serverPlayer) {
            if (serverPlayer.isCreative()) {
                return true;
            }

            if (EssenceOfTheBees.hasEssence(serverPlayer)) {
                damageAmount = maxHealth / 6;
            }
            else {
                damageAmount = maxHealth / 4;
            }
        }
        else {
            damageAmount = maxHealth / 6;
        }

        livingEntity.hurt(this.level().damageSources().source(BzDamageSources.COSMIC_CRYSTAL_TYPE, this, this), damageAmount);
        this.lastPhysicalHit = this.currentStateTimeTick;

        for (Holder<MobEffect> mobEffect : new HashSet<>(livingEntity.getActiveEffectsMap().keySet())) {
            if (mobEffect.value().isBeneficial()) {
                livingEntity.removeEffect(mobEffect);
            }
        }

        return false;
    }

    // Try to stop healing
    @Override
    public void heal(float f) {}

    @Override
    public boolean isInvulnerableTo(ServerLevel serverLevel, DamageSource damageSource) {
        DamageSources sources = this.level().damageSources();
        if (this.getShield() && damageSource != sources.fellOutOfWorld() && damageSource != sources.outOfBorder()) {
            return true;
        }

        return super.isInvulnerableTo(serverLevel, damageSource);
    }

    @Override
    protected boolean canAddPassenger(Entity entity) {
        return false;
    }

    @Override
    protected boolean couldAcceptPassenger() {
        return false;
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
    }

    private void spawnLargeParticleCloud(int radius) {
        int radiusSquared = radius * radius;
        for (int xParticle = -radius; xParticle <= radius; xParticle++) {
            for (int zParticle = -radius; zParticle <= radius; zParticle++) {
                for (int yParticle = -radius; yParticle <= radius; yParticle++) {
                    int distanceSquared = xParticle * xParticle + yParticle * yParticle + zParticle * zParticle;
                    if (distanceSquared <= radiusSquared) {
                        for (int i = 0; i <= (radiusSquared / Math.max(1, distanceSquared)) * 3; i++) {
                            this.spawnFancyParticle(this.position().add(xParticle, yParticle, zParticle));
                        }
                    }
                }
            }
        }
    }

    public @Nullable Pair<Vec3, Vec3> getLaserPositions(float partialTick) {
        if(!this.isLaserFiring()) {
            return null;
        }

        Vec3 lerpedLook = this.getViewVector(partialTick);
        Vec3 startPos = this.getEyePosition(partialTick).add(lerpedLook);
        Vec3 endPos = startPos.add(lerpedLook.scale(50));

        HitResult hitResult = this.level().clip(new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

        if (hitResult.getType() != HitResult.Type.MISS) {
            endPos = hitResult.getLocation();
        }

        return Pair.of(startPos, endPos);
    }

    private void spawnFancyParticle(Vec3 center) {
        this.level().addParticle(
                ParticleTypes.END_ROD,
                center.x() + this.random.nextGaussian() / 5,
                center.y() + this.random.nextGaussian() / 2.5,
                center.z() + this.random.nextGaussian() / 5,
                (this.random.nextFloat() * this.random.nextGaussian() / 15),
                (this.random.nextFloat() * this.random.nextGaussian() / 15),
                (this.random.nextFloat() * this.random.nextGaussian() / 15));

        this.level().addParticle(
                BzParticles.SPARKLE_PARTICLE.get(),
                center.x() + this.random.nextGaussian() / 4,
                center.y() + this.random.nextGaussian() / 2.5,
                center.z() + this.random.nextGaussian() / 4,
                (this.random.nextFloat() * this.random.nextGaussian() / 15),
                (this.random.nextFloat() * this.random.nextGaussian() / 15),
                (this.random.nextFloat() * this.random.nextGaussian() / 15));
    }
}
