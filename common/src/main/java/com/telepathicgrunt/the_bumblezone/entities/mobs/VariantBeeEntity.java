package com.telepathicgrunt.the_bumblezone.entities.mobs;

import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class VariantBeeEntity extends Bee {
   private static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(VariantBeeEntity.class, EntityDataSerializers.STRING);
   public static final String VARIANT_TAG = "variant";

   public VariantBeeEntity(Level worldIn) {
      super(BzEntities.VARIANT_BEE.get(), worldIn);
   }

   public VariantBeeEntity(EntityType<? extends VariantBeeEntity> type, Level worldIn) {
      super(type, worldIn);
      getVariant();
   }

   public String getVariant() {
      String variant = this.entityData.get(VARIANT);
      if (!level().isClientSide() && (variant == null || variant.isEmpty())) {
         variant = BzGeneralConfigs.variantBeeTypes.get(random.nextInt(BzGeneralConfigs.variantBeeTypes.size()));
         setVariant(variant);
      }
      return variant;
   }

   public void setVariant(String variant) {
      if (!level().isClientSide() && (variant == null || variant.isEmpty())) {
         variant = BzGeneralConfigs.variantBeeTypes.get(random.nextInt(BzGeneralConfigs.variantBeeTypes.size()));
      }
      this.entityData.set(VARIANT, variant);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(VARIANT, "");
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compoundTag) {
      super.addAdditionalSaveData(compoundTag);
      compoundTag.putString(VARIANT_TAG, this.getVariant());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compoundTag) {
      super.readAdditionalSaveData(compoundTag);
      setVariant(compoundTag.getString(VARIANT_TAG));
   }

   @Override
   public Bee getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
      if (ageableMob instanceof VariantBeeEntity variantBeeEntity) {
         VariantBeeEntity babyBee = BzEntities.VARIANT_BEE.get().create(serverLevel);

         if (babyBee != null) {
            babyBee.setVariant(variantBeeEntity.getVariant());
         }

         return babyBee;
      }
      else if (ageableMob instanceof Bee) {
         return EntityType.BEE.create(serverLevel);
      }
      else {
         return BzEntities.VARIANT_BEE.get().create(serverLevel);
      }
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
   protected Component getTypeName() {
      if (getVariant() != null) {
         return Component.translatable("buzzing_briefcase.the_bumblezone.bee_typing." + getVariant());
      }
      return this.getType().getDescription();
   }
}