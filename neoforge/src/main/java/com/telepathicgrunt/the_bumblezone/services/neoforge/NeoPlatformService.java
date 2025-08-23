package com.telepathicgrunt.the_bumblezone.services.neoforge;

import com.mojang.authlib.GameProfile;
import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.entities.neoforge.DisableFlightAttribute;
import com.telepathicgrunt.the_bumblezone.items.BzCustomBucketItem;
import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;
import com.telepathicgrunt.the_bumblezone.platform.ModInfo;
import com.telepathicgrunt.the_bumblezone.services.PlatformService;
import com.telepathicgrunt.the_bumblezone.utils.neoforge.NeoForgeModInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.util.BlockSnapshot;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class NeoPlatformService implements PlatformService {
    
    @Override
    public <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> entityFactory, MobCategory category, float size, int clientTrackingRange, int updateInterval, String buildName) {
        return EntityType.Builder
                .of(entityFactory, category)
                .sized(size, size)
                .clientTrackingRange(clientTrackingRange)
                .updateInterval(updateInterval)
                .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, buildName)));
    }
    
    @Override
    public <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> entityFactory, MobCategory category, float xzSize, float ySize, int clientTrackingRange, int updateInterval, String buildName) {
        return EntityType.Builder
                .of(entityFactory, category)
                .sized(xzSize, ySize)
                .clientTrackingRange(clientTrackingRange)
                .updateInterval(updateInterval)
                .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, buildName)));
    }
    
    @Override
    public <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> entityFactory, MobCategory category, float xzSize, float ySize, float eyeHeight, int clientTrackingRange, int updateInterval, String buildName) {
        return EntityType.Builder
                .of(entityFactory, category)
                .sized(xzSize, ySize)
                .eyeHeight(eyeHeight)
                .clientTrackingRange(clientTrackingRange)
                .updateInterval(updateInterval)
                .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, buildName)));
    }
    
    @Override
    public ModInfo getModInfo(String modid) {
        return getModInfo(modid, false);
    }

    @Override
    public ModInfo getModInfo(String modid, boolean qualifierIsVersion) {
        return ModList.get().getModContainerById(modid)
                .map(container -> new NeoForgeModInfo(container.getModInfo(), qualifierIsVersion))
                .orElse(null);
    }

    @Override
    public Fluid getBucketFluid(BucketItem bucket) {
        return bucket.content;
    }

    @Override
    public boolean hasCraftingRemainder(ItemStack stack) {
        return stack.hasCraftingRemainingItem();
    }

    @Override
    public ItemStack getCraftingRemainder(ItemStack stack) {
        return stack.getCraftingRemainingItem();
    }

    @Override
    public int getXpDrop(LivingEntity entity, Player attackingPlayer, int xp) {
        return EventHooks.getExperienceDrop(entity, attackingPlayer, xp);
    }

    @Override
    public boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    @Override
    public boolean isNeoForge() {
        return true;
    }

    @Override
    public boolean isFakePlayer(ServerPlayer player) {
        return player.isFakePlayer();
    }

    @Override
    public ServerPlayer getFakePlayer(ServerLevel level, GameProfile gameProfile) {
        if (gameProfile == null) {
            return FakePlayerFactory.getMinecraft(level);
        }
        return FakePlayerFactory.get(level, gameProfile);
    }

    @Override
    public SpawnGroupData finalizeSpawn(Mob entity, ServerLevelAccessor world, SpawnGroupData spawnGroupData, MobSpawnType spawnReason) {
        return EventHooks.finalizeMobSpawn(entity, world, world.getCurrentDifficultyAt(BlockPos.containing(entity.position())), spawnReason, spawnGroupData);
    }
    
    @Override
    public boolean sendBlockBreakEvent(Level level, BlockPos pos, BlockState state, BlockEntity entity, Player player) {
        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, pos, state, player);
        NeoForge.EVENT_BUS.post(event);
        return event.isCanceled();
    }
    
    @Override
    public void afterBlockBreakEvent(Level level, BlockPos pos, BlockState state, BlockEntity entity, Player player) {
        //Do nothing
    }
    
    @Override
    public double getFluidHeight(Entity entity, TagKey<Fluid> fallback, FluidData... fluids) {
        for (int i = 0, size = fluids.length; i < size; i++) {
            FluidData fluid = fluids[i];
            double forgeTypeHeight = entity.getFluidTypeHeight(fluid.still().get().getFluidType());
            if (forgeTypeHeight > 0) {
                return forgeTypeHeight;
            }
        }
        return entity.getFluidHeight(fallback);
    }
    
    @Override
    public boolean isEyesInNoFluid(Entity entity) {
        return entity.getEyeInFluidType().isAir();
    }
    
    @Override
    public InteractionResultHolder<ItemStack> performItemUse(Level world, Player user, InteractionHand hand, Fluid fluid, BzCustomBucketItem bzCustomBucketItem) {
        return InteractionResultHolder.pass(user.getItemInHand(hand));
    }
    
    @Override
    public boolean isPermissionAllowedAtSpot(Level level, Entity entity, BlockPos pos, boolean placingBlock) {
        if (entity instanceof Player player && !player.mayInteract(level, pos)) {
            return false;
        }

        if (placingBlock) {
            return !EventHooks.onBlockPlace(entity, BlockSnapshot.create(level.dimension(), level, pos), Direction.UP);
        }
        else if (entity instanceof LivingEntity livingEntity) {
            return EventHooks.onEntityDestroyBlock(livingEntity, pos, level.getBlockState(pos));
        }
        return true;
    }
    
    @Override
    public boolean isDimensionAllowed(ServerPlayer serverPlayer, ResourceKey<Level> dimension) {
        return CommonHooks.onTravelToDimension(serverPlayer, dimension);
    }
    
    @Override
    public boolean isItemAbility(ItemStack stack, Class<?> targetBackupClass, String... targetToolAction) {
        return Arrays.stream(targetToolAction).anyMatch(actionString -> stack.canPerformAction(ItemAbility.get(actionString)))
                || (targetBackupClass != null && targetBackupClass.isInstance(stack.getItem()));
    }
    
    @Override
    public void disableFlight(Player player) {
        if (player.level().isClientSide()) {
            return;
        }

        // Sync on server only
        player.onUpdateAbilities();

        AttributeInstance attributeInstance = player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT);
        if (attributeInstance != null && !attributeInstance.hasModifier(DisableFlightAttribute.DISABLE_FLIGHT_RL)) {
            attributeInstance.addTransientModifier(DisableFlightAttribute.DISABLE_FLIGHT);
        }
    }
    
    @Override
    public boolean isDevEnvironment() {
        return !FMLLoader.isProduction();
    }
    
    @Override
    public boolean isClientEnvironment() {
        return FMLLoader.getDist().isClient();
    }
    
    @Override
    public boolean shouldMobSplit(Mob parent, List<Mob> children) {
        return !EventHooks.onMobSplit(parent, children).isCanceled();
    }
    
    @Override
    public Fluid getBucketItemFluid(BucketItem stack) {
        return stack.content;
    }
    
    @Override
    public RegistryAccess getCurrentRegistryAccess() {
        try {
            if (EffectiveSide.get().isClient()) {
                return GeneralUtilsClient.getClientRegistryAccess();
            }
        }
        catch (Throwable ignored) {}

        return ServerLifecycleHooks.getCurrentServer().registryAccess();
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> create(BzMenuTypes.MenuCreator<T> creator) {
        return new MenuType<>(creator::create, FeatureFlags.DEFAULT_FLAGS);
    }

    @Override
    public <T extends Module<T>> Optional<T> getModule(Entity entity, ModuleHolder<T> moduleHolder) {
        AttachmentType<T> attachmentType = (AttachmentType<T>) NeoForgeRegistries.ATTACHMENT_TYPES.get(moduleHolder.id());
        if (attachmentType != null) {
            return Optional.of(entity.getData(attachmentType));
        }
        return Optional.empty();
    }

    @Override
    public Thread createServerThread(Runnable runnable, String name) {
        return new Thread(SidedThreadGroups.SERVER, runnable, name);
    }
}
