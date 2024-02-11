package com.telepathicgrunt.the_bumblezone.neoforge;

import com.google.common.util.concurrent.AtomicDouble;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.neoforge.BzGeneralConfig;
import com.telepathicgrunt.the_bumblezone.events.AddCreativeTabEntriesEvent;
import com.telepathicgrunt.the_bumblezone.events.BlockBreakEvent;
import com.telepathicgrunt.the_bumblezone.events.ProjectileHitEvent;
import com.telepathicgrunt.the_bumblezone.events.RegisterCommandsEvent;
import com.telepathicgrunt.the_bumblezone.events.RegisterVillagerTradesEvent;
import com.telepathicgrunt.the_bumblezone.events.RegisterWanderingTradesEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BabySpawnEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityAttackedEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityDeathEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityHurtEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntitySpawnEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityTickEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityTravelingToDimensionEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityVisibilityEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.FinishUseItemEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.AddBuiltinResourcePacks;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.DatapackSyncEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.FinalSetupEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.RegisterDataSerializersEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.RegisterEntityAttributesEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.RegisterFlammabilityEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.RegisterReloadListenerEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.RegisterSpawnPlacementsEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.ServerGoingToStartEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.ServerGoingToStopEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.ServerLevelTickEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.SetupEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.TagsUpdatedEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerBreakSpeedEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerEntityInteractEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerGrantAdvancementEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerItemAttackBlockEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerItemUseEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerItemUseOnBlockEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerPickupItemEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerTickEvent;
import com.telepathicgrunt.the_bumblezone.fluids.neoforge.BzFluidBottlesWrapper;
import com.telepathicgrunt.the_bumblezone.fluids.neoforge.BzFluidBucketWrapper;
import com.telepathicgrunt.the_bumblezone.mixin.neoforge.block.FireBlockInvoker;
import com.telepathicgrunt.the_bumblezone.modcompat.neoforge.NeoForgeModChecker;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.packets.networking.neoforge.PacketChannelHelperImpl;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.CapabilityHooks;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.SpawnPlacementRegisterEvent;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingAttackEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforgespi.language.IModFileInfo;
import net.neoforged.neoforgespi.language.IModInfo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class NeoForgeEventManager {
    public static void init(IEventBus modEventBus, IEventBus eventBus) {
        modEventBus.addListener(NeoForgeEventManager::onRegistryEvent);
        modEventBus.addListener(NeoForgeEventManager::onRegisterPackFinder);
        modEventBus.addListener(NeoForgeEventManager::onRegisterAttributes);
        modEventBus.addListener(NeoForgeEventManager::onSetup);
        modEventBus.addListener(EventPriority.LOWEST, NeoForgeEventManager::onFinalSetup);
        modEventBus.addListener(NeoForgeEventManager::onAddTabContents);
        modEventBus.addListener(NeoForgeEventManager::onSpawnPlacements);
        modEventBus.addListener(NeoForgeEventManager::registerBumblezoneCapProviders);
        modEventBus.addListener(PacketChannelHelperImpl::registerPayloads);

        eventBus.addListener(NeoForgeEventManager::onBabySpawn);
        eventBus.addListener(NeoForgeEventManager::onServerStarting);
        eventBus.addListener(NeoForgeEventManager::onServerStopping);
        eventBus.addListener(NeoForgeEventManager::onAddVillagerTrades);
        eventBus.addListener(NeoForgeEventManager::onWanderingTrades);
        eventBus.addListener(NeoForgeEventManager::onRegisterCommand);
        eventBus.addListener(NeoForgeEventManager::onProjectileHit);
        eventBus.addListener(EventPriority.HIGH, NeoForgeEventManager::onItemAttackBlock);
        eventBus.addListener(EventPriority.HIGH, NeoForgeEventManager::onItemUseOnBlock);
        eventBus.addListener(EventPriority.HIGH, NeoForgeEventManager::onItemUse);
        eventBus.addListener(EventPriority.HIGH, NeoForgeEventManager::onProjectileHitHighPriority);
        eventBus.addListener(EventPriority.LOWEST, NeoForgeEventManager::onBlockBreak);
        eventBus.addListener(NeoForgeEventManager::onPlayerTick);
        eventBus.addListener(NeoForgeEventManager::onPickupItem);
        eventBus.addListener(NeoForgeEventManager::onGrantAdvancement);
        eventBus.addListener(NeoForgeEventManager::onInteractEntity);
        eventBus.addListener(NeoForgeEventManager::onBreakSpeed);
        eventBus.addListener(NeoForgeEventManager::onTagsUpdate);
        eventBus.addListener(NeoForgeEventManager::onLevelTick);
        eventBus.addListener(NeoForgeEventManager::onAddReloadListeners);
        eventBus.addListener(NeoForgeEventManager::onDatapackSync);
        eventBus.addListener(NeoForgeEventManager::onEntityAttacked);
        eventBus.addListener(NeoForgeEventManager::onEntityDeath);
        eventBus.addListener(EventPriority.LOWEST, NeoForgeEventManager::onEntityDeathLowest);
        eventBus.addListener(NeoForgeEventManager::onEntitySpawn);
        eventBus.addListener(NeoForgeEventManager::onEntityTick);
        eventBus.addListener(NeoForgeEventManager::onEntityDimensionTravel);
        eventBus.addListener(NeoForgeEventManager::onEntityVisibility);
        eventBus.addListener(NeoForgeEventManager::onFinishUseItem);
        eventBus.addListener(EventPriority.LOWEST, NeoForgeEventManager::onEntityHurtLowest);
    }

    private static void onAddTabContents(BuildCreativeModeTabContentsEvent event) {
        AddCreativeTabEntriesEvent.EVENT.invoke(new AddCreativeTabEntriesEvent(toType(BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(event.getTab()).orElse(null)), event.getTab(), event.hasPermissions(), event::accept));
    }

    private static AddCreativeTabEntriesEvent.Type toType(ResourceKey<CreativeModeTab> tab) {
        if (CreativeModeTabs.BUILDING_BLOCKS.equals(tab)) return AddCreativeTabEntriesEvent.Type.BUILDING;
        else if (CreativeModeTabs.COLORED_BLOCKS.equals(tab)) return AddCreativeTabEntriesEvent.Type.COLORED;
        else if (CreativeModeTabs.NATURAL_BLOCKS.equals(tab)) return AddCreativeTabEntriesEvent.Type.NATURAL;
        else if (CreativeModeTabs.FUNCTIONAL_BLOCKS.equals(tab)) return AddCreativeTabEntriesEvent.Type.FUNCTIONAL;
        else if (CreativeModeTabs.REDSTONE_BLOCKS.equals(tab)) return AddCreativeTabEntriesEvent.Type.REDSTONE;
        else if (CreativeModeTabs.TOOLS_AND_UTILITIES.equals(tab)) return AddCreativeTabEntriesEvent.Type.TOOLS;
        else if (CreativeModeTabs.COMBAT.equals(tab)) return AddCreativeTabEntriesEvent.Type.COMBAT;
        else if (CreativeModeTabs.FOOD_AND_DRINKS.equals(tab)) return AddCreativeTabEntriesEvent.Type.FOOD;
        else if (CreativeModeTabs.INGREDIENTS.equals(tab)) return AddCreativeTabEntriesEvent.Type.INGREDIENTS;
        else if (CreativeModeTabs.SPAWN_EGGS.equals(tab)) return AddCreativeTabEntriesEvent.Type.SPAWN_EGGS;
        else if (CreativeModeTabs.OP_BLOCKS.equals(tab)) return AddCreativeTabEntriesEvent.Type.OPERATOR;
        return AddCreativeTabEntriesEvent.Type.CUSTOM;
    }

    private static void onSetup(FMLCommonSetupEvent event) {
        SetupEvent.EVENT.invoke(new SetupEvent(event::enqueueWork));

        FluidInteractionRegistry.addInteraction(NeoForgeMod.LAVA_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
                BzFluids.SUGAR_WATER_FLUID_TYPE.get().flowing().getFluidType(),
                fluidState -> fluidState.isSource() ? Blocks.OBSIDIAN.defaultBlockState() : BzBlocks.SUGAR_INFUSED_COBBLESTONE.get().defaultBlockState()
        ));

        FluidInteractionRegistry.addInteraction(NeoForgeMod.LAVA_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
                BzFluids.SUGAR_WATER_FLUID_TYPE.get().source().getFluidType(),
                fluidState -> fluidState.isSource() ? Blocks.OBSIDIAN.defaultBlockState() : BzBlocks.SUGAR_INFUSED_COBBLESTONE.get().defaultBlockState()
        ));

        event.enqueueWork(() ->
                RegisterFlammabilityEvent.EVENT.invoke(new RegisterFlammabilityEvent((item, igniteOdds, burnOdds) ->
                        ((FireBlockInvoker) Blocks.FIRE).callSetFlammable(item, igniteOdds, burnOdds)))
        );
    }

    private static void onFinalSetup(FMLCommonSetupEvent event) {
        FinalSetupEvent.EVENT.invoke(new FinalSetupEvent(event::enqueueWork));
        event.enqueueWork(NeoForgeModChecker::setupModCompat);
    }

    private static void onServerStarting(ServerAboutToStartEvent event) {
        ServerGoingToStartEvent.EVENT.invoke(new ServerGoingToStartEvent(event.getServer()));
    }

    private static void onServerStopping(ServerStoppingEvent event) {
        ServerGoingToStopEvent.EVENT.invoke(ServerGoingToStopEvent.INSTANCE);
    }

    private static void onRegistryEvent(RegisterEvent event) {
        if (event.getRegistryKey() == NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS) {
            RegisterDataSerializersEvent.EVENT.invoke(new RegisterDataSerializersEvent(
                    (id, serializer) -> event.register(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, id, () -> serializer)));
        }
    }

    private static void onRegisterPackFinder(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            AddBuiltinResourcePacks.EVENT.invoke(new AddBuiltinResourcePacks((id, displayName, mode) -> {
                IModFileInfo info = getPackInfo(id);
                Path resourcePath = info.getFile().findResource("resourcepacks/" + id.getPath());

                final Pack.Info packInfo = createInfoForLatest(displayName, mode == AddBuiltinResourcePacks.PackMode.FORCE_ENABLED);
                final Pack pack = Pack.create(
                        Bumblezone.MODID + ":add_pack/" + id.getPath(), displayName,
                        mode == AddBuiltinResourcePacks.PackMode.FORCE_ENABLED,
                        new PathPackResources.PathResourcesSupplier(resourcePath, true),
                        packInfo,
                        Pack.Position.BOTTOM,
                        false,
                        createSource(mode)
                );
                event.addRepositorySource((packConsumer) -> packConsumer.accept(pack));
            }));
        }
    }

    private static IModFileInfo getPackInfo(ResourceLocation pack) {
        if (!FMLLoader.isProduction()) {
            for (IModInfo mod : ModList.get().getMods()) {
                if (mod.getModId().startsWith("generated_") && fileExists(mod, "resourcepacks/" + pack.getPath())) {
                    return mod.getOwningFile();
                }
            }
        }
        return ModList.get().getModFileById(pack.getNamespace());
    }

    private static boolean fileExists(IModInfo info, String path) {
        return Files.exists(info.getOwningFile().getFile().findResource(path.split("/")));
    }

    private static Pack.Info createInfoForLatest(Component description, boolean hidden) {
        return new Pack.Info(
                description,
                PackCompatibility.COMPATIBLE,
                FeatureFlagSet.of(),
                new ArrayList<>(),
                hidden
        );
    }

    private static PackSource createSource(AddBuiltinResourcePacks.PackMode mode) {
        final Component text = Component.translatable("pack.source.builtin");
        return PackSource.create(
                component -> Component.translatable("pack.nameAndSource", component, text).withStyle(ChatFormatting.GRAY),
                mode != AddBuiltinResourcePacks.PackMode.USER_CONTROLLED
        );
    }

    private static void onBabySpawn(BabyEntitySpawnEvent event) {
        boolean cancel = BabySpawnEvent.EVENT.invoke(new BabySpawnEvent(event.getParentA(), event.getParentB(), event.getCausedByPlayer(), event.getChild()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }

    private static void onRegisterAttributes(EntityAttributeCreationEvent event) {
        RegisterEntityAttributesEvent.EVENT.invoke(new RegisterEntityAttributesEvent((entity, builder) -> event.put(entity, builder.build())));
    }

    private static void onAddVillagerTrades(VillagerTradesEvent event) {
        RegisterVillagerTradesEvent.EVENT.invoke(new RegisterVillagerTradesEvent(event.getType(), (i, listing) -> event.getTrades().get(i.intValue()).add(listing)));
    }

    private static void onWanderingTrades(WandererTradesEvent event) {
        RegisterWanderingTradesEvent.EVENT.invoke(new RegisterWanderingTradesEvent(event.getGenericTrades()::add, event.getRareTrades()::add));
    }

    private static void onRegisterCommand(net.neoforged.neoforge.event.RegisterCommandsEvent event) {
        RegisterCommandsEvent.EVENT.invoke(new RegisterCommandsEvent(event.getDispatcher(), event.getCommandSelection(), event.getBuildContext()));
    }

    private static void onProjectileHit(ProjectileImpactEvent event) {
        boolean cancel = ProjectileHitEvent.EVENT.invoke(new ProjectileHitEvent(event.getProjectile(), event.getRayTraceResult()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }

    private static void onProjectileHitHighPriority(ProjectileImpactEvent event) {
        boolean cancel = ProjectileHitEvent.EVENT_HIGH.invoke(new ProjectileHitEvent(event.getProjectile(), event.getRayTraceResult()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }

    private static void onBlockBreak(BlockEvent.BreakEvent event) {
        boolean cancel = BlockBreakEvent.EVENT_LOWEST.invoke(new BlockBreakEvent(event.getPlayer(), event.getState()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }

    private static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerTickEvent eventObject = new PlayerTickEvent(event.player, event.phase == TickEvent.Phase.END);

        PlayerTickEvent.EVENT.invoke(eventObject);
        if (event.side == LogicalSide.CLIENT) {
            PlayerTickEvent.CLIENT_EVENT.invoke(eventObject);
        }
    }

    private static void onPickupItem(PlayerEvent.ItemPickupEvent event) {
        PlayerPickupItemEvent.EVENT.invoke(new PlayerPickupItemEvent(event.getEntity(), event.getStack()));
    }

    private static void onGrantAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
        PlayerGrantAdvancementEvent.EVENT.invoke(new PlayerGrantAdvancementEvent(event.getAdvancement().value(), event.getEntity()));
    }

    private static void onInteractEntity(PlayerInteractEvent.EntityInteract event) {
        InteractionResult result = PlayerEntityInteractEvent.EVENT.invoke(new PlayerEntityInteractEvent(event.getEntity(), event.getTarget(), event.getHand()));
        if (result != null) {
            event.setCancellationResult(result);
            event.setCanceled(true);
        }
    }

    private static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        AtomicDouble speed = new AtomicDouble(event.getNewSpeed());
        PlayerBreakSpeedEvent.EVENT.invoke(new PlayerBreakSpeedEvent(event.getEntity(), event.getState(), speed));
        event.setNewSpeed(speed.floatValue());
    }

    private static void onTagsUpdate(net.neoforged.neoforge.event.TagsUpdatedEvent event) {
        TagsUpdatedEvent.EVENT.invoke(new TagsUpdatedEvent(event.getRegistryAccess(), event.getUpdateCause() == net.neoforged.neoforge.event.TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED));
    }

    private static void onSpawnPlacements(SpawnPlacementRegisterEvent event) {
        RegisterSpawnPlacementsEvent.EVENT.invoke(new RegisterSpawnPlacementsEvent(NeoForgeEventManager.registerPlacement(event)));
    }

    private static RegisterSpawnPlacementsEvent.Registrar registerPlacement(SpawnPlacementRegisterEvent event) {
        return new RegisterSpawnPlacementsEvent.Registrar() {
            @Override
            public <T extends Mob> void register(EntityType<T> type, RegisterSpawnPlacementsEvent.Placement<T> place) {
                event.register(type, place.spawn(), place.height(), place.predicate(), SpawnPlacementRegisterEvent.Operation.AND);
            }
        };
    }

    private static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.level.isClientSide) return;
        ServerLevelTickEvent.EVENT.invoke(new ServerLevelTickEvent(event.level, event.phase == TickEvent.Phase.END));
    }

    private static void onAddReloadListeners(AddReloadListenerEvent event) {
        RegisterReloadListenerEvent.EVENT.invoke(new RegisterReloadListenerEvent((id, listener) -> event.addListener(listener)));
    }

    private static void onDatapackSync(OnDatapackSyncEvent event) {
        if (FMLEnvironment.dist.isDedicatedServer()) {
            if (event.getPlayer() != null) {
                DatapackSyncEvent.EVENT.invoke(new DatapackSyncEvent(event.getPlayer()));
            }
            else {
                event.getPlayerList().getPlayers().forEach(player -> DatapackSyncEvent.EVENT.invoke(new DatapackSyncEvent(player)));
            }
        }
    }

    private static void onFinishUseItem(LivingEntityUseItemEvent.Finish event) {
        ItemStack stack = FinishUseItemEvent.EVENT.invoke(new FinishUseItemEvent(event.getEntity(), event.getItem(), event.getDuration()));
        if (stack != null) {
            event.setResultStack(stack);
        }
    }

    private static void onEntityVisibility(LivingEvent.LivingVisibilityEvent event) {
        EntityVisibilityEvent visibilityEvent = new EntityVisibilityEvent(event.getVisibilityModifier(), event.getEntity(), event.getLookingEntity());
        EntityVisibilityEvent.EVENT.invoke(visibilityEvent);
        event.modifyVisibility(visibilityEvent.visibility() / event.getVisibilityModifier());
    }

    private static void onEntityDimensionTravel(EntityTravelToDimensionEvent event) {
        boolean cancel = EntityTravelingToDimensionEvent.EVENT.invoke(new EntityTravelingToDimensionEvent(event.getDimension(), event.getEntity()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }

    private static void onEntityTick(LivingEvent.LivingTickEvent event) {
        EntityTickEvent.EVENT.invoke(new EntityTickEvent(event.getEntity()));
    }

    private static void onEntitySpawn(MobSpawnEvent.FinalizeSpawn event) {
        boolean cancel = EntitySpawnEvent.EVENT.invoke(new EntitySpawnEvent(event.getEntity(), event.getLevel(), event.getEntity().isBaby(), event.getSpawnType()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
            event.setSpawnCancelled(true);
        }
    }

    private static void onEntityHurtLowest(LivingHurtEvent event) {
        boolean cancel = EntityHurtEvent.EVENT_LOWEST.invoke(new EntityHurtEvent(event.getEntity(), event.getSource(), event.getAmount()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }

    private static void onEntityDeath(LivingDeathEvent event) {
        boolean cancel = EntityDeathEvent.EVENT.invoke(new EntityDeathEvent(event.getEntity(), event.getSource()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }

    private static void onEntityDeathLowest(LivingDeathEvent event) {
        boolean cancel = EntityDeathEvent.EVENT_LOWEST.invoke(new EntityDeathEvent(event.getEntity(), event.getSource()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }


    private static void onEntityAttacked(LivingAttackEvent event) {
        boolean cancel = EntityAttackedEvent.EVENT.invoke(new EntityAttackedEvent(event.getEntity(), event.getSource(), event.getAmount()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }

    public static void onItemAttackBlock(PlayerInteractEvent.LeftClickBlock event) {
        PlayerItemAttackBlockEvent eventBz = new PlayerItemAttackBlockEvent(event.getEntity(), event.getLevel(), event.getHand(), event.getItemStack());
        InteractionResult result = PlayerItemAttackBlockEvent.EVENT_HIGH.invoke(eventBz);
        if (result != null) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }

    public static void onItemUseOnBlock(PlayerInteractEvent.RightClickBlock event) {
        PlayerItemUseOnBlockEvent eventBz = new PlayerItemUseOnBlockEvent(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec(), event.getItemStack());
        InteractionResult result = PlayerItemUseOnBlockEvent.EVENT_HIGH.invoke(eventBz);
        if (result != null) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }

    public static void onItemUse(PlayerInteractEvent.RightClickItem event) {
        PlayerItemUseEvent eventBz = new PlayerItemUseEvent(event.getEntity(), event.getLevel(), event.getItemStack());
        if (PlayerItemUseEvent.EVENT_HIGH.invoke(eventBz)) {
            event.setCanceled(true);
        }
    }

    public static void registerBumblezoneCapProviders(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BzBlockEntities.HONEY_COCOON.get(),
                (honeyCocoon, side) -> new SidedInvWrapper(honeyCocoon, Direction.UP));

        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (stack, ctx) -> new BzFluidBucketWrapper(stack),
                BzItems.HONEY_BUCKET.get());

        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (stack, ctx) -> new BzFluidBucketWrapper(stack),
                BzItems.ROYAL_JELLY_BUCKET.get());

        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (stack, ctx) -> new BzFluidBucketWrapper(stack),
                BzItems.SUGAR_WATER_BUCKET.get());

        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (stack, ctx) -> new BzFluidBottlesWrapper(stack, BzFluids.ROYAL_JELLY_FLUID.get()),
                BzItems.ROYAL_JELLY_BOTTLE.get());

        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (stack, ctx) -> new BzFluidBottlesWrapper(stack, BzFluids.SUGAR_WATER_FLUID.get()),
                BzItems.SUGAR_WATER_BOTTLE.get());

        if (BzGeneralConfig.bzHoneyFluidFromHoneyBottles.get()) {
            event.registerItem(
                    Capabilities.FluidHandler.ITEM,
                    (stack, ctx) -> new BzFluidBottlesWrapper(stack, BzFluids.HONEY_FLUID.get()),
                    Items.HONEY_BOTTLE);
        }
    }
}
