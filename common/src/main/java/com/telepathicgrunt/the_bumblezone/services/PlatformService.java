package com.telepathicgrunt.the_bumblezone.services;

import com.mojang.authlib.GameProfile;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfo;
import com.telepathicgrunt.the_bumblezone.items.BzCustomBucketItem;
import com.telepathicgrunt.the_bumblezone.platform.ModInfo;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public interface PlatformService {

    PlatformService INSTANCE = GeneralUtils.loadService(PlatformService.class);

    default <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> entityFactory, MobCategory category, float size, boolean scalable, int clientTrackingRange, int updateInterval, String buildName) {
        throw new NotImplementedException("PlatformHooks createEntityType is not implemented!");
    }

    default <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> entityFactory, MobCategory category, float xzSize, float ySize, boolean scalable, int clientTrackingRange, int updateInterval, String buildName) {
        throw new NotImplementedException("PlatformHooks createEntityType 2 is not implemented!");
    }

    default SpawnGroupData finalizeSpawn(Mob entity, ServerLevelAccessor world, SpawnGroupData spawnGroupData, MobSpawnType spawnReason, CompoundTag tag) {
        throw new NotImplementedException("PlatformHooks canEntitySpawn is not implemented!");
    }

    default ServerPlayer getFakePlayer(ServerLevel level, GameProfile gameProfile) {
        throw new NotImplementedException("PlatformHooks getFakePlayer is not implemented!");
    }

    default boolean isFakePlayer(ServerPlayer player) {
        throw new NotImplementedException("PlatformHooks isFakePlayer is not implemented!");
    }

    default boolean isModLoaded(String modid) {
        throw new NotImplementedException("PlatformHooks isModLoaded is not implemented!");
    }

    default int getXpDrop(LivingEntity entity, Player attackingPlayer, int xp) {
        throw new NotImplementedException("PlatformHooks getXpDrop is not implemented!");
    }

    default ItemStack getCraftingRemainder(ItemStack stack) {
        throw new NotImplementedException("PlatformHooks getCraftingRemainder is not implemented!");
    }

    default boolean hasCraftingRemainder(ItemStack stack) {
        throw new NotImplementedException("PlatformHooks hasCraftingRemainder is not implemented!");
    }

    default Fluid getBucketFluid(BucketItem bucket) {
        throw new NotImplementedException("PlatformHooks getBucketFluid is not implemented!");
    }

    default ModInfo getModInfo(String modid) {
        return getModInfo(modid, false);
    }

    @Nullable
    default ModInfo getModInfo(String modid, boolean qualifierIsVersion) {
        throw new NotImplementedException("PlatformHooks getModInfo is not implemented!");
    }

    default boolean sendBlockBreakEvent(Level level, BlockPos pos, BlockState state, BlockEntity entity, Player player) {
        throw new NotImplementedException("PlatformHooks sendBlockBreakEvent is not implemented!");
    }

    default void afterBlockBreakEvent(Level level, BlockPos pos, BlockState state, BlockEntity entity, Player player) {
        throw new NotImplementedException("PlatformHooks sendBlockBreakEvent is not implemented!");
    }

    default double getFluidHeight(Entity entity, TagKey<Fluid> fallback, FluidInfo... fluids) {
        throw new NotImplementedException("PlatformHooks getFluidHeight is not implemented!");
    }

    default boolean isEyesInNoFluid(Entity entity) {
        throw new NotImplementedException("PlatformHooks isEyesInNoFluid is not implemented!");
    }

    default InteractionResultHolder<ItemStack> performItemUse(Level world, Player user, InteractionHand hand, Fluid fluid, BzCustomBucketItem bzCustomBucketItem) {
        throw new NotImplementedException("PlatformHooks performItemUse is not implemented!");
    }

    default boolean isPermissionAllowedAtSpot(Level level, Entity entity, BlockPos pos, boolean placingBlock) {
        throw new NotImplementedException("PlatformHooks isPermissionAllowedAtSpot is not implemented!");
    }

    default boolean isDimensionAllowed(ServerPlayer serverPlayer, ResourceKey<Level> dimension) {
        throw new NotImplementedException("PlatformHooks isDimensionAllowed is not implemented!");
    }

    default boolean isToolAction(ItemStack stack, Class<?> targetBackupClass, String... targetToolAction) {
        throw new NotImplementedException("PlatformHooks isToolAction is not implemented!");
    }

    default Fluid getBucketItemFluid(BucketItem stack) {
        throw new NotImplementedException("PlatformHooks getBucketItemFluid is not implemented!");
    }

    default String getPlatformTagNamespace() {
        throw new NotImplementedException("PlatformHooks getPlatformTagNamespace is not implemented!");
    }

    default String getPlatformName() {
        throw new NotImplementedException("PlatformHooks getPlatformName is not implemented!");
    }
    
    default boolean isDevEnvironment() {
        throw new NotImplementedException("PlatformHooks isDevEnvironment is not implemented!");
    }
}
