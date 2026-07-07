package com.telepathicgrunt.the_bumblezone.services.fabric;

import com.mojang.authlib.GameProfile;
import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.items.BzCustomBucketItem;
import com.telepathicgrunt.the_bumblezone.mixin.fabric.entities.EntityAccessor;
import com.telepathicgrunt.the_bumblezone.mixin.fabric.items.BucketItemAccessor;
import com.telepathicgrunt.the_bumblezone.mixin.items.ItemAccessor;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.fabric.RestrictedPortalsCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;
import com.telepathicgrunt.the_bumblezone.platform.ModInfo;
import com.telepathicgrunt.the_bumblezone.services.PlatformService;
import com.telepathicgrunt.the_bumblezone.utils.fabric.FabricModInfo;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.impl.attachment.AttachmentRegistryImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
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
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class FabricPlatformService implements PlatformService {

    @Override
    public <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> entityFactory, MobCategory category, float size, int clientTrackingRange, int updateInterval, String buildName) {
        return EntityType.Builder
                .of(entityFactory, category)
                .sized(size, size)
                .clientTrackingRange(clientTrackingRange)
                .updateInterval(updateInterval)
                .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Bumblezone.MODID, buildName)));
    }
    
    @Override
    public <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> entityFactory, MobCategory category, float xzSize, float ySize, int clientTrackingRange, int updateInterval, String buildName) {
        return EntityType.Builder
                .of(entityFactory, category)
                .sized(xzSize, ySize)
                .clientTrackingRange(clientTrackingRange)
                .updateInterval(updateInterval)
                .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Bumblezone.MODID, buildName)));
    }

    @Override
    public <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> entityFactory, MobCategory category, float xzSize, float ySize, float eyeHeight, int clientTrackingRange, int updateInterval, String buildName) {
        return EntityType.Builder
                .of(entityFactory, category)
                .sized(xzSize, ySize)
                .eyeHeight(eyeHeight)
                .clientTrackingRange(clientTrackingRange)
                .updateInterval(updateInterval)
                .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Bumblezone.MODID, buildName)));
    }
    
    @Override
    public ModInfo getModInfo(String modid) {
        return getModInfo(modid, false);
    }

    @Override
    public ModInfo getModInfo(String modid, boolean qualifierIsVersion) {
        return FabricLoader.getInstance()
                .getModContainer(modid)
                .map(container -> new FabricModInfo(container.getMetadata()))
                .orElse(null);
    }

    @Override
    public Fluid getBucketFluid(BucketItem bucket) {
        Fluid fluid = ((BucketItemAccessor) bucket).bumblezone$getContents();
        return fluid == null ? Fluids.EMPTY : fluid;
    }

    @Override
    public boolean hasCraftingRemainder(ItemStack stack) {
        return stack.getCraftingRemainder() != null;
    }

    @Override
    public ItemStack getCraftingRemainder(ItemStack stack) {
        final ItemStack itemStack = stack.getItem().getCraftingRemainder().create();
        return itemStack == ItemStack.EMPTY ? ItemStack.EMPTY : new ItemStack(itemStack.getItem());
    }

    @Override
    public int getXpDrop(LivingEntity entity, Player attackingPlayer, int xp) {
        return xp;
    }

    @Override
    public boolean isModLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }

    @Override
    public boolean isNeoForge() {
        return false;
    }

    @Override
    public boolean isFakePlayer(ServerPlayer player) {
        //Crude way of doing it but it should work for almost all cases.
        return player != null && player.getClass() != ServerPlayer.class;
    }

    @Override
    public ServerPlayer getFakePlayer(ServerLevel level, GameProfile gameProfile) {
        if (gameProfile == null) {
            return FakePlayer.get(level);
        }
        return FakePlayer.get(level, gameProfile);
    }

    @Override
    public SpawnGroupData finalizeSpawn(Mob entity, ServerLevelAccessor world, SpawnGroupData spawnGroupData, EntitySpawnReason spawnReason) {
        return entity.finalizeSpawn(
                world,
                world.getCurrentDifficultyAt(entity.blockPosition()),
                spawnReason,
                spawnGroupData);
    }

    @Override
    public boolean sendBlockBreakEvent(Level level, BlockPos pos, BlockState state, BlockEntity entity, Player player) {
        boolean result = PlayerBlockBreakEvents.BEFORE.invoker().beforeBlockBreak(level, player, pos, state, entity);
        if (!result) {
            PlayerBlockBreakEvents.CANCELED.invoker().onBlockBreakCanceled(level, player, pos, state, entity);
            return true;
        }
        return false;
    }
    
    @Override
    public void afterBlockBreakEvent(Level level, BlockPos pos, BlockState state, BlockEntity entity, Player player) {
        PlayerBlockBreakEvents.AFTER.invoker().afterBlockBreak(level, player, pos, state, entity);
    }
    
    @Override
    public double getFluidHeight(Entity entity, TagKey<Fluid> fallback, FluidData... fluids) {
        return entity.getFluidHeight(fallback);
    }
    
    @Override
    public boolean isEyesInNoFluid(Entity entity) {
        return ((EntityAccessor)entity).bumblezone$getFluidOnEyes().isEmpty();
    }
    
    @Override
    public InteractionResult performItemUse(Level world, Player user, InteractionHand hand, Fluid fluid, BzCustomBucketItem bzCustomBucketItem) {
        ItemStack itemStack = user.getItemInHand(hand);
        BlockHitResult blockHitResult = ItemAccessor.bumblezone$callGetPlayerPOVHitResult(world, user, fluid == Fluids.EMPTY ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
        if (blockHitResult.getType() == HitResult.Type.MISS) {
            return InteractionResult.PASS;
        }
        else if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResult.PASS;
        }
        else {
            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getDirection();
            BlockPos blockPos2 = blockPos.relative(direction);
            if (world.mayInteract(user, blockPos) && user.mayUseItemAt(blockPos2, direction, itemStack)) {
                BlockState blockState;
                if (fluid == Fluids.EMPTY) {
                    blockState = world.getBlockState(blockPos);
                    if (blockState.getBlock() instanceof BucketPickup fluidDrainable) {
                        ItemStack itemStack2 = fluidDrainable.pickupBlock(user, world, blockPos, blockState);
                        if (!itemStack2.isEmpty()) {
                            user.awardStat(Stats.ITEM_USED.get(bzCustomBucketItem));
                            fluidDrainable.getPickupSound().ifPresent((sound) -> user.playSound(sound, 1.0F, 1.0F));
                            world.gameEvent(user, GameEvent.FLUID_PICKUP, blockPos);
                            ItemUtils.createFilledResult(itemStack, user, itemStack2);
                            if (user instanceof ServerPlayer serverPlayer) {
                                CriteriaTriggers.FILLED_BUCKET.trigger(serverPlayer, itemStack2);
                            }

                            return InteractionResult.SUCCESS;
                        }
                    }

                    return InteractionResult.FAIL;
                }
                else {
                    blockState = world.getBlockState(blockPos);
                    BlockPos blockPos3 = blockState.getBlock() instanceof LiquidBlockContainer && fluid.is(FluidTags.WATER) ? blockPos : blockPos2;
                    user.swingingArm = hand;
                    if (bzCustomBucketItem.emptyContents(user, world, blockPos3, blockHitResult)) {
                        bzCustomBucketItem.checkExtraContent(user, world, itemStack, blockPos3);
                        if (user instanceof ServerPlayer serverPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, blockPos3, itemStack);
                        }

                        user.awardStat(Stats.ITEM_USED.get(bzCustomBucketItem));
                        BucketItem.getEmptySuccessItem(itemStack, user);
                        return InteractionResult.SUCCESS;
                    }
                    else {
                        return InteractionResult.FAIL;
                    }
                }
            }
            else {
                return InteractionResult.FAIL;
            }
        }
    }
    
    @Override
    public boolean isPermissionAllowedAtSpot(Level level, Entity entity, BlockPos pos, boolean placingBlock) {
        if (entity instanceof Player player) {
            if (level instanceof ServerLevel serverLevel && !player.mayInteract(serverLevel, pos)) {
                return false;
            }

            Vec3 centerOfPos = Vec3.atCenterOf(pos);
            BlockHitResult blockHitResult = new BlockHitResult(centerOfPos, Direction.getApproximateNearest(centerOfPos.subtract(player.position())), pos, true);
            InteractionHand hand = player.swingingArm == null ? InteractionHand.MAIN_HAND : player.swingingArm;
            InteractionResult interact = UseBlockCallback.EVENT.invoker().interact(player, level, hand, blockHitResult);
            return interact != InteractionResult.FAIL;
        }
        return true;
    }
    
    @Override
    public boolean isDimensionAllowed(ServerPlayer serverPlayer, ResourceKey<Level> dimension) {
        if (ModChecker.restrictedPortalsPresent) {
            return !RestrictedPortalsCompat.isDimensionDisallowed(serverPlayer, dimension);
        }

        return true;
    }
    
    @Override
    public boolean isItemAbility(ItemStack stack, TagKey<Item> targetBackupTag, String... targetToolAction) {
        return targetBackupTag != null && stack.is(targetBackupTag);
    }
    
    @Override
    public void disableFlight(Player player) {
        player.getAbilities().flying = false;

        if (player.level().isClientSide()) {
            return;
        }

        // Sync on server only
        player.onUpdateAbilities();
    }
    
    @Override
    public boolean isDevEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
    
    @Override
    public boolean isClientEnvironment() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }
    
    @Override
    public boolean shouldMobSplit(Mob parent, List<Mob> children) { return true; }
    
    @Override
    public Fluid getBucketItemFluid(BucketItem stack) {
        return ((BucketItemAccessor)stack).bumblezone$getContents();
    }

    public static MinecraftServer currentMinecraftServer = null;

    @Override
    public RegistryAccess getCurrentRegistryAccess() {
        try {
            if (currentMinecraftServer == null || !currentMinecraftServer.isSameThread()) {
                return GeneralUtilsClient.getClientRegistryAccess();
            }
        }
        catch (Throwable ignored) {}

        return currentMinecraftServer.registryAccess();
    }

    @Override
    public <T extends Module<T>> Optional<T> getModule(Entity entity, ModuleHolder<T> moduleHolder) {
        AttachmentType<T> attachmentType = (AttachmentType<T>) AttachmentRegistryImpl.get(moduleHolder.id());
        if (attachmentType != null) {
            if (!entity.hasAttached(attachmentType)) {
                entity.setAttached(attachmentType, moduleHolder.factory().get());
            }
            return Optional.ofNullable(entity.getAttached(attachmentType));
        }
        return Optional.empty();
    }

    @Override
    public Thread createServerThread(Runnable runnable, String name) {
        return new Thread(runnable, name);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> create(BzMenuTypes.MenuCreator<T> creator) {
        return new MenuType<>(creator::create, FeatureFlags.DEFAULT_FLAGS);
    }
}
