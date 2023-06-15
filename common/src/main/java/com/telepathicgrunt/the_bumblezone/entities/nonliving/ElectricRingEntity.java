package com.telepathicgrunt.the_bumblezone.entities.nonliving;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ElectricRingEntity extends Entity {

    public ElectricRingEntity(EntityType<? extends ElectricRingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected float getEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return entityDimensions.height * 0.5f;
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            if (this.tickCount % 2 == 0) {
                this.makeParticle(1);
            }
        }
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

    public boolean canCollideWith(Entity arg) {
        return arg.canBeCollidedWith() && !this.isPassengerOfSameVehicle(arg);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }
}