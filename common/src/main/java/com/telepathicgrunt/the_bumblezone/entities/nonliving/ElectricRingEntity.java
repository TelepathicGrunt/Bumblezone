package com.telepathicgrunt.the_bumblezone.entities.nonliving;

import com.telepathicgrunt.the_bumblezone.blocks.EssenceBlockYellow;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class ElectricRingEntity extends Entity {
    private static final EntityDataAccessor<Boolean> DATA_ID_DISAPPEARING_SET = SynchedEntityData.defineId(ElectricRingEntity.class, EntityDataSerializers.BOOLEAN);
    public static final int DISAPPERING_TIMESPAN = 20;
    public static final int APPEARING_TIMESPAN = 20;

    public int disappearingTime = -1;
    private UUID essenceController = null;
    private BlockPos essenceControllerBlockPos = null;
    private ResourceKey<Level> essenceControllerDimension = null;

    public ElectricRingEntity(EntityType<? extends ElectricRingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_ID_DISAPPEARING_SET, this.disappearingTime > 0);
    }

    public UUID getEssenceController() {
        return essenceController;
    }

    public void setEssenceController(UUID essenceController) {
        this.essenceController = essenceController;
    }

    public BlockPos getEssenceControllerBlockPos() {
        return essenceControllerBlockPos;
    }

    public void setEssenceControllerBlockPos(BlockPos essenceControllerBlockPos) {
        this.essenceControllerBlockPos = essenceControllerBlockPos;
    }

    public ResourceKey<Level> getEssenceControllerDimension() {
        return essenceControllerDimension;
    }

    public void setEssenceControllerDimension(ResourceKey<Level> essenceControllerDimension) {
        this.essenceControllerDimension = essenceControllerDimension;
    }

    public boolean getDisappearingMarker() {
        return this.entityData.get(DATA_ID_DISAPPEARING_SET);
    }

    protected void setDisappearingMarker(boolean disappearingMarker) {
        this.entityData.set(DATA_ID_DISAPPEARING_SET, disappearingMarker);
    }

    @Override
    protected float getEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return entityDimensions.height * 0.5f;
    }

    public void tick() {
        super.tick();
        this.setRot(this.getYRot(), this.getXRot());

        if (this.level().isClientSide()) {
            if (this.tickCount % 2 == 0) {
                this.makeParticle(1);
            }

            if (this.getDisappearingMarker() && disappearingTime == -1) {
                disappearingTime = DISAPPERING_TIMESPAN;
            }
        }
        else if (tickCount == 1) {
            this.makeServerParticle(50, (ServerLevel) this.level());
        }

        if (disappearingTime > 0) {
            disappearingTime--;
        }

        if (disappearingTime == 0) {
            this.discard();
        }

        if (!this.level().isClientSide() && this.tickCount % 20 == 0) {
            checkIfStillInEvent();
        }
    }

    private void checkIfStillInEvent() {
        UUID essenceUuid = this.getEssenceController();
        ResourceKey<Level> essenceDimension = this.getEssenceControllerDimension();
        BlockPos essenceBlockPos = this.getEssenceControllerBlockPos();

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
            }
        }
        else {
            //Failed check. Kill mob.
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    private void makeParticle(int particlesToSpawn) {
        if (particlesToSpawn > 0) {
            double size = this.getBoundingBox().getSize();
            double extraScale = 1.5;
            double x = this.getX() + ((this.random.nextFloat() - 0.5f) * size * extraScale);
            double y = this.getY() + (size / 2) + ((this.random.nextFloat() - 0.5f) * size * extraScale);
            double z = this.getZ() + ((this.random.nextFloat() - 0.5f) * size * extraScale);

            for(int i = 0; i < particlesToSpawn; ++i) {
                this.level().addParticle(
                        ParticleTypes.ELECTRIC_SPARK,
                        x,
                        y,
                        z,
                        (this.random.nextFloat() - 0.5f) * 0.5f,
                        (this.random.nextFloat() - 0.5f) * 0.5f,
                        (this.random.nextFloat() - 0.5f) * 0.5f);
            }
        }
    }

    private void makeServerParticle(int particlesToSpawn, ServerLevel serverLevel) {
        if (particlesToSpawn > 0) {
            double xOffset = (random.nextFloat() * 0.3) - 0.15;
            double yOffset = (random.nextFloat() * 0.3) - 0.15;
            double zOffset = (random.nextFloat() * 0.3) - 0.15;

            serverLevel.sendParticles(
                    ParticleTypes.CRIT,
                    this.getX(),
                    this.getEyeY(),
                    this.getZ(),
                    particlesToSpawn,
                    random.nextGaussian() * xOffset,
                    (random.nextGaussian() * yOffset) + 0.2f,
                    random.nextGaussian() * zOffset,
                    1.5f
            );

            serverLevel.sendParticles(
                    ParticleTypes.ENCHANTED_HIT,
                    this.getX(),
                    this.getEyeY(),
                    this.getZ(),
                    particlesToSpawn,
                    random.nextGaussian() * xOffset,
                    (random.nextGaussian() * yOffset) + 0.2f,
                    random.nextGaussian() * zOffset,
                    1f
            );

            serverLevel.sendParticles(
                    ParticleTypes.FIREWORK,
                    this.getX(),
                    this.getEyeY(),
                    this.getZ(),
                    particlesToSpawn,
                    random.nextGaussian() * xOffset,
                    (random.nextGaussian() * yOffset) + 0.2f,
                    random.nextGaussian() * zOffset,
                    0.2f
            );
        }
    }

    @Override
    public void playerTouch(Player player) {
        if (!this.level().isClientSide() && !this.isRemoved() && this.disappearingTime == -1) {

            double ringRadiusSq = Math.pow((this.getBoundingBox().getSize() / 2) + 0.2, 2);
            Vec3 centerOfRing = this.getEyePosition();
            if (!intersectedPassed(player, ringRadiusSq, centerOfRing, player.getEyePosition())) {
                if (!intersectedPassed(player, ringRadiusSq, centerOfRing, player.getBoundingBox().getCenter())) {
                    intersectedPassed(player, ringRadiusSq, centerOfRing, player.getEyePosition().subtract(0, player.getEyeHeight(), 0));
                }
            }
        }

        super.playerTouch(player);
    }

    private boolean intersectedPassed(Player player, double ringRadiusSq, Vec3 centerOfRing, Vec3 playerPosToCheck) {
        Vec3 playerPoint = playerPosToCheck.subtract(centerOfRing);

        if ((playerPoint.x() * playerPoint.x()) +
            (playerPoint.y() * playerPoint.y()) +
            (playerPoint.z() * playerPoint.z()) < ringRadiusSq)
        {

            Vec3 normalVector = this.getLookAngle();
            // Source https://www.geeksforgeeks.org/distance-between-a-point-and-a-plane-in-3-d/#
            double numerator = Math.abs(
                    (normalVector.x() * playerPoint.x()) +
                    (normalVector.y() * playerPoint.y()) +
                    (normalVector.z() * playerPoint.z())
            );
            double divisor = Math.sqrt(
                    (normalVector.x() * normalVector.x()) +
                    (normalVector.y() * normalVector.y()) +
                    (normalVector.z() * normalVector.z())
            );
            double distanceBetweenPlayerAndPlane = numerator / divisor;
            double playerSpeed = (Math.pow(player.getSpeed() + 1 , 3) - 1) * 2;
            double rangeCheck = Math.max(0.2f, playerSpeed);
            if (distanceBetweenPlayerAndPlane < rangeCheck) {
                // Notify Essence Block
                EssenceBlockEntity essenceBlockEntity = EssenceBlockEntity.getEssenceBlockAtLocation(this.level(), this.getEssenceControllerDimension(), this.getEssenceControllerBlockPos(), this.getEssenceController());
                if (essenceBlockEntity != null && essenceBlockEntity.getBlockState().getBlock() instanceof EssenceBlockYellow essenceBlockYellow) {
                    essenceBlockYellow.ringActivated(this, essenceBlockEntity, (ServerPlayer) player);
                }
                this.makeServerParticle(50, (ServerLevel) this.level());
                this.disappearingTime = DISAPPERING_TIMESPAN;
                this.setDisappearingMarker(true);
                this.playSound(BzSounds.ELECTRIC_RING_PASSED.get());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public Entity changeDimension(ServerLevel serverLevel) {
        return this;
    }

    @Override
    public int getPortalCooldown() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        if (compoundTag.contains("disappearingTime")) {
            this.disappearingTime = compoundTag.getInt("disappearingTime");
        }
        this.setDisappearingMarker(compoundTag.getBoolean("disappearingMarker"));

        if (compoundTag.contains("essenceController")) {
            this.setEssenceController(compoundTag.getUUID("essenceController"));
        }
        if (compoundTag.contains("essenceControllerBlockPos")) {
            this.setEssenceControllerBlockPos(NbtUtils.readBlockPos(compoundTag.getCompound("essenceControllerBlockPos")));
        }
        if (compoundTag.contains("essenceControllerDimension")) {
            this.setEssenceControllerDimension(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(compoundTag.getString("essenceControllerDimension"))));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putInt("disappearingTime", this.disappearingTime);
        compoundTag.putBoolean("disappearingMarker", this.getDisappearingMarker());

        if (this.getEssenceController() != null) {
            compoundTag.putUUID("essenceController", this.getEssenceController());
        }
        if (this.getEssenceControllerBlockPos() != null) {
            compoundTag.put("essenceControllerBlockPos", NbtUtils.writeBlockPos(this.getEssenceControllerBlockPos()));
        }
        if (this.getEssenceControllerDimension() != null) {
            compoundTag.putString("essenceControllerDimension", this.getEssenceControllerDimension().location().toString());
        }
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket clientboundAddEntityPacket) {
        double d = clientboundAddEntityPacket.getX();
        double e = clientboundAddEntityPacket.getY();
        double f = clientboundAddEntityPacket.getZ();
        float g = clientboundAddEntityPacket.getYRot();
        float h = clientboundAddEntityPacket.getXRot();
        this.syncPacketPositionCodec(d, e, f);
        this.setId(clientboundAddEntityPacket.getId());
        this.setUUID(clientboundAddEntityPacket.getUUID());
        this.absMoveTo(d, e, f, g, h);
        this.setDeltaMovement(clientboundAddEntityPacket.getXa(), clientboundAddEntityPacket.getYa(), clientboundAddEntityPacket.getZa());
    }
}