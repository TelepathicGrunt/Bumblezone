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
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

public interface PlatformService {
    PlatformService INSTANCE = GeneralUtils.loadService(PlatformService.class);

    <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> entityFactory, MobCategory category, float size, boolean scalable, int clientTrackingRange, int updateInterval, String buildName);

    <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> entityFactory, MobCategory category, float xzSize, float ySize, boolean scalable, int clientTrackingRange, int updateInterval, String buildName);

    SpawnGroupData finalizeSpawn(Mob entity, ServerLevelAccessor world, SpawnGroupData spawnGroupData, MobSpawnType spawnReason, CompoundTag tag);

    ServerPlayer getFakePlayer(ServerLevel level, GameProfile gameProfile);

    boolean isFakePlayer(ServerPlayer player);

    boolean isModLoaded(String modid);

    int getXpDrop(LivingEntity entity, Player attackingPlayer, int xp);

    ItemStack getCraftingRemainder(ItemStack stack);

    boolean hasCraftingRemainder(ItemStack stack);

    Fluid getBucketFluid(BucketItem bucket);

    ModInfo getModInfo(String modid, boolean qualifierIsVersion);

    boolean sendBlockBreakEvent(Level level, BlockPos pos, BlockState state, BlockEntity entity, Player player);

    void afterBlockBreakEvent(Level level, BlockPos pos, BlockState state, BlockEntity entity, Player player);

    double getFluidHeight(Entity entity, TagKey<Fluid> fallback, FluidInfo... fluids);

    boolean isEyesInNoFluid(Entity entity);

    InteractionResultHolder<ItemStack> performItemUse(Level world, Player user, InteractionHand hand, Fluid fluid, BzCustomBucketItem bzCustomBucketItem);

    boolean isPermissionAllowedAtSpot(Level level, Entity entity, BlockPos pos, boolean placingBlock);

    boolean isDimensionAllowed(ServerPlayer serverPlayer, ResourceKey<Level> dimension);

    boolean isToolAction(ItemStack stack, Class<?> targetBackupClass, String... targetToolAction);

    Fluid getBucketItemFluid(BucketItem stack);

    boolean isDevEnvironment();

    String getPlatformTagNamespace();

    boolean canApplyAtEnchantingTable(Enchantment enchantment, ItemStack stack);

    boolean isAllowedOnBooks(Enchantment enchantment);
}
