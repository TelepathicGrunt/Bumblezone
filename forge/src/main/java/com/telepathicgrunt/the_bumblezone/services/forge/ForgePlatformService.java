package com.telepathicgrunt.the_bumblezone.services.forge;

import com.mojang.authlib.GameProfile;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfo;
import com.telepathicgrunt.the_bumblezone.items.BzCustomBucketItem;
import com.telepathicgrunt.the_bumblezone.platform.ModInfo;
import com.telepathicgrunt.the_bumblezone.services.PlatformService;
import com.telepathicgrunt.the_bumblezone.utils.forge.ForgeModInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;

public class ForgePlatformService implements PlatformService {

    public <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> entityFactory, MobCategory category, float size, boolean scalable, int clientTrackingRange, int updateInterval, String buildName) {
        return EntityType.Builder
                .of(entityFactory, category)
                .sized(size, size)
                .clientTrackingRange(clientTrackingRange)
                .updateInterval(updateInterval)
                .build(buildName);
    }

    public <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> entityFactory, MobCategory category, float xzSize, float ySize, boolean scalable, int clientTrackingRange, int updateInterval, String buildName) {
        return EntityType.Builder
                .of(entityFactory, category)
                .sized(xzSize, ySize)
                .clientTrackingRange(clientTrackingRange)
                .updateInterval(updateInterval)
                .build(buildName);
    }

    public ModInfo getModInfo(String modid, boolean qualifierIsVersion) {
        return ModList.get().getModContainerById(modid)
                .map(container -> new ForgeModInfo(container.getModInfo(), qualifierIsVersion))
                .orElse(null);
    }

    public Fluid getBucketFluid(BucketItem bucket) {
        return bucket.getFluid();
    }

    public boolean hasCraftingRemainder(ItemStack stack) {
        return stack.hasCraftingRemainingItem();
    }

    public ItemStack getCraftingRemainder(ItemStack stack) {
        return stack.getCraftingRemainingItem();
    }

    public int getXpDrop(LivingEntity entity, Player attackingPlayer, int xp) {
        return ForgeEventFactory.getExperienceDrop(entity, attackingPlayer, xp);
    }

    public boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    public boolean isFakePlayer(ServerPlayer player) {
        return player instanceof FakePlayer;
    }

    public ServerPlayer getFakePlayer(ServerLevel level, GameProfile gameProfile) {
        if (gameProfile == null) {
            return FakePlayerFactory.getMinecraft(level);
        }
        return FakePlayerFactory.get(level, gameProfile);
    }

    public SpawnGroupData finalizeSpawn(Mob entity, ServerLevelAccessor world, SpawnGroupData spawnGroupData, MobSpawnType spawnReason, CompoundTag tag) {
        return ForgeEventFactory.onFinalizeSpawn(entity, world, world.getCurrentDifficultyAt(BlockPos.containing(entity.position())), spawnReason, spawnGroupData, tag);
    }

    public boolean sendBlockBreakEvent(Level level, BlockPos pos, BlockState state, BlockEntity entity, Player player) {
        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, pos, state, player);
        MinecraftForge.EVENT_BUS.post(event);
        return event.isCanceled();
    }

    public void afterBlockBreakEvent(Level level, BlockPos pos, BlockState state, BlockEntity entity, Player player) {
        //Do nothing
    }

    public double getFluidHeight(Entity entity, TagKey<Fluid> fallback, FluidInfo... fluids) {
        for (FluidInfo fluid : fluids) {
            double forgeTypeHeight = entity.getFluidTypeHeight(fluid.source().getFluidType());
            if (forgeTypeHeight > 0) {
                return forgeTypeHeight;
            }
        }
        return entity.getFluidHeight(fallback);
    }

    public boolean isEyesInNoFluid(Entity entity) {
        return entity.getEyeInFluidType().isAir();
    }

    public InteractionResultHolder<ItemStack> performItemUse(Level world, Player user, InteractionHand hand, Fluid fluid, BzCustomBucketItem bzCustomBucketItem) {
        return InteractionResultHolder.pass(user.getItemInHand(hand));
    }

    public boolean isPermissionAllowedAtSpot(Level level, Entity entity, BlockPos pos, boolean placingBlock) {
        if (entity instanceof Player player && !player.mayInteract(level, pos)) {
            return false;
        }

        if (placingBlock) {
            return !ForgeEventFactory.onBlockPlace(entity, BlockSnapshot.create(level.dimension(), level, pos), Direction.UP);
        }
        else if (entity instanceof LivingEntity livingEntity) {
            return ForgeEventFactory.onEntityDestroyBlock(livingEntity, pos, level.getBlockState(pos));
        }
        return true;
    }

    public boolean isDimensionAllowed(ServerPlayer serverPlayer, ResourceKey<Level> dimension) {
        return ForgeHooks.onTravelToDimension(serverPlayer, dimension);
    }

    public boolean isToolAction(ItemStack stack, Class<?> targetBackupClass, String... targetToolAction) {
        return Arrays.stream(targetToolAction).anyMatch(actionString -> stack.canPerformAction(ToolAction.get(actionString)))
                || targetBackupClass.isInstance(stack.getItem());
    }

    public Fluid getBucketItemFluid(BucketItem stack) {
        return stack.getFluid();
    }

    public String getPlatformTagNamespace() {
        return "forge";
    }

    public String getPlatformName() {
        return "forge";
    }

    public boolean isDevEnvironment() {
        return !FMLLoader.isProduction();
    }
}
