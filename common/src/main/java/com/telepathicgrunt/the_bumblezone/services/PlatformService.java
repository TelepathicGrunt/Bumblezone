package com.telepathicgrunt.the_bumblezone.services;

import com.mojang.authlib.GameProfile;
import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import com.telepathicgrunt.the_bumblezone.items.BzCustomBucketItem;
import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;
import com.telepathicgrunt.the_bumblezone.platform.ModInfo;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface PlatformService {
    PlatformService INSTANCE = GeneralUtils.loadService(PlatformService.class);

    <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> entityFactory, MobCategory category, float size, int clientTrackingRange, int updateInterval, String buildName);

    <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> entityFactory, MobCategory category, float xzSize, float ySize, int clientTrackingRange, int updateInterval, String buildName);

    <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> entityFactory, MobCategory category, float xzSize, float ySize, float eyeHeight, int clientTrackingRange, int updateInterval, String buildName);

    SpawnGroupData finalizeSpawn(Mob entity, ServerLevelAccessor world, SpawnGroupData spawnGroupData, EntitySpawnReason spawnReason);

    ServerPlayer getFakePlayer(ServerLevel level, GameProfile gameProfile);

    boolean isFakePlayer(ServerPlayer player);

    boolean isModLoaded(String modid);

    boolean isNeoForge();

    int getXpDrop(LivingEntity entity, Player attackingPlayer, int xp);

    ItemStack getCraftingRemainder(ItemStack stack);

    boolean hasCraftingRemainder(ItemStack stack);

    Fluid getBucketFluid(BucketItem bucket);

    @Nullable
    ModInfo getModInfo(String modid);

    @Nullable
    ModInfo getModInfo(String modid, boolean qualifierIsVersion);

    boolean sendBlockBreakEvent(Level level, BlockPos pos, BlockState state, BlockEntity entity, Player player);

    void afterBlockBreakEvent(Level level, BlockPos pos, BlockState state, BlockEntity entity, Player player);

    double getFluidHeight(Entity entity, TagKey<Fluid> fallback, FluidData... fluids);

    boolean isEyesInNoFluid(Entity entity);

    InteractionResult performItemUse(Level world, Player user, InteractionHand hand, Fluid fluid, BzCustomBucketItem bzCustomBucketItem);

    boolean isPermissionAllowedAtSpot(Level level, Entity entity, BlockPos pos, boolean placingBlock);

    boolean isDimensionAllowed(ServerPlayer serverPlayer, ResourceKey<Level> dimension);

    boolean isItemAbility(ItemStack stack, TagKey<Item> targetBackupTag, String... targetToolAction);

    void disableFlight(Player player);

    boolean isDevEnvironment();

    boolean isClientEnvironment();

    boolean shouldMobSplit(Mob parent, List<Mob> children);

    Fluid getBucketItemFluid(BucketItem stack);

    RegistryAccess getCurrentRegistryAccess();

    <T extends AbstractContainerMenu> MenuType<T> create(BzMenuTypes.MenuCreator<T> creator);

    <T extends Module<T>> Optional<T> getModule(Entity entity, ModuleHolder<T> moduleHolder);

    Thread createServerThread(Runnable runnable, String name);
}
