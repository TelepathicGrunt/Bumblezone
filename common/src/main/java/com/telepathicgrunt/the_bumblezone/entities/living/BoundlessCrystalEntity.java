package com.telepathicgrunt.the_bumblezone.entities.living;

import com.telepathicgrunt.the_bumblezone.client.rendering.boundlesscrystal.BoundlessCrystalState;
import com.telepathicgrunt.the_bumblezone.client.rendering.rootmin.RootminPose;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BoundlessCrystalEntity extends LivingEntity {
    public static final EntityDataSerializer<BoundlessCrystalState> BOUNDLESS_CRYSTAL_STATE_SERIALIZER = EntityDataSerializer.simpleEnum(BoundlessCrystalState.class);
    private static final EntityDataAccessor<BoundlessCrystalState> BOUNDLESS_CRYSTAL_STATE = SynchedEntityData.defineId(BoundlessCrystalEntity.class, BOUNDLESS_CRYSTAL_STATE_SERIALIZER);

    private final NonNullList<ItemStack> armorItems = NonNullList.withSize(0, ItemStack.EMPTY);

    public final AnimationState rotateAnimationState = new AnimationState();

    public BoundlessCrystalEntity(EntityType<? extends BoundlessCrystalEntity> entityType, Level level) {
        super(entityType, level);
        rotateAnimationState.start(tickCount);
    }

    public static AttributeSupplier.Builder getAttributeBuilder() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.1D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 40.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BOUNDLESS_CRYSTAL_STATE, BoundlessCrystalState.NORMAL);
    }

    public void tick() {
        super.tick();
    }

    @Override
    public HumanoidArm getMainArm() {
        return null;
    }

    @Override
    public void baseTick() {}

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return armorItems;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {}
}