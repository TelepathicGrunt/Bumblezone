package com.telepathicgrunt.the_bumblezone.utils;

import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfo;
import com.telepathicgrunt.the_bumblezone.platform.ModInfo;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * We use @Contract(pure = true) because intellij will think that they always return the same value.
 */
public class PlatformHooks {

    @ExpectPlatform
    public static <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> entityFactory, MobCategory category, float size, int clientTrackingRange, int updateInterval, String buildName) {
        throw new NotImplementedException("PlatformHooks isEyesInNoFluid is not implemented!");
    }

    @ExpectPlatform
    @Contract(pure = true)
    public static int canEntitySpawn(Mob entity, LevelAccessor world, double x, double y, double z, BaseSpawner spawner, MobSpawnType spawnReason) {
        throw new NotImplementedException("PlatformHooks canEntitySpawn is not implemented!");
    }

    @ExpectPlatform
    @Contract(pure = true)
    public static ServerPlayer getFakePlayer(ServerLevel level) {
        throw new NotImplementedException("PlatformHooks getFakePlayer is not implemented!");
    }

    @ExpectPlatform
    @Contract(pure = true)
    public static boolean isFakePlayer(ServerPlayer player) {
        throw new NotImplementedException("PlatformHooks isFakePlayer is not implemented!");
    }

    @ExpectPlatform
    @Contract(pure = true)
    public static boolean isModLoaded(String modid) {
        throw new NotImplementedException("PlatformHooks isModLoaded is not implemented!");
    }

    @ExpectPlatform
    @Contract(pure = true)
    public static int getXpDrop(LivingEntity entity, Player attackingPlayer, int xp) {
        throw new NotImplementedException("PlatformHooks getXpDrop is not implemented!");
    }

    @ExpectPlatform
    @Contract(pure = true)
    public static ItemStack getCraftingRemainder(ItemStack stack) {
        throw new NotImplementedException("PlatformHooks getCraftingRemainder is not implemented!");
    }

    @ExpectPlatform
    @Contract(pure = true)
    public static boolean hasCraftingRemainder(ItemStack stack) {
        throw new NotImplementedException("PlatformHooks hasCraftingRemainder is not implemented!");
    }

    @ExpectPlatform
    @Contract(pure = true)
    public static Fluid getBucketFluid(BucketItem bucket) {
        throw new NotImplementedException("PlatformHooks getBucketFluid is not implemented!");
    }

    public static ModInfo getModInfo(String modid) {
        return getModInfo(modid, false);
    }

    @Nullable
    @ExpectPlatform
    public static ModInfo getModInfo(String modid, boolean qualifierIsVersion) {
        throw new NotImplementedException("PlatformHooks getModInfo is not implemented!");
    }

    @ExpectPlatform
    public static boolean sendBlockBreakEvent(Level level, BlockPos pos, BlockState state, BlockEntity entity, Player player) {
        throw new NotImplementedException("PlatformHooks sendBlockBreakEvent is not implemented!");
    }

    @ExpectPlatform
    public static void afterBlockBreakEvent(Level level, BlockPos pos, BlockState state, BlockEntity entity, Player player) {
        throw new NotImplementedException("PlatformHooks sendBlockBreakEvent is not implemented!");
    }

    @ExpectPlatform
    public static double getFluidHeight(Entity entity, TagKey<Fluid> fallback, FluidInfo... fluids) {
        throw new NotImplementedException("PlatformHooks getFluidHeight is not implemented!");
    }

    @ExpectPlatform
    public static boolean isEyesInNoFluid(Entity entity) {
        throw new NotImplementedException("PlatformHooks isEyesInNoFluid is not implemented!");
    }
}
